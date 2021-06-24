package co.ke.tracom.bprgateway.web.irembo.service;

import co.ke.tracom.bprgateway.core.util.RRNGenerator;
import co.ke.tracom.bprgateway.web.agenttransactions.dto.response.AuthenticateAgentResponse;
import co.ke.tracom.bprgateway.web.agenttransactions.services.AgentTransactionService;
import co.ke.tracom.bprgateway.web.bankbranches.entity.BPRBranches;
import co.ke.tracom.bprgateway.web.bankbranches.service.BPRBranchService;
import co.ke.tracom.bprgateway.web.depositmoney.data.response.DepositMoneyResult;
import co.ke.tracom.bprgateway.web.eucl.dto.response.EUCLPaymentResponse;
import co.ke.tracom.bprgateway.web.eucl.dto.response.MeterNoValidationResponse;
import co.ke.tracom.bprgateway.web.irembo.dto.request.BillNumberValidationRequest;
import co.ke.tracom.bprgateway.web.irembo.dto.request.IremboBillPaymentRequest;
import co.ke.tracom.bprgateway.web.irembo.dto.request.IremboRequest;
import co.ke.tracom.bprgateway.web.irembo.dto.response.*;
import co.ke.tracom.bprgateway.web.irembo.entity.IremboPaymentNotifications;
import co.ke.tracom.bprgateway.web.irembo.repository.IremboPaymentNotificationsRepository;
import co.ke.tracom.bprgateway.web.switchparameters.repository.XSwitchParameterRepository;
import co.ke.tracom.bprgateway.web.t24communication.services.T24Channel;
import co.ke.tracom.bprgateway.web.transactions.entities.T24TXNQueue;
import co.ke.tracom.bprgateway.web.transactions.services.TransactionService;
import co.ke.tracom.bprgateway.web.util.services.BaseServiceProcessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.TimeZone;

import static co.ke.tracom.bprgateway.web.t24communication.services.T24Channel.MASKED_T24_PASSWORD;
import static co.ke.tracom.bprgateway.web.t24communication.services.T24Channel.MASKED_T24_USERNAME;

@Slf4j
@RequiredArgsConstructor
@Service
public class IremboService {
    @Value("${merchant.account.validation}")
    private String agentValidation;

    private final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
    private final String VALIDATE_URI = "/api/v1/clients/validateBill";

    private final AgentTransactionService agentTransactionService;
    private final BaseServiceProcessor baseServiceProcessor;
    private final BPRBranchService bprBranchService;
    private final T24Channel t24Channel;
    private final TransactionService transactionService;
    private final XSwitchParameterRepository xSwitchParameterRepository;
    private final IremboPaymentNotificationsRepository iremboPaymentNotificationsRepository;

