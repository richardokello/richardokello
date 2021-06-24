package co.ke.tracom.bprgateway.web.izicash.service;

import co.ke.tracom.bprgateway.web.agenttransactions.dto.response.AuthenticateAgentResponse;
import co.ke.tracom.bprgateway.web.bankbranches.entity.BPRBranches;
import co.ke.tracom.bprgateway.web.bankbranches.service.BPRBranchService;
import co.ke.tracom.bprgateway.web.izicash.data.request.IZICashRequest;
import co.ke.tracom.bprgateway.web.izicash.data.response.IZICashResponse;
import co.ke.tracom.bprgateway.web.izicash.data.response.IZICashResponseData;
import co.ke.tracom.bprgateway.web.izicash.entities.IZICashTxnLogs;
import co.ke.tracom.bprgateway.web.izicash.repository.IZICashTxnLogsRepository;
import co.ke.tracom.bprgateway.web.sendmoney.data.response.SendMoneyResponse;
import co.ke.tracom.bprgateway.web.sendmoney.data.response.SendMoneyResponseData;
import co.ke.tracom.bprgateway.web.sendmoney.entity.MoneySend;
import co.ke.tracom.bprgateway.web.smsscheduled.entities.ScheduledSMS;
import co.ke.tracom.bprgateway.web.switchparameters.entities.XSwitchParameter;
import co.ke.tracom.bprgateway.web.switchparameters.repository.XSwitchParameterRepository;
import co.ke.tracom.bprgateway.web.t24communication.services.T24Channel;
import co.ke.tracom.bprgateway.web.transactions.entities.T24TXNQueue;
import co.ke.tracom.bprgateway.web.transactions.services.TransactionService;
import co.ke.tracom.bprgateway.web.util.services.BaseServiceProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

import static co.ke.tracom.bprgateway.web.t24communication.services.T24Channel.MASKED_T24_PASSWORD;
import static co.ke.tracom.bprgateway.web.t24communication.services.T24Channel.MASKED_T24_USERNAME;

@Slf4j
@RequiredArgsConstructor
@Service
public class IZICashService {
    private final BaseServiceProcessor baseServiceProcessor;
    private final BPRBranchService branchService;
    private final TransactionService transactionService;
    private final T24Channel t24Channel;

    private final XSwitchParameterRepository xSwitchParameterRepository;
    private final IZICashTxnLogsRepository iziCashTxnLogsRepository;

    @Value("${merchant.account.validation}")
    private String agentValidation;

