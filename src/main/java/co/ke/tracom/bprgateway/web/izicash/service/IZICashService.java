package co.ke.tracom.bprgateway.web.izicash.service;

import co.ke.tracom.bprgateway.web.agenttransactions.dto.response.AuthenticateAgentResponse;
import co.ke.tracom.bprgateway.web.bankbranches.entity.BPRBranches;
import co.ke.tracom.bprgateway.web.bankbranches.service.BPRBranchService;
import co.ke.tracom.bprgateway.web.exceptions.custom.InvalidAgentCredentialsException;
import co.ke.tracom.bprgateway.web.izicash.data.request.IZICashRequest;
import co.ke.tracom.bprgateway.web.izicash.data.response.IZICashResponse;
import co.ke.tracom.bprgateway.web.izicash.data.response.IZICashResponseData;
import co.ke.tracom.bprgateway.web.izicash.entities.IZICashTxnLogs;
import co.ke.tracom.bprgateway.web.izicash.repository.IZICashTxnLogsRepository;
import co.ke.tracom.bprgateway.web.switchparameters.XSwitchParameterService;
import co.ke.tracom.bprgateway.web.t24communication.services.T24Channel;
import co.ke.tracom.bprgateway.web.transactionLimits.TransactionLimitManagerService;
import co.ke.tracom.bprgateway.web.transactions.entities.T24TXNQueue;
import co.ke.tracom.bprgateway.web.transactions.services.TransactionService;
import co.ke.tracom.bprgateway.web.util.services.BaseServiceProcessor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
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

    private final XSwitchParameterService xSwitchParameterService;
    private final IZICashTxnLogsRepository iziCashTxnLogsRepository;
    private final TransactionLimitManagerService limitManagerService;


    @SneakyThrows
    public IZICashResponse processWithdrawMoneyTnx(IZICashRequest request, String transactionRRN) {
        // Validate agent credentials
        AuthenticateAgentResponse authenticateAgentResponse =null;
        try{
            authenticateAgentResponse =  baseServiceProcessor.authenticateAgentUsernamePassword(request.getCredentials());
        }catch (InvalidAgentCredentialsException e)
        {
         transactionService.saveFailedUserPasswordTransactions("Failed Logins PC module transactions","Agent logins",request.getCredentials().getUsername(),
                 "AgentValidation","FAILED","ipAddress");
        }


        IZICashTxnLogs iziCashTxnLogs = new IZICashTxnLogs();
        T24TXNQueue toT24=new T24TXNQueue();
        /*
         * 01 IZI Request from external system 00 Success 06 Failure 43 Not
         * Valid Mobile Number 45 Unknown Error
         */


            IZICashResponse responses=new IZICashResponse();
        Long IZI_CASH_TRANSACTION_LIMIT_ID = 9L;
        TransactionLimitManagerService.TransactionLimit limitValid = limitManagerService.isLimitValid(IZI_CASH_TRANSACTION_LIMIT_ID, request.getAmount());
            if (!limitValid.isValid()) {
                if (authenticateAgentResponse != null) {
                    transactionService.saveCardLessTransactionToAllTransactionTable(toT24, "IZI CASH WITHDRAWAL", "1200",
                            request.getAmount(), "061",
                            authenticateAgentResponse.getData().getTid(), authenticateAgentResponse.getData().getMid(),"","");
                }
                responses.setStatus("061");
                responses.setMessage("Amount should be between"+ limitValid.getLower()+ " and " + limitValid.getUpper());
                return responses;
            }
try
{
            //TODO Clarify
            String transactionTerminalID = "PC";
            iziCashTxnLogs.setTid(transactionTerminalID);
            //TODO Clarify
            iziCashTxnLogs.setMid("PCMerchant");
            iziCashTxnLogs.setPosReference(transactionRRN);

            String IZICashSuspenseAccount = xSwitchParameterService.fetchXSwitchParamValue("IZICASHSUSPENSE");

            BPRBranches branch = branchService.fetchBranchAccountsByBranchCode(IZICashSuspenseAccount); // check what account should be used
            String accountBranchID = branch.getId();

            if (request.getMobileNo().length() < 5) {


                assert authenticateAgentResponse != null;
                transactionService.saveCardLessTransactionToAllTransactionTable(toT24, "IZI CASH WITHDRAWAL", "1200",
                        request.getAmount(), "091",
                        authenticateAgentResponse.getData().getTid(), authenticateAgentResponse.getData().getMid(),"","");
                return IZICashResponse.builder()
                        .status("091")
                        .message("Transaction processing failed. Invalid mobile no length.")
                        .data(null)
                        .build();
            }





            String mobileNo10 = request.getMobileNo();
            String mobileNoLast5Digits = mobileNo10.substring(mobileNo10.length() - 5);
//            String mobileNoLast5Digits = mobileNo10;
            iziCashTxnLogs.setRecipientNo(mobileNo10);

            String pinCode = request.getPinCode();
            Long transactionAmount = request.getAmount();

//            String customerPAN = request.getSecretCode();
            String secretCode = request.getSecretCode();
            iziCashTxnLogs.setSecretCode(secretCode);
            iziCashTxnLogs.setPassCode(new String(org.apache.commons.codec.binary.Base64.encodeBase64(request.getPinCode().getBytes())));
            iziCashTxnLogs.setAmount(String.valueOf(transactionAmount));

            String IZICashServiceID = xSwitchParameterService.fetchXSwitchParamValue("IZICASH_SERVICEID");
            String mobileWebServiceRequest =
                    IZICashServiceID
                            .concat("|")
                            .concat(mobileNoLast5Digits.trim())
                            .concat("|")
                            .concat(pinCode.trim())
                            .concat("|")
                            .concat(secretCode.substring(secretCode.length()-5).trim())
                            .concat("|")
                            .concat(String.valueOf(transactionAmount).trim())
                            .concat("|")
                            .concat(transactionTerminalID.trim())
                            .concat("|")
                            .concat(transactionRRN.trim())
                            .concat("|");

            String izicash_interfacecode = xSwitchParameterService.fetchXSwitchParamValue("IZICASH_INTERFACECODE");
            String soapBody = createIZICashRequestXML(mobileWebServiceRequest, izicash_interfacecode);
            iziCashTxnLogs.setXmlRequest(soapBody);

            String endpoint = xSwitchParameterService.fetchXSwitchParamValue("IZICASH_WSDL_URL");
            HashMap<String, String> responseMap = sendPostRequestToMobileBankingWebService(soapBody, transactionRRN, endpoint);

            String webServiceCallResponseCode = responseMap.get("responseCode");
            iziCashTxnLogs.setModeFinResponseCode(webServiceCallResponseCode);

            if (webServiceCallResponseCode.equalsIgnoreCase("200")) {

                String response = responseMap.get("responseMessage");

                log.info("IZI Cash Response: " + response);
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
                    log.info("Mode Fin Reference " + modeFinReference);

                    iziCashTxnLogs.setGatewayT24Reference(modeFinReference);
                    iziCashTxnLogs.setModeFinResponseCode(WSResultArray[0]);

                    String firstPaymentDetails = "AGENCY BANKING IZI WITHDRAWAL";
                    String secondPaymentDetails = request.getSecretCode() + "/" + pinCode + "/" + mobileNo10;
                    String thirdPaymentDetails = transactionTerminalID + "/" + transactionRRN + "/" + modeFinReference;

                    assert authenticateAgentResponse != null;
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
                    T24TXNQueue tot24 =
                            postToT24(
                                    transactionTerminalID,
                                    creditAgentRequest,
                                    transactionRRN,
                                    "PC",
                                    "530000");


                    if (tot24.getT24responsecode().equalsIgnoreCase("1")) {
                        System.out.printf(
                                "IZI Cash: [Success] Transaction %s was completed at T24 successfully. %n",
                                transactionRRN);

                        iziCashTxnLogs.setGatewayT24Reference(tot24.getT24reference());
                        iziCashTxnLogs.setGatewayT24PostingStatus(tot24.getT24responsecode());
                        //TODO Put success response
                        IZICashResponseData iziCashResponseData = IZICashResponseData.builder()
                                .rrn(transactionRRN)
                                .t24Reference(tot24.getT24reference())
                                .IZIReference(modeFinReference)
                                .build();

                        iziCashResponseData.setUsername(authenticateAgentResponse.getData().getUsername());
                        iziCashResponseData.setNames(authenticateAgentResponse.getData().getNames());
                        iziCashResponseData.setBusinessName(authenticateAgentResponse.getData().getBusinessName());
                        iziCashResponseData.setLocation(authenticateAgentResponse.getData().getLocation());
                        iziCashResponseData.setTid(authenticateAgentResponse.getData().getTid());
                        iziCashResponseData.setMid(authenticateAgentResponse.getData().getMid());

                        IZICashResponse iziCashResponse = IZICashResponse.builder()
                                .data(iziCashResponseData)
                                .status("00")
                                .message("IZICash Withdrawal transaction successful.")
                                .build();

                        log.info("IZICash withdrawal successful. " + iziCashResponse.toString());
                        transactionService.saveCardLessTransactionToAllTransactionTable(tot24, "IZI CASH WITHDRAWAL", "1200",
                                request.getAmount(), "000",
                                authenticateAgentResponse.getData().getTid(), authenticateAgentResponse.getData().getMid(),"","");
                        iziCashTxnLogsRepository.save(iziCashTxnLogs);
                        return iziCashResponse;
                    } else {

                        System.out.printf(
                                "IZI Cash: [Failed] Transaction %s failed processing at T24. Reconciliation to reverse IZI Cash transaction required!",
                                transactionRRN);
                        IZICashResponseData iziCashResponseData = IZICashResponseData.builder()
                                .rrn(transactionRRN)
                                .t24Reference(tot24.getT24reference())
                                .IZIReference(modeFinReference)
                                .build();
                        IZICashResponse iziCashResponse = IZICashResponse.builder()
                                .data(iziCashResponseData)
                                .status("098")
                                .message("IZICash Withdrawal transaction failed. Reversal Required. Error: "+ tot24.getT24failnarration())
                                .build();

                        transactionService.saveCardLessTransactionToAllTransactionTable(tot24, "IZI CASH WITHDRAWAL", "1200",
                                request.getAmount(), "098",
                                authenticateAgentResponse.getData().getTid(), authenticateAgentResponse.getData().getMid(),"","");
                        iziCashTxnLogs.setPassCode("*****");
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
                    iziCashTxnLogs.setPassCode("*****");
                    iziCashTxnLogsRepository.save(iziCashTxnLogs);


                    transactionService.saveCardLessTransactionToAllTransactionTable(toT24, "IZI CASH WITHDRAWAL", "1200",
                            request.getAmount(), "118",
                            Objects.requireNonNull(authenticateAgentResponse).getData().getTid(), authenticateAgentResponse.getData().getMid(),"","");
                    return IZICashResponse.builder()
                            .data(null)
                            .status("118")
                            .message("IZICash Withdrawal transaction failed. " + WSResultArray[1])
                            .build();
                }
            } else {
                iziCashTxnLogs.setGatewayT24PostingStatus("5");
                iziCashTxnLogs.setPassCode("*****");
                iziCashTxnLogsRepository.save(iziCashTxnLogs);
                transactionService.saveCardLessTransactionToAllTransactionTable(toT24, "IZI CASH WITHDRAWAL", "1200",
                        request.getAmount(), "092",
                        Objects.requireNonNull(authenticateAgentResponse).getData().getTid(), authenticateAgentResponse.getData().getMid(),"","");
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
            transactionService.saveCardLessTransactionToAllTransactionTable(toT24, "IZI CASH WITHDRAWAL", "1200",
                    request.getAmount(), "908",
                    Objects.requireNonNull(authenticateAgentResponse).getData().getTid(), authenticateAgentResponse.getData().getMid(),"","");
            return IZICashResponse.builder()
                    .data(null)
                    .status("908")
                    .message("IZICash Withdrawal transaction failed. Please try again.")
                    .build();
        }
    }

    public String createIZICashRequestXML(String payload, String IZICashInterfaceCode) {

        log.info("IZI Cash Payload: " + payload);

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

        NodeList childNodes = doc.getChildNodes();
        Node envelope = childNodes.item(0);
        Node body = envelope.getChildNodes().item(1);
        Node processWebServiceReq = body.getChildNodes().item(0);
        Node req = processWebServiceReq.getChildNodes().item(0);

//        NodeList statusMessageNL = doc.getElementsByTagName("ns:return");
//        return statusMessageNL.item(0).getTextContent().trim();

        return req.getTextContent();
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

        final String t24Ip = xSwitchParameterService.fetchXSwitchParamValue("T24_IP");
        final String t24Port = xSwitchParameterService.fetchXSwitchParamValue("T24_PORT");
        t24Channel.processTransactionToT24(t24Ip, Integer.parseInt(t24Port), t24Transaction);
        transactionService.updateT24TransactionDTO(t24Transaction);

        return t24Transaction;
    }
}