    public IremboBillNoValidationResponse validateIremboBillNo(BillNumberValidationRequest request, String transactionRefNo) {
        // Validate agent credentials
        Optional<AuthenticateAgentResponse> optionalAuthenticateAgentResponse = baseServiceProcessor.authenticateAgentUsernamePassword(request.getCredentials(), agentValidation);
        if (optionalAuthenticateAgentResponse.isEmpty()) {
            log.info(
                    "Agent Float Deposit:[Failed] Missing agent information %n");
            return IremboBillNoValidationResponse.builder()
                    .status("117")
                    .message("Missing agent information")
                    .data(null)
                    .build();

        } else if (optionalAuthenticateAgentResponse.get().getCode() != HttpStatus.OK.value()) {
            return IremboBillNoValidationResponse.builder()
                    .status(String.valueOf(
                            optionalAuthenticateAgentResponse.get().getCode())
                    )
                    .message(optionalAuthenticateAgentResponse.get().getMessage()).build();
        }

        HttpURLConnection httpConnection = null;
        try {

            String IREMBOGATEWAYVALIDATEURL = xSwitchParameterRepository.findByParamName("IREMBOGATEWAYVALIDATEURL").get().getParamValue();
            String IREMBOPIVOTACCESSID = xSwitchParameterRepository.findByParamName("IREMBOPIVOTACCESSID").get().getParamValue();
            String IREMBOPIVOTSECRETKEY = xSwitchParameterRepository.findByParamName("IREMBOPIVOTSECRETKEY").get().getParamValue();


            String customerBillNo = request.getCustomerBillNo().trim();
            IremboRequest iremboRequest = new IremboRequest();
            iremboRequest.setBillNumber(customerBillNo);

            SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
            String content_type = "application/json";
            String content_MD5 = "";
            String uri = VALIDATE_URI;

            Date date2 = new Date();
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            String gmtdate = sdf.format(date2);
            String gmtdatefinal = gmtdate.substring(0, 3).toUpperCase() + gmtdate.substring(3, gmtdate.length());
            String canonical_str = content_type + "," + content_MD5 + "," + uri + "," + gmtdatefinal;
            String hmac = calculateRFC2104HMACBase64(canonical_str, IREMBOPIVOTSECRETKEY);
            URL targetUrl = new URL(IREMBOGATEWAYVALIDATEURL);

            httpConnection = (HttpURLConnection) targetUrl.openConnection();
            httpConnection.setConnectTimeout(5000); // set timeout to 10 seconds
            httpConnection.setDoOutput(true);
            httpConnection.setDoInput(true);
            httpConnection.setRequestMethod("POST");
            httpConnection.setRequestProperty("Content-Type", "application/json");
            httpConnection.setRequestProperty("Authorization", "APIAuth " + IREMBOPIVOTACCESSID + ":" + hmac);
            httpConnection.setRequestProperty("Date", gmtdatefinal);
            httpConnection.setUseCaches(false);
            httpConnection.setReadTimeout(20000);


            OutputStream outputStream = httpConnection.getOutputStream();
            outputStream.write(new ObjectMapper().writeValueAsString(iremboRequest).getBytes(StandardCharsets.UTF_8));
            outputStream.flush();

            log.info("Irembo bill number validation " + iremboRequest.getBillNumber() + " HTTP ResponseCode code : "
                    + httpConnection.getResponseCode());
            BufferedReader responseBuffer = new BufferedReader(
                    new InputStreamReader((httpConnection.getInputStream())));

            String inputLine;
            StringBuilder stringBuilder = new StringBuilder();

            while ((inputLine = responseBuffer.readLine()) != null) {
                stringBuilder.append(inputLine);
            }

            IremboValidateRes ires = new ObjectMapper().readValue(stringBuilder.toString(), IremboValidateRes.class);
            if (ires.getResultCode().equals("200")) {


                String dname = (ires.getBillDetails().getCustomerName() == null
                        || ires.getBillDetails().getCustomerName() == "") ? ires.getBillDetails().getRraAccountName()
                        : ires.getBillDetails().getCustomerName();

                String mobile = (ires.getBillDetails().getMobile() == null
                        || ires.getBillDetails().getMobile().equals("")) ? "NA" : ires.getBillDetails().getMobile();

                String f60res = ires.getBillDetails().getBillNumber() + "#" + ires.getMessage() + "#"
                        + ires.getBillDetails().getBillerCode() + "#" + ires.getBillDetails().getDescription() + "#"
                        + ires.getBillDetails().getAmount() + "#" + ires.getBillDetails().getCurrencyCode() + "#"
                        + ires.getBillDetails().getRraAccountNumber() + "#" + ires.getBillDetails().getExpiryDate()
                        + "#" + ires.getBillDetails().getRraAccountName() + "#" + dname + "#"
                        + ires.getBillDetails().getCreatedAt() + "#" + ires.getBillDetails().getTransactionType() + "#"
                        + ires.getResultCode() + "#" + mobile;

                log.info("Irembo validation result for bill number [" + request.getCustomerBillNo() + "] : " + f60res);
                IremboValidationData data = IremboValidationData.builder()
                        .billNo(ires.getBillDetails().getBillNumber())
                        .customerName(dname)
                        .mobileNo(mobile)
                        .RRAAccountNo(ires.getBillDetails().getRraAccountNumber())
                        .amount(ires.getBillDetails().getAmount())
                        .currencyCode(ires.getBillDetails().getCurrencyCode())
                        .createdAt(ires.getBillDetails().getCreatedAt())
                        .expiryDate(ires.getBillDetails().getExpiryDate())
                        .transactionType(ires.getBillDetails().getTransactionType())
                        .build();

                return IremboBillNoValidationResponse
                        .builder()
                        .status("00")
                        .message("Bill no validation successful")
                        .data(data)
                        .build();

            } else {
                log.info("Irembo bill validation failed. Error: " + ires.getMessage());
                return IremboBillNoValidationResponse
                        .builder()
                        .status("098")
                        .message("Bill no validation failed. " + ires.getMessage())
                        .data(null)
                        .build();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException | SignatureException | NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            log.info("Irembo validation failure: Exception occurred.");
            return IremboBillNoValidationResponse
                    .builder()
                    .status("098")
                    .message("Transaction failed. Please try again.")
                    .data(null)
                    .build();
        } finally {
            httpConnection.disconnect();
        }

        return null;
    }

    public String calculateRFC2104HMACBase64(String data, String key)
            throws SignatureException, NoSuchAlgorithmException, InvalidKeyException {
        Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
        mac.init(new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM));
        byte[] rawHmac = mac.doFinal(data.getBytes());
        return new String(Base64.encodeBase64(rawHmac));
    }