    public IZICashResponse processWithdrawMoneyTnx(IZICashRequest request, String transactionRRN) {
        // Validate agent credentials
        Optional<AuthenticateAgentResponse> optionalAuthenticateAgentResponse = baseServiceProcessor.authenticateAgentUsernamePassword(request.getCredentials(), agentValidation);
        if (optionalAuthenticateAgentResponse.isEmpty()) {
            log.info(
                    "Agent Float Deposit:[Failed] Missing agent information %n");
            return IZICashResponse.builder()
                    .status("117")
                    .message("Missing agent information")
                    .data(null)
                    .build();

        } else if (optionalAuthenticateAgentResponse.get().getCode() != HttpStatus.OK.value()) {
            return IZICashResponse.builder()
                    .status(String.valueOf(
                            optionalAuthenticateAgentResponse.get().getCode())
                    )
                    .message(optionalAuthenticateAgentResponse.get().getMessage()).build();
        }
        AuthenticateAgentResponse authenticateAgentResponse = optionalAuthenticateAgentResponse.get();

        IZICashTxnLogs iziCashTxnLogs = new IZICashTxnLogs();
        /*
         * 01 IZI Request from external system 00 Success 06 Failure 43 Not
         * Valid Mobile Number 45 Unknown Error
         */
        try {

            //TODO Clarify
            String transactionTerminalID = "PC";
            iziCashTxnLogs.setTid(transactionTerminalID);
            //TODO Clarify
            iziCashTxnLogs.setMid("PCMerchant");
            iziCashTxnLogs.setPosReference(transactionRRN);

            Optional<XSwitchParameter> optionalXSwitchParameter = xSwitchParameterRepository.findByParamName("IZICASHSUSPENSE");
            // this must be set
            if (optionalXSwitchParameter.isEmpty()) {
                log.info("IZI Cash transaction [" + transactionRRN + "] processing failed. Missing XSwitch Parameter IZICASHSUSPENSE");
                return IZICashResponse.builder()
                        .status("098")
                        .message("Transaction processing failed. Please contact BPR Customer care")
                        .data(null)
                        .build();
            }

            String IZICashSuspenseAccount = optionalXSwitchParameter.get().getParamValue();

            BPRBranches branch = branchService.fetchBranchAccountsByBranchCode(IZICashSuspenseAccount); // check what account should be used
            if (null == branch.getCompanyName()) {
                log.info("IZI Cash: [Error] An error occurred processing transaction [%s] : Missing configuration for IZI Cash Suspense account");
                return IZICashResponse.builder()
                        .status("065")
                        .message("Transaction processing failed. Please contact BPR Customer care")
                        .data(null)
                        .build();
            }
            String accountBranchID = branch.getId();
            if (accountBranchID.isEmpty()) {
                log.info(
                        "IZI Cash: [Error] An error occurred processing transaction [%s] : Missing configuration for Branch ID");
                return IZICashResponse.builder()
                        .status("065")
                        .message("Transaction processing failed. Please contact BPR Customer care")
                        .data(null)
                        .build();
            }

            if (request.getMobileNo().length() < 5) {
                return IZICashResponse.builder()
                        .status("091")
                        .message("Transaction processing failed. Invalid mobile no length.")
                        .data(null)
                        .build();
            }

            String mobileNo10 = request.getMobileNo();
            String customerMobileNo = mobileNo10.substring(mobileNo10.length() - 5);
            iziCashTxnLogs.setRecipientNo(mobileNo10);


            String transactionPassCode = request.getPassCode();
            Long transactionAmount = request.getAmount();

            String customerPAN = request.getCustomerPAN();
            String secretCode = customerPAN.substring(customerPAN.length() - 5);
            iziCashTxnLogs.setSecretCode(secretCode);
            iziCashTxnLogs.setPassCode(new String(org.apache.commons.codec.binary.Base64.encodeBase64(secretCode.getBytes())));
            iziCashTxnLogs.setAmount(String.valueOf(transactionAmount));


            Optional<XSwitchParameter> optionalIZICashServiceIdConfig = xSwitchParameterRepository.findByParamName("IZICASH_SERVICEID");
            if (optionalIZICashServiceIdConfig.isEmpty()) {
                log.info("IZI Cash: [Error] An error occurred processing transaction [%s] : Missing configuration for IZICASH_SERVICEID");
                return IZICashResponse.builder()
                        .status("065")
                        .message("Transaction processing failed. Please contact BPR Customer care")
                        .data(null)
                        .build();
            }

            String IZICashServiceID = optionalIZICashServiceIdConfig.get().getParamValue();
            String mobileWebServiceRequest =
                    IZICashServiceID
                            .concat("|")
                            .concat(customerMobileNo)
                            .concat("|")
                            .concat(transactionPassCode)
                            .concat("|")
                            .concat(secretCode)
                            .concat("|")
                            .concat(String.valueOf(transactionAmount))
                            .concat("|")
                            .concat(transactionTerminalID)
                            .concat("|")
                            .concat(transactionRRN)
                            .concat("|");


            Optional<XSwitchParameter> optionalInterfaceCode = xSwitchParameterRepository.findByParamName("IZICASH_INTERFACECODE");
            if (optionalIZICashServiceIdConfig.isEmpty()) {
                log.info("IZI Cash: [Error] An error occurred processing transaction [%s] : Missing configuration for IZICASH_INTERFACECODE");
                return IZICashResponse.builder()
                        .status("065")
                        .message("Transaction processing failed. Please contact BPR Customer care")
                        .data(null)
                        .build();
            }

            String izicash_interfacecode = optionalInterfaceCode.get().getParamValue();


            String soapBody = createIZICashRequestXML(mobileWebServiceRequest, izicash_interfacecode);
            iziCashTxnLogs.setXmlRequest(soapBody);

            Optional<XSwitchParameter> optionalIZICashURL = xSwitchParameterRepository.findByParamName("IZICASH_WSDL_URL");
            if (optionalIZICashURL.isEmpty()) {
                log.info("IZI Cash: [Error] An error occurred processing transaction [%s] : Missing configuration for IZICASH_WSDL_URL");
                return IZICashResponse.builder()
                        .status("065")
                        .message("Transaction processing failed. Please contact BPR Customer care")
                        .data(null)
                        .build();
            }

            String endpoint = optionalIZICashURL.get().getParamValue();
            HashMap<String, String> responseMap = sendPostRequestToMobileBankingWebService(soapBody, transactionRRN, endpoint);

            String webServiceCallResponseCode = responseMap.get("responseCode");
            iziCashTxnLogs.setModeFinResponseCode(webServiceCallResponseCode);

            if (webServiceCallResponseCode.equalsIgnoreCase("200")) {

                String response = responseMap.get("responseMessage");
                String wsresponse = "";
                try {
                    wsresponse = getTag(response);
                    iziCashTxnLogs.setXmlResponse(response);
                } catch (Exception e) {
                    System.out.printf("IZI Cash: [Error] An error occurred processing transaction [%s] : Error message %s %n",
                            transactionRRN, e.getMessage());
                    e.printStackTrace();
                }

                String[] WSResultArray = wsresponse.split("\\|");
                String transactionDescription = WSResultArray[2];
                iziCashTxnLogs.setModeFinTransactionDescription(transactionDescription);

                if (WSResultArray[0].equalsIgnoreCase("00") && WSResultArray.length >= 4) {

                    String modeFinReference = WSResultArray[3];
                    iziCashTxnLogs.setGatewayT24Reference(modeFinReference);
                    iziCashTxnLogs.setModeFinResponseCode(WSResultArray[0]);

                    String firstPaymentDetails = "AGENCY BANKING IZI WITHDRAWAL";
                    String secondPaymentDetails = customerPAN + "/" + transactionPassCode + "/" + mobileNo10;
                    String thirdPaymentDetails = transactionTerminalID + "/" + transactionRRN + "/" + modeFinReference;

                    String creditAgentRequest =
                            prepareIZICashT24OFS(
                                    accountBranchID,
                                    authenticateAgentResponse.getData().getAccountNumber(),
                                    IZICashSuspenseAccount,
                                    transactionAmount,
                                    firstPaymentDetails,
                                    secondPaymentDetails,
                                    thirdPaymentDetails);

                    // sweep charges
                    T24TXNQueue T24Transaction =
                            postToT24(
                                    transactionTerminalID,
                                    creditAgentRequest,
                                    transactionRRN,
                                    "PC",
                                    "530000");


                    if (T24Transaction.getT24responsecode().equalsIgnoreCase("1")) {
                        System.out.printf(
                                "IZI Cash: [Success] Transaction %s was completed at T24 successfully. %n",
                                transactionRRN);

                        iziCashTxnLogs.setGatewayT24Reference(T24Transaction.getT24reference());
                        iziCashTxnLogs.setGatewayT24PostingStatus(T24Transaction.getT24responsecode());
                       //TODO Put success response
                        IZICashResponseData iziCashResponseData = IZICashResponseData.builder()
                                .rrn(transactionRRN)
                                .t24Reference(T24Transaction.getT24reference())
                                .IZIReference(modeFinReference)
                                .build();

                        IZICashResponse iziCashResponse = IZICashResponse.builder()
                                .data(iziCashResponseData)
                                .status("00")
                                .message("IZICash Withdrawal transaction successful.")
                                .build();

                        log.info("IZICash withdrawal successful. "+ iziCashResponse.toString());
                        transactionService.saveCardLessTransactionToAllTransactionTable(T24Transaction, "IZI CASH WITHDRAWAL");
                        iziCashTxnLogsRepository.save(iziCashTxnLogs);
                        return iziCashResponse;
                    } else {
                        System.out.printf(
                                "IZI Cash: [Failed] Transaction %s failed processing at T24. Reconciliation to reverse IZI Cash transaction required!",
                                transactionRRN);
                        IZICashResponseData iziCashResponseData = IZICashResponseData.builder()
                                .rrn(transactionRRN)
                                .t24Reference(T24Transaction.getT24reference())
                                .IZIReference(modeFinReference)
                                .build();
                        IZICashResponse iziCashResponse = IZICashResponse.builder()
                                .data(iziCashResponseData)
                                .status("098")
                                .message("IZICash Withdrawal transaction failed. Reversal Required.")
                                .build();

                        transactionService.saveCardLessTransactionToAllTransactionTable(T24Transaction, "IZI CASH WITHDRAWAL");
                        iziCashTxnLogsRepository.save(iziCashTxnLogs);
                        return iziCashResponse;
                    }
                } else {
                    // >43|Not Valid Mobile Number|040620173232|T200604162528414527
                    // 43|Not Valid Mobile Number|0020853608|T200503064110230059
                    // String errorMessage = WSResultArray[2];
                    iziCashTxnLogs.setModeFinResponseCode(WSResultArray[0]);
                    iziCashTxnLogs.setModeFinTransactionDescription(WSResultArray[1]);
                    iziCashTxnLogs.setModeFinT24Reference(WSResultArray[3]);
                    iziCashTxnLogs.setGatewayT24PostingStatus("5");
                    iziCashTxnLogsRepository.save(iziCashTxnLogs);

                    return IZICashResponse.builder()
                            .data(null)
                            .status("118")
                            .message("IZICash Withdrawal transaction failed. "+ WSResultArray[1])
                            .build();
                }
            } else {
                iziCashTxnLogs.setGatewayT24PostingStatus("5");
                iziCashTxnLogsRepository.save(iziCashTxnLogs);
               return IZICashResponse.builder()
                        .data(null)
                        .status("092")
                        .message("IZICash Withdrawal transaction failed.")
                        .build();
            }
        } catch (Exception e) {
            System.out.printf(
                    "IZI Cash: [Error] An error occurred processing transaction [%s] : Error message %s %n",
                    transactionRRN, e.getMessage());
            e.printStackTrace();
            return IZICashResponse.builder()
                    .data(null)
                    .status("908")
                    .message("IZICash Withdrawal transaction failed. Please try again.")
                    .build();
        }
    }