    public IremboPaymentResponse processPayment(IremboBillPaymentRequest request, String transactionRefNo) {

        try {
            Optional<AuthenticateAgentResponse> optionalAuthenticateAgentResponse = baseServiceProcessor.authenticateAgentUsernamePassword(request.getCredentials(), agentValidation);
            if (optionalAuthenticateAgentResponse.isEmpty()) {
                log.info(
                        "Agent Float Deposit:[Failed] Missing agent information %n");
                return IremboPaymentResponse.builder()
                        .status("117")
                        .message("Missing agent information")
                        .data(null)
                        .build();

            } else if (optionalAuthenticateAgentResponse.get().getCode() != HttpStatus.OK.value()) {
                return IremboPaymentResponse
                        .builder()
                        .status(String.valueOf(
                                optionalAuthenticateAgentResponse.get().getCode()))
                        .message(optionalAuthenticateAgentResponse.get().getMessage())
                        .build();
            }
            AuthenticateAgentResponse authenticateAgentResponse = optionalAuthenticateAgentResponse.get();
            String tid = "PC";

            BPRBranches bprBranches = bprBranchService.fetchBranchAccountsByBranchCode(authenticateAgentResponse.getData().getAccountNumber());
            String branchId = bprBranches.getId();

            String chargeCreditPLAccount = "PL64200\\HOF";

            if (branchId.isEmpty() || null == bprBranches.getCompanyName()) {
                return IremboPaymentResponse.builder()
                        .status("065")
                        .message("Missing agent branch information")
                        .data(null)
                        .build();
            }


            String branchname = bprBranches.getCompanyName();
            String irembocharges = clientPaymentConfirmationValidate(authenticateAgentResponse.getData().getAccountNumber(), branchId, request, transactionRefNo);
            long irembochargeslong = Long.parseLong(irembocharges);


            // Save de60 in db
//            String bill_id = split60[0];
//            String payment_status_message = split60[1];
//            String biller_code = split60[2];
//            String description = split60[3];
//            String amount = split60[4];
//            String ccy = split60[5];
//            String IREMBO_account_number = split60[6];
//            String expirydate = split60[7];
//            String IREMBO_account_name = split60[8];
//            String payer_name = split60[9];
//            String bill_created_date = split60[10];
//            String payment_txn_type = split60[11];
//            String payment_status = split60[12];
//            String payer_phone = split60[13];


            String channel = "Channel";
            String txnref = transactionRefNo;


            String amountf4 = request.getAmount();
            long iremboamt = Long.parseLong(amountf4);
            long agentAccountBalance = agentTransactionService.fetchAgentAccountBalanceOnly(authenticateAgentResponse.getData().getAccountNumber());
            System.out.printf("\n\nIrembo ref %s  irembo amt %s , Charges %s , agent balance %s \n\n", txnref, iremboamt,
                    irembochargeslong, agentAccountBalance);

            if (agentAccountBalance < iremboamt + irembochargeslong) {
                return IremboPaymentResponse.builder()
                        .status("117")
                        .message("Insufficient agent balance")
                        .data(null)
                        .build();
            }
            // fetch from X switch params
            String irembocashaccount = xSwitchParameterRepository.findByParamName("IREMBOCASHACCOUNT").get().getParamValue();


            String paymentdetail2 = authenticateAgentResponse.getData().getNames() + " " + authenticateAgentResponse.getData().getAccountNumber();
            String paymentdetail1 = "BILL ID:  " + request.getBillCode();
            paymentdetail1 = paymentdetail1.length() > 34 ? paymentdetail1.substring(0, 34) : paymentdetail1;
            String paymentdetail3 = "IREMBO PAYMENT";

            String irembOFS = "0000AFUNDS.TRANSFER,IREMBO/I/PROCESS/0/0," + "" + MASKED_T24_USERNAME + "/" + MASKED_T24_PASSWORD + "/"
                    + branchId + ",," + "TRANSACTION.TYPE::=ACTY," + "DEBIT.ACCT.NO::=" + authenticateAgentResponse.getData().getAccountNumber()
                    + ",DEBIT.AMOUNT::=" + iremboamt + "," + "CREDIT.ACCT.NO::=" + irembocashaccount + ","
                    + "DEBIT.CURRENCY::=RWF,TCM.REF::=" + txnref + ","
                    + "PAYMENT.DETAILS:1:= " + paymentdetail1.trim() + ","
                    + "PAYMENT.DETAILS:2:=" + paymentdetail2.trim() + ","
                    + "PAYMENT.DETAILS:3:=" + paymentdetail3.trim();

            String tot24str = String.format("%04d", irembOFS.length()) + irembOFS;

            System.out.println("CHANNEL :" + channel);
            System.out.println("TXNREF :" + txnref);
            System.out.println("TID :" + tid);


            T24TXNQueue tot24 = new T24TXNQueue();
            tot24.setRequestleg(tot24str);
            tot24.setStarttime(System.currentTimeMillis());
            tot24.setTxnchannel(channel);
            tot24.setGatewayref(txnref);
            tot24.setPostedstatus("0");
            tot24.setTid(tid);
            tot24.setProcode("470000");
            tot24.setDebitacctno(authenticateAgentResponse.getData().getAccountNumber());
            tot24.setCreditacctno(irembocashaccount);

            final String t24Ip = xSwitchParameterRepository.findByParamName("T24_IP").get().getParamValue();
            final String t24Port = xSwitchParameterRepository.findByParamName("T24_PORT").get().getParamValue();

            t24Channel.processTransactionToT24(t24Ip, Integer.parseInt(t24Port), tot24);

            transactionService.updateT24TransactionDTO(tot24);

            String t24ResponseCode = tot24.getT24responsecode() == null ? "NA" : tot24.getT24responsecode();

            if (t24ResponseCode.equals("1")) {

                String charges = tot24.getTotalchargeamt();
                String ISOFormatCharge = "";

                if (null != charges) {
                    ISOFormatCharge = charges.replace("RWF", "");
                    log.info("Irembo payment transaction charges. " + ISOFormatCharge);
                }
                String isoamount = String.format("%012d", Integer.parseInt(ISOFormatCharge));
                String chargesAccountNumber = tot24.getChargesacctno() == null ? ""
                        : tot24.getChargesacctno();
                branchId = tot24.getCocode();
                String profitcentre = tot24.getProfitcentrecust() == null ? ""
                        : tot24.getProfitcentrecust();

                if (Integer.parseInt(ISOFormatCharge) > 0) {
                    if (!chargesAccountNumber.isEmpty()) {
                        sweepChargesToPL(tid, profitcentre, branchId, chargeCreditPLAccount,
                                chargesAccountNumber, ISOFormatCharge, transactionRefNo, tot24.getT24reference(),
                                "470000");
                    }
                }
                String payment_reference = tot24.getT24reference();
                String payment_datetime = new Date().toString();
                String payment_channel = "BPR_AGENCY";

                IremboPaymentResponseData data = IremboPaymentResponseData.builder()
                        .t24Reference(tot24.getT24reference())
                        .charges(isoamount)
                        .build();

                IremboPaymentNotifications ipn = new IremboPaymentNotifications();

                ipn.setBillid(request.getBillNo());
                ipn.setAmount(new BigDecimal(request.getAmount()));
                ipn.setPaymenttype(request.getTransactionType());
                ipn.setPayername(request.getCustomerName());
                ipn.setPayerphone(request.getMobileNo());
                ipn.setPaymentreference(payment_reference);
                ipn.setAccountnumber(request.getRRAAccountNo());
                ipn.setDescription(request.getDescription());
                ipn.setPaymentdatetime(payment_datetime);
                ipn.setPaymentstatus(request.getPaymentStatus());
                ipn.setPaymentchannel(payment_channel);
                ipn.setTrials(0);
                ipn.setResend("Y");

                // Insert in database
                queueIremboNotification(ipn);
                transactionService.saveCardLessTransactionToAllTransactionTable(tot24, "IREMBO");

                return IremboPaymentResponse
                        .builder()
                        .status("00")
                        .message("Irembo bill payment successful")
                        .data(data)
                        .build();

            } else {
                transactionService.saveCardLessTransactionToAllTransactionTable(tot24, "IREMBO");

                return IremboPaymentResponse.builder()
                        .status("098")
                        .message("TRANSACTION FAILED. SYSTEM FAILURE" + tot24.getT24failnarration())
                        .data(null)
                        .build();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            log.info("An exception occurred processing Irembo payment for transaction reference no [" + transactionRefNo + "]");
            return IremboPaymentResponse.builder()
                    .status("118")
                    .message("Transaction failed. System error: " + ex.getMessage())
                    .data(null)
                    .build();
        }
    }

    private void queueIremboNotification(IremboPaymentNotifications ipn) {
        iremboPaymentNotificationsRepository.save(ipn);
    }

    private String clientPaymentConfirmationValidate(String agentFloatAcc, String accountBranchId,
                                                     IremboBillPaymentRequest request, String referenceNo) {
        String charges = "0";
        try {
            String tid = "PC";
            String channel = "GATEWAY";

            String IremboCashAcc = xSwitchParameterRepository.findByParamName("IREMBOCASHACCOUNT").get().getParamValue();

            String iremboT24OFS = "0000AFUNDS.TRANSFER,IREMBO/I/VALIDATE/0/0,"
                    + MASKED_T24_USERNAME + "/" + MASKED_T24_PASSWORD + "/"
                    + accountBranchId + ",,TRANSACTION.TYPE::=ACTY,"
                    + "DEBIT.ACCT.NO::=" + agentFloatAcc + ",DEBIT.AMOUNT::=" + request.getAmount()
                    + ",CREDIT.ACCT.NO::=" + IremboCashAcc + ",DEBIT.CURRENCY::=RWF,"
                    + "TCM.REF::=" + referenceNo + ",DEBIT.THEIR.REF::='IREMBOPAYMENT',"
                    + "CREDIT.THEIR.REF::='IREMBOPAYMENT'";

            String preparedOFSMessage = String.format("%04d", iremboT24OFS.length()) + iremboT24OFS;
            T24TXNQueue tot24 = new T24TXNQueue();
            tot24.setRequestleg(preparedOFSMessage);
            tot24.setStarttime(System.currentTimeMillis());
            tot24.setTxnchannel(channel);
            tot24.setGatewayref(referenceNo);
            tot24.setPostedstatus("0");
            tot24.setTid(tid);
            tot24.setProcode("470000");

            log.info("Irembo Charge Transaction Validation: Sending to T24 [" + preparedOFSMessage + "]");

            final String t24Ip = xSwitchParameterRepository.findByParamName("T24_IP").get().getParamValue();
            final String t24Port = xSwitchParameterRepository.findByParamName("T24_PORT").get().getParamValue();
            t24Channel.processTransactionToT24(t24Ip, Integer.parseInt(t24Port), tot24);
            transactionService.updateT24TransactionDTO(tot24);

            String t24ResponseCode = tot24.getT24responsecode() == null ? "NA" : tot24.getT24responsecode();

            if (t24ResponseCode.equals("1")) {
                try {
                    charges = tot24.getTotalchargeamt();
                    charges = charges.replace("RWF", "");
                    log.info("Irembo Charge Transaction Validation Successful: Charges [" + charges + "] for transaction [" + referenceNo + "]");
                } catch (Exception e) {
                    log.info("Irembo Charge Transaction Validation Error: Fetching charges failed from response. " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return charges;
    }


    private void sweepChargesToPL(String tid, String profitCentre, String branchId, String chargeDebit,
                                  String chargeCreditPL, String chargeAmount, String gatewayRef, String T24reference, String processingCode) {

        String channel = "Gateway";

        String chargesRRN = RRNGenerator.getInstance("IC").getRRN();

        String iremboChargesOFS = "0000AFUNDS.TRANSFER," + "BPR.PL.SWEEP/I/PROCESS/1/0,"
                + MASKED_T24_USERNAME + "/" + MASKED_T24_PASSWORD + "/" + branchId
                + ",,TRANSACTION.TYPE::=ACTD,DEBIT.ACCT.NO::=" + chargeDebit
                + ",DEBIT.AMOUNT::=" + chargeAmount
                + ",CREDIT.ACCT.NO::=" + chargeCreditPL
                + ",DEBIT.CURRENCY::=RWF,ORDERING.CUST::='9999999',PROFIT.CENTRE.CUST::=" + profitCentre
                + ",TCM.REF::=" + chargesRRN
                + ",DEBIT.THEIR.REF::='" + T24reference
                + "',CREDIT.THEIR.REF::='" + T24reference + "'";

        String preparedOFSMsg = String.format("%04d", iremboChargesOFS.length()) + iremboChargesOFS;
        log.info("Irembo Charges transaction [" + chargesRRN + "] ready for processing. Original transaction reference [" + gatewayRef + "] OFS " + preparedOFSMsg);

        T24TXNQueue tot24 = new T24TXNQueue();
        tot24.setRequestleg(preparedOFSMsg);
        tot24.setStarttime(System.currentTimeMillis());
        tot24.setTxnchannel(channel);
        tot24.setGatewayref(chargesRRN);
        tot24.setPostedstatus("0");
        tot24.setProcode(processingCode);
        tot24.setTid(tid);

        final String t24Ip = xSwitchParameterRepository.findByParamName("T24_IP").get().getParamValue();
        final String t24Port = xSwitchParameterRepository.findByParamName("T24_PORT").get().getParamValue();

        t24Channel.processTransactionToT24(t24Ip, Integer.parseInt(t24Port), tot24);

        transactionService.updateT24TransactionDTO(tot24);

        String T24ResponseCode = tot24.getT24responsecode() == null ? "NA" : tot24.getT24responsecode();
        if (!T24ResponseCode.equals("1")) {
            log.info("Irembo Charges transaction [" + chargesRRN + "] processed successfully. Original transaction reference [" + gatewayRef + "]");
        } else {

            log.info("Irembo Charges transaction [" + chargesRRN + "] processing failed. Original transaction reference [" + gatewayRef + "] :" + tot24.getT24failnarration());
        }

    }
}