    public String createIZICashRequestXML(String payload, String IZICashInterfaceCode) {
        return "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:ever=\"http://everest\">\n"
                + "   <soap:Header/>\n"
                + "   <soap:Body>\n"
                + "      <ever:processWebServiceReq>\n"
                + "         <ever:interfaceCode>"
                + IZICashInterfaceCode
                + "</ever:interfaceCode>\n"
                + "         <ever:req>"
                + payload
                + "</ever:req>\n"
                + "      </ever:processWebServiceReq>\n"
                + "   </soap:Body>\n"
                + "</soap:Envelope>";
    }

    public HashMap<String, String> sendPostRequestToMobileBankingWebService(
            String iziCashSOAPRequest, String transactionRefNo, String endpoint) {

        System.out.printf(
                "IZI Cash: Beginning of sending transaction for Gateway Reference No %s to end point %s %n",
                transactionRefNo, endpoint);
        System.out.printf(
                "IZI Cash: Request body for transaction [%s] : [%s] %n",
                transactionRefNo, iziCashSOAPRequest);

        HashMap<String, String> responseData = new HashMap<>();
        responseData.put("responseCode", String.valueOf(org.apache.http.HttpStatus.SC_BAD_REQUEST));

        HttpURLConnection con = null;
        try {
            URL obj = new URL(endpoint);
            con = (HttpURLConnection) obj.openConnection();
            con.setRequestProperty("Content-Type", "Content-Type: application/soap");
            con.setRequestProperty("SOAPAction", "urn:processWebServiceReq"); // parametrize
            con.setRequestProperty("Operation", "processWebServiceReq"); // parametrize
            con.setRequestProperty("Style", "Document");
            con.setRequestProperty("Type", "Request-Response");
            con.setRequestMethod("POST");
            con.setDoInput(true);

            final int CONNECTION_TIMEOUT_CONFIG = 5;
            con.setConnectTimeout(CONNECTION_TIMEOUT_CONFIG * 5000);
            final int READ_TIMEOUT_CONFIG = 30;
            con.setReadTimeout(READ_TIMEOUT_CONFIG * 30000);
            con.setDefaultUseCaches(false);
            con.setDoOutput(true);

            DataOutputStream dataOutputStream = new DataOutputStream(con.getOutputStream());
            dataOutputStream.writeBytes(iziCashSOAPRequest);
            dataOutputStream.flush();
            int responseCode = con.getResponseCode();
            System.out.printf(
                    "IZI Cash: Response Code %s for transaction reference [%s] %n",
                    responseCode, transactionRefNo);

            responseData.put("responseCode", String.valueOf(responseCode));
            InputStream errorStream = con.getErrorStream();
            InputStream responseStream = (errorStream != null ? errorStream : con.getInputStream());

            BufferedReader in = null;
            if (responseCode == HttpURLConnection.HTTP_OK) {
                in = new BufferedReader(new InputStreamReader(responseStream));
                responseData.put("responseMessage", streamToString(in, transactionRefNo));
            } else {
                if (responseStream != null) {
                    in = new BufferedReader(new InputStreamReader(responseStream));
                    String response = streamToString(in, transactionRefNo);
                    responseData.put("responseMessage", response);
                    System.out.printf(
                            "IZI Cash: Response received for transaction [%s] : [%s]",
                            transactionRefNo, response);
                } else {
                    System.out.printf(
                            "IZI Cash: [Error] Response stream is null for transaction [%s] %n",
                            transactionRefNo);
                }
            }
            if (in != null) {
                in.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            log.info("IZI Cash: [Error] An exception occurred processing transaction [" + transactionRefNo + "] : Error : " + ex.getMessage());
        } finally {
            if (con != null) {
                con.disconnect();
                log.info("IZI Cash: Closing web service connection for transaction [" + transactionRefNo + "]");
            }
        }
        return responseData;
    }

    public String streamToString(BufferedReader in, String transactionRefNo) {
        StringBuilder responseBuffer = new StringBuilder();
        String inputLine;
        try {
            while ((inputLine = in.readLine()) != null) {
                responseBuffer.append(inputLine);
            }
        } catch (IOException e) {
            log.info("IZI Cash: [Error] Stream to String conversion for transaction [" + transactionRefNo + "] : Error message " + e.getMessage());
            e.printStackTrace();
        }
        return responseBuffer.toString();
    }

    public String getTag(String response)
            throws SAXException, IOException, ParserConfigurationException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(new InputSource(new StringReader(response)));
        doc.getDocumentElement().normalize();
        NodeList statusMessageNL = doc.getElementsByTagName("ns:return");
        return statusMessageNL.item(0).getTextContent().trim();
    }

    public String prepareIZICashT24OFS(String accountBranchID, String agentFloatAccount, String IZICashSuspenseAccount,
                                       long transactionAmount, String firstPaymentDetails, String secondPaymentDetails,
                                       String thirdPaymentDetails) {
        String IZICashWithdrawalOFS =
                "0000AFUNDS.TRANSFER,"
                        + "BPR.AGB.IZIWDL/I/PROCESS,"
                        + ""
                        + MASKED_T24_USERNAME
                        + "/"
                        + MASKED_T24_PASSWORD
                        + "/"
                        + accountBranchID
                        + ",,"
                        + "DEBIT.ACCT.NO::="
                        + IZICashSuspenseAccount
                        + ","
                        + "CREDIT.ACCT.NO::="
                        + agentFloatAccount
                        + ","
                        + "DEBIT.CURRENCY::=RWF,"
                        + "DEBIT.AMOUNT::="
                        + transactionAmount
                        + ","
                        + "PAYMENT.DETAILS:1:1="
                        + firstPaymentDetails.trim()
                        + ","
                        + "PAYMENT.DETAILS:2:="
                        + secondPaymentDetails.trim()
                        + ","
                        + "PAYMENT.DETAILS:3:="
                        + thirdPaymentDetails.trim();

        return String.format("%04d", IZICashWithdrawalOFS.length()) + IZICashWithdrawalOFS;
    }

    public T24TXNQueue postToT24(
            String tid, String t24request, String transactionReferenceNo, String channel, String field003) {

        T24TXNQueue t24Transaction = new T24TXNQueue();
        t24Transaction.setTid(tid);
        t24Transaction.setRequestleg(t24request);
        t24Transaction.setStarttime(System.currentTimeMillis());
        t24Transaction.setTxnchannel(channel);
        t24Transaction.setGatewayref(transactionReferenceNo);
        t24Transaction.setPostedstatus("0");
        t24Transaction.setProcode(field003);

        final String t24Ip = xSwitchParameterRepository.findByParamName("T24_IP").get().getParamValue();
        final String t24Port = xSwitchParameterRepository.findByParamName("T24_PORT").get().getParamValue();

        t24Channel.processTransactionToT24(t24Ip, Integer.parseInt(t24Port), t24Transaction);
        transactionService.updateT24TransactionDTO(t24Transaction);

        return t24Transaction;
    }
}
