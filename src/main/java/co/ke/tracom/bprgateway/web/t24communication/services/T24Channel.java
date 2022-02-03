package co.ke.tracom.bprgateway.web.t24communication.services;

import co.ke.tracom.bprgateway.web.academicbridge.data.studentdetails.GetStudentDetailsResponse;
import co.ke.tracom.bprgateway.web.switchparameters.entities.XSwitchParameter;
import co.ke.tracom.bprgateway.web.switchparameters.repository.XSwitchParameterRepository;
import co.ke.tracom.bprgateway.web.transactions.entities.T24TXNQueue;
import co.ke.tracom.bprgateway.web.util.services.UtilityService;
import co.ke.tracom.bprgateway.web.wasac.data.customerprofile.CustomerProfileResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.telnet.TelnetClient;
import org.springframework.stereotype.Service;
import co.ke.tracom.bprgateway.core.tracomchannels.tcp.T24TCPClient;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class  T24Channel {

    public static final String MASKED_T24_USERNAME = "########U";
    public static final String MASKED_T24_PASSWORD = "########A";
    public static final String T24_IP = "T24_IP";
    public static final String T24_PORT = "T24_PORT";
    public static final SimpleDateFormat GATEWAY_SERVER_DATE_FORMAT =
            new SimpleDateFormat("dd/MM/yy HH:mm:ss SSS Z");

    private final UtilityService utilityService;
    private final XSwitchParameterRepository xSwitchParameterRepository;

    private static void parseT24EuclElecInquiry(T24TXNQueue t24TXNQueue, String gatewayref) {
        String t24Response = t24TXNQueue.getResponseleg();
        System.out.println(t24Response.isEmpty());//kelvin to do
        t24TXNQueue.setPostedstatus("1");
        String[] output = t24Response.split(",");
        System.out.println("Output length is : " +output.length);//kelvin to do
        System.out.println("Output message is" +
                " : " +output[0]);//kelvin to do

        if (output.length > 0) {
            if (output[0].equals("0112")) {
                String[] custData = output[2].split("\t");
                String customerName = custData[0].replace("\"", "").trim();
                String meterLocation = custData[1].replace("\"", "").trim();
                t24TXNQueue.setCustomerName(customerName);
                t24TXNQueue.setMeterLocation(meterLocation);
            } else {
                System.out.println("In else of parse t24 elec inquiry");
                System.out.println("Output length is : " +output.length);//kelvin to do
                System.out.println("Error=>" + output[3]);
                t24TXNQueue.setT24failnarration(output[3].replace("\"", ""));
            }
        }

        t24TXNQueue.setAttempts(t24TXNQueue.getAttempts() < 1 ? 1 : t24TXNQueue.getAttempts() + 1);

        System.out.println(
                "Exiting t24 parse for Rec id  : "
                        + t24TXNQueue.getT24TXNQueueid()
                        + ", "
                        + "Gateway ref "
                        + gatewayref
                        + " at "
                        + System.currentTimeMillis());
    }

    private static void parseT24ResponseRefactored(T24TXNQueue t24TXNQueue, String transactionRRN) {
        t24TXNQueue.setPostedstatus("1");

        String T24RawResponse = t24TXNQueue.getResponseleg();
        System.out.printf(
                "T24 Raw response for transaction %s is [%s] %n", transactionRRN, T24RawResponse);

        String T24MessageBody = T24RawResponse.substring(4);
        t24TXNQueue.setResponseleg(T24MessageBody);

        String[] T24MessageBodyArray = T24MessageBody.split(",");
        String[] T24ResultArray = T24MessageBodyArray[0].split("/");

        String T24Reference = T24ResultArray[0];
        t24TXNQueue.setT24reference(T24Reference.length() > 20 ? "" : T24Reference);

        String T24ResponseCode = "";
        HashMap<String, String> t24data = new HashMap<>();
        switch (T24ResultArray.length) {
            case 3:
                T24ResponseCode = T24ResultArray[2].substring(0, 1);
                t24TXNQueue.setT24responsecode(T24ResponseCode);

                int ii = 0;
                for (String column : T24MessageBodyArray) {
                    if (ii > 0) {
                        // would have been easier to split using : and read key/ value from 0 and 3 but
                        // the t24 data is very unpredictable and small changes in the format could potentially
                        // cause parsing challenges
                        String replaceAll = column.replaceAll(":1:1=", "#");
                        replaceAll = replaceAll.replaceAll(":2:1=", "#");
                        replaceAll = replaceAll.replaceAll(":3:1=", "#");
                        replaceAll =
                                replaceAll.replaceAll(":4:1=", "#"); // dont expect this to happen but hey ...
                        if (replaceAll.contains("#")) {
                            String[] splitdata = replaceAll.split("#");
                            String key = splitdata[0].replaceAll("\\.", "");
                            t24data.put(key, splitdata[1]);

                        } else {
                            t24data.put(column, column);
                        }
                    }
                    ii++;
                }

                if (t24data.containsKey("CHARGESACCTNO")) {
                    System.out.println("CHARGES.ACCT.NO " + t24data.get("CHARGESACCTNO"));
                    t24TXNQueue.setChargesacctno(t24data.get("CHARGESACCTNO"));
                }

                if (t24data.containsKey("TOTALCHARGEAMT")) {
                    System.out.println("key TOTALCHARGEAMT  : " + t24data.containsKey("TOTALCHARGEAMT"));
                    t24TXNQueue.setTotalchargeamt(t24data.get("TOTALCHARGEAMT"));
                }

                if (t24data.containsKey("BPRBUYERNAME")) {
                    t24TXNQueue.setCustomerName(t24data.get("BPRBUYERNAME"));
                }

                if (t24data.containsKey("METERNO")) {
                    t24TXNQueue.setMeterno(t24data.get("METERNO"));
                }
                if (t24data.containsKey("PYMNTDETAILS6")) {
                    String[] paymentDetailsArray = t24data.get("PYMNTDETAILS6").split("-");
                    String[] token = paymentDetailsArray[0].split(":");
                    String[] units = paymentDetailsArray[1].split(":");

                    t24TXNQueue.setTokenNo(token[1]); // .replaceAll(".{4}(?!$)", "$0-")
                    t24TXNQueue.setUnitsKw(units[1]);
                }
                t24TXNQueue.setPostedstatus("1");
                break;

            case 4:
                // first level split based on / ... to get the response code
                String[] t24levelonesplitfs = T24MessageBody.split("/");
                int xi = 0;
                StringBuilder errorDescription = new StringBuilder();
                for (String l1split : t24levelonesplitfs) {
                    if (xi > 2) {
                        errorDescription.append(l1split);
                    }
                    xi++;
                }
                T24ResponseCode = t24levelonesplitfs[2];
                System.out.printf(
                        "T24 Response code for transaction %s is [%s] %n",
                        t24TXNQueue.getGatewayref(), T24ResponseCode);
                System.out.printf(
                        "T24 Response error message %s for transaction %s %n",
                        errorDescription.toString(), t24TXNQueue.getGatewayref());
                t24TXNQueue.setT24responsecode(T24ResponseCode);
                t24TXNQueue.setT24reference(T24ResultArray[0]);
                t24TXNQueue.setPostedstatus("1");
                t24TXNQueue.setT24failnarration(errorDescription.toString());
                t24data.put("failnarrations", errorDescription.toString());

                break;
            default:
                /**
                 * T24 responded with the below responses whenever an error occurred on its end.
                 * 0016OFSERROR_PROCESS and OFSERROR_TIMEOUT When this occured the gateway responded with a
                 * success. Below implementation is to gracefully handle any "OFSERROR_" type of response
                 */
                // Note: 0016OFSERROR_PROCESS and OFSERROR_TIMEOUT occurred and caused
                if (T24RawResponse.contains("OFSERROR")) {
                    // Handle timeouts
                    t24TXNQueue.setT24responsecode("3");
                    t24TXNQueue.setT24failnarration(T24ResultArray[0]);
                    t24data.put("failnarrations", T24ResultArray[0]);
                } else {
                    /**
                     * T24 can send an empty response i.e. header length 0000 but body is empty The existing
                     * implementation would allow the gateway to retry the request. This is a risk as it could
                     * lead to queue growth for tnx that can never be processed
                     */
                    t24TXNQueue.setT24responsecode("5");
                    t24TXNQueue.setT24failnarration("T24 Timeout");
                    t24data.put("failnarrations", "T24 Timeout");
                }
                break;
        }

        t24TXNQueue.setCradvicereqdyn(t24data.get("CRADVICEREQDYN"));
        t24TXNQueue.setChargecode(t24data.get("CHARGECODE"));
        t24TXNQueue.setDebitamount(t24data.get("DEBITAMOUNT"));
        t24TXNQueue.setCocode(t24data.get("COCODE"));
        t24TXNQueue.setCreditcustomer(t24data.get("CREDITCUSTOMER"));

        t24TXNQueue.setReturntodept(t24data.get("RETURNTODEPT"));
        t24TXNQueue.setCreditvaluedate(t24data.get("CREDITVALUEDATE"));
        t24TXNQueue.setCreditcompcode(t24data.get("CREDITCOMPCODE"));
        t24TXNQueue.setRoundtype(t24data.get("ROUNDTYPE"));
        t24TXNQueue.setDebitcurrency(t24data.get("DEBITCURRENCY"));

        t24TXNQueue.setInputter(t24data.get("INPUTTER"));
        t24TXNQueue.setPositiontype(t24data.get("POSITIONTYPE"));
        t24TXNQueue.setTotsndchgcrccy(t24data.get("TOTSNDCHGCRCCY"));
        t24TXNQueue.setTotreccommlcl(t24data.get("TOTRECCOMMLCL"));
        t24TXNQueue.setLocamtcredited(t24data.get("LOCAMTCREDITED"));

        t24TXNQueue.setRatefixing(t24data.get("RATEFIXING"));
        t24TXNQueue.setCommissioncode(t24data.get("COMMISSIONCODE"));
        t24TXNQueue.setAmountdebited(t24data.get("AMOUNTDEBITED"));
        t24TXNQueue.setTotrecchg(t24data.get("TOTRECCHG"));
        // t24TXNQueue.setT24responsecode(t24data.get("T24ResponseCode"));

        t24TXNQueue.setCurrencymktcr(t24data.get("CURRENCYMKTCR"));
        t24TXNQueue.setChargecomdisplay(t24data.get("CHARGECOMDISPLAY"));
        t24TXNQueue.setTransactiontype(t24data.get("TRANSACTIONTYPE"));
        t24TXNQueue.setDebitcompcode(t24data.get("DEBITCOMPCODE"));
        t24TXNQueue.setChargedcustomer(t24data.get("CHARGEDCUSTOMER"));

        t24TXNQueue.setProcessingdate(t24data.get("PROCESSINGDATE"));
        t24TXNQueue.setCreditacctno(t24data.get("CREDITACCTNO"));
        t24TXNQueue.setTotrecchglcl(t24data.get("TOTRECCHGLCL"));
        t24TXNQueue.setLocamtdebited(t24data.get("LOCAMTDEBITED"));
        t24TXNQueue.setDatetime(t24data.get("DATETIME"));

        t24TXNQueue.setTotreccomm(t24data.get("TOTRECCOMM"));
        t24TXNQueue.setProfitcentrecust(t24data.get("PROFITCENTRECUST"));
        t24TXNQueue.setStmtnos(t24data.get("STMTNOS"));
        t24TXNQueue.setDebitvaluedate(t24data.get("DEBITVALUEDATE"));
        t24TXNQueue.setRecordstatus(t24data.get("RECORDSTATUS"));

        t24TXNQueue.setDradvicereqdyn(t24data.get("DRADVICEREQDYN"));
        t24TXNQueue.setCurrencymktcr(t24data.get("CURRENCYMKTDR"));
        t24TXNQueue.setAmountcredited(t24data.get("AMOUNTCREDITED"));
        t24TXNQueue.setFedfunds(t24data.get("FEDFUNDS"));
        t24TXNQueue.setCreditcurrency(t24data.get("CREDIT.CURRENCY"));

        t24TXNQueue.setDebitacctno(t24data.get("DEBITACCTNO"));
        t24TXNQueue.setTotrecchgcrccy(t24data.get("TOTRECCHGCRCCY"));
        t24TXNQueue.setDeptcode(t24data.get("DEPTCODE"));
        t24TXNQueue.setDebitcustomer(t24data.get("DEBITCUSTOMER"));
        t24TXNQueue.setCustgrouplevel(t24data.get("CUSTGROUPLEVEL"));
        t24TXNQueue.setCurrno(t24data.get("CURRNO"));
        t24TXNQueue.setT24failnarration(t24data.get("failnarrations"));

        t24TXNQueue.setPostedstatus("1");
    }

    private static void parseT24EnquiryResponse(T24TXNQueue t24Request, String transactionRRN) {
        System.out.printf("Begin processing of enquiry response for transaction %s %n", transactionRRN);

        String t24Response = t24Request.getResponseleg();
        System.out.printf(
                "T24 response for transaction reference no %s : [%s] %n", transactionRRN, t24Response);

        String[] responseMessageArray = t24Response.substring(4).split(",");

        if (responseMessageArray.length > 1) {


            String[] enquiryResponseArray = responseMessageArray[2].split("\\|");

            for (int i = 0; i < enquiryResponseArray.length - 1; i++) {
                if (i == 0) {
                    // Value in array is in format: "CUSTDET"	"401428327210176"	"#RWF
                    String[] currencyArray = enquiryResponseArray[0].split("#");
                    String currency =
                            currencyArray.length > 1 ? currencyArray[1].toUpperCase() : enquiryResponseArray[0];
                    t24Request.setCurrency(currency);
                    System.out.printf("Transaction %s account currency %s %n", transactionRRN, currency);

                } else if (i == 1) {
                    t24Request.setAccounttitle(enquiryResponseArray[1]);
                    t24Request.setAccountname(enquiryResponseArray[1]);
                    System.out.printf(
                            "Transaction %s account title %s %n", transactionRRN, t24Request.getAccounttitle());

                } else if (i == 2) {
                    t24Request.setCustid(enquiryResponseArray[2]);
                    System.out.printf(
                            "Transaction %s account customer Id %s %n", transactionRRN, t24Request.getCustid());

                } else if (i == 3) {
                    t24Request.setWorkingbal(enquiryResponseArray[3]);
                    t24Request.setBaladvise(enquiryResponseArray[3]);
                    System.out.printf(
                            "Transaction %s account working balance %s %n", transactionRRN, t24Request.getWorkingbal());

                } else if (i == 4) {
                    String inactivemarker = enquiryResponseArray[4];
                    System.out.printf(
                            "Transaction %s account inactive maker setting %s %n", transactionRRN, inactivemarker);

                } else if (i == 5) {
                    String postingRestrict = enquiryResponseArray[5];
                    System.out.printf(
                            "Transaction %s account posting restriction setting %s %n",
                            transactionRRN, postingRestrict);

                } else if (i == 6) {
                    t24Request.setLegalid(enquiryResponseArray[6]);
                    System.out.printf(
                            "Transaction %s customer legal ID %s %n", transactionRRN, t24Request.getLegalid());

                } else if (i == 7) {
                    t24Request.setTowncountry(enquiryResponseArray[7]);
                    System.out.printf(
                            "Transaction %s customer town/country %s %n", transactionRRN, t24Request.getTowncountry());

                } else if (i == 8) {
                    t24Request.setPhone(enquiryResponseArray[8]);
                    System.out.printf(
                            "Transaction %s customer phone number %s %n", transactionRRN, t24Request.getPhone());

                } else if (i == 9) {
                    t24Request.setDateofbirth(enquiryResponseArray[9]);
                    System.out.printf(
                            "Transaction %s customer date of birth %s %n", transactionRRN, t24Request.getDateofbirth());

                } else if (i == 10) {
                    t24Request.setCustemail(enquiryResponseArray[10]);
                    System.out.printf(
                            "Transaction %s customer email %s %n", transactionRRN, t24Request.getCustemail());

                } else if (i == 11) {
                    t24Request.setBusinessloc(enquiryResponseArray[11]);
                    System.out.printf(
                            "Transaction %s customer business location %s %n",
                            transactionRRN, t24Request.getBusinessloc());

                } else if (i == 12) {
                    t24Request.setSector(enquiryResponseArray[12]);
                    System.out.printf(
                            "Transaction %s customer business sector %s %n", transactionRRN, t24Request.getSector());
                }
            }
        } else {
            t24Request.setResponseleg(responseMessageArray[0]);
        }

        t24Request.setPostedstatus("1");
        t24Request.setAttempts(t24Request.getAttempts() < 1 ? 1 : t24Request.getAttempts() + 1);

        System.out.printf(
                "T24 Response parsing successful for transaction reference no %s %n", transactionRRN);
    }

   /* public void processTransactionToT24(String t24Ip, int t24Port, T24TXNQueue transactionPendingT24Processing) {
       log.info("Processing T24 transaction");
       CustomerProfileResponse student = new CustomerProfileResponse();
        try {
            if (transactionPendingT24Processing != null) {
                log.info(
                        "Transaction {} fetched from database queue ready for processing at T24",
                        transactionPendingT24Processing.getGatewayref());

                transactionPendingT24Processing.setAttempts(
                        Math.max(transactionPendingT24Processing.getAttempts(), 1));

                transactionPendingT24Processing.setStarttime(System.currentTimeMillis());
                transactionPendingT24Processing.setPostedstatus("6");
                boolean xx = t24TransactionPosting(transactionPendingT24Processing, t24Ip, t24Port);

                log.info("After transaction posting >>>> {}", student.getMessage());
            }
        } catch (Exception e) {
           log.info(
                    "[Error] Unable to fetch transaction from database queue. Error message: {}",
                    e.getMessage());
            transactionPendingT24Processing.setPostedstatus("3");
            e.printStackTrace();
        }
    }
*/
   /* public boolean t24TransactionPosting(T24TXNQueue transactionPendingProcessing, String t24ip, int t24port) {
        String t24RequestOFS = transactionPendingProcessing.getRequestleg();
        String transactionRRN = transactionPendingProcessing.getGatewayref();

        log.info(
                "Processing initialization for Transaction RRN [{}] at [{}] OFS Request: [{}]",
                transactionRRN, GATEWAY_SERVER_DATE_FORMAT.format(new Date()), t24RequestOFS);
        System.out.println("Pcode is : "+transactionPendingProcessing.getProcode());

        TelnetClient telnetClient = new TelnetClient();
        try {
            log.info(
                    "Connection established to T24 for Transaction RRN [{}] at [{}] ",
                    transactionRRN, GATEWAY_SERVER_DATE_FORMAT.format(new Date()));

//            String[] OFSSplit = t24RequestOFS.split(",");
//            String OFSContainingMaskedCredentials = OFSSplit[2];

            Optional<XSwitchParameter> optionalT24UserCredentials = xSwitchParameterRepository.findByParamName("T24USER");
            Optional<XSwitchParameter> optionalT24Pass = xSwitchParameterRepository.findByParamName("T24PASS");

            if (optionalT24Pass.isEmpty() || optionalT24UserCredentials.isEmpty()) {
                log.info("****************************** Missing T24 Pass or T24 User on the database. ******************************");
            }

            String t24usn = utilityService.decryptSensitiveData(optionalT24UserCredentials.get().getParamValue());
            String t24pwd = utilityService.decryptSensitiveData(optionalT24Pass.get().getParamValue());

//            String[] T24ChannelCredentials = OFSContainingMaskedCredentials.split("/");
//            String OFSWithPlainTextCredentials =
//                    t24RequestOFS.replaceAll(T24ChannelCredentials[0], t24usn);
//            OFSWithPlainTextCredentials =
//                    OFSWithPlainTextCredentials.replaceAll(T24ChannelCredentials[1], t24pwd);

            t24RequestOFS = t24RequestOFS.replace(MASKED_T24_USERNAME, t24usn.trim());
            String OFSWithPlainTextCredentials = t24RequestOFS.replace(MASKED_T24_PASSWORD, t24pwd.trim());


            String minus4 = OFSWithPlainTextCredentials.substring(4);
            String formattedOFSMessageString = String.format("%04d", minus4.length()) + minus4;

            log.info("Formatted OFS Message String: {}", formattedOFSMessageString);

            telnetClient.connect(t24ip, t24port);
            if(!telnetClient.isConnected()){
                //log that connection has failed
                //retun
                //to do waweru
            }
            log.info("Connection established >>>> {}", telnetClient.isConnected());
            boolean sent = send(telnetClient, formattedOFSMessageString.trim(), transactionRRN);
            log.info("Transaction sent to t24");
            String T24ResponseOFS = receive(telnetClient, transactionPendingProcessing);
           // String T24ResponseOFS = receive2(telnetClient);
           // String T24ResponseOFS = "yolo";
            System.out.println("####################################################################");
            System.out.println("Response from t24 is :> "+T24ResponseOFS);
            System.out.println("####################################################################");

            log.info(
                    "T24 response received for Transaction RRN [{}] at [{}] :: Response [{}]",
                    transactionRRN, GATEWAY_SERVER_DATE_FORMAT.format(new Date()), T24ResponseOFS);

            transactionPendingProcessing.setResponseleg(T24ResponseOFS);
            transactionPendingProcessing.setEndtime(System.currentTimeMillis());

            if (T24ResponseOFS.length() < 5) {
                log.info(
                        "[Error] T24 failed processing Transaction RRN [{}] at [{}] :: Response [UNEXPECTED RESPONSE FROM REMOTE SYSTEM]",
                        transactionRRN, GATEWAY_SERVER_DATE_FORMAT.format(new Date()));
                transactionPendingProcessing.setPostedstatus("4");
                transactionPendingProcessing.setT24failnarration("UNEXPECTED RESPONSE FROM REMOTE SYSTEM");
            } else {
                System.out.println("Pcode in else is : "+transactionPendingProcessing.getProcode());
                try {
                    switch (transactionPendingProcessing.getProcode() == null ? "" : transactionPendingProcessing.getProcode()) {
                        case "460001":
                            if (transactionPendingProcessing.getTxnmti().equals("1100")) {
                                parseT24EuclElecInquiry(transactionPendingProcessing, transactionRRN);
                            } if(transactionPendingProcessing.getTxnmti().equals("1200")) {
                            parseT24AcademicBidgeInquiry(transactionPendingProcessing, transactionRRN);
                            }
                            else {
                                parseT24ResponseRefactored(transactionPendingProcessing, transactionRRN);
                            }
                            break;
                        case "510000":
                        case "430000":
                        case "500000":
                            parseT24EnquiryResponse(transactionPendingProcessing, transactionRRN);
                            break;
                        case "500001":
                            parseT24SMSResponse(transactionPendingProcessing, transactionRRN);
                            break;
                        default:
                            parseT24ResponseRefactored(transactionPendingProcessing, transactionRRN);
                            break;
                    }
                } catch (Exception e) {
                    log.info("Error Parsing T24 Response : for  {}", transactionRRN );
                    transactionPendingProcessing.setPostedstatus("5");
                    transactionPendingProcessing.setT24failnarration(e.getMessage());
                    e.printStackTrace();
                }
            }

        } catch (IOException ex) {
            transactionPendingProcessing.setPostedstatus("5");
            transactionPendingProcessing.setT24failnarration(ex.getMessage());
            System.out.printf(
                    "[Error] An exception occurred while processing Transaction RRN [%s] at [%s] :: Response [%s]\n",
                    transactionRRN,
                    GATEWAY_SERVER_DATE_FORMAT.format(new Date()),
                    ex.getMessage());
        } finally {
            if (telnetClient.isConnected()) {
                try {
                    System.out.printf(
                            "T24 channel closed for transaction RRN [%s] at [%s] ",
                            transactionRRN, GATEWAY_SERVER_DATE_FORMAT.format(new Date()));
                    telnetClient.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }*/

    /**
     * Unpack OFS Message from T24
     * Structure Failure: 0081,Y.STATUS::STATUS/Y.REASON::REJECT.REASON,"FAILED" "The access token has expired"
     * Structure Success: 0081,Y.STATUS::STATUS/Y.REASON::REJECT.REASON,"Success" "Message sent successful"
     *
     * @param t24TXNQueue
     * @param transactionRRN
     */
    private void parseT24SMSResponse(T24TXNQueue t24TXNQueue, String transactionRRN) {
        String T24RawResponse = t24TXNQueue.getResponseleg();
        System.out.printf("T24 Raw response for SMS transaction %s is [%s] %n", transactionRRN, T24RawResponse);
        t24TXNQueue.setT24reference("");
        t24TXNQueue.setPostedstatus("1");
        String[] T24MessageBodyArray = T24RawResponse.split(",");
        if (T24MessageBodyArray.length == 3) {
            String[] split = T24MessageBodyArray[2].split("\"");

            if (split[1].equalsIgnoreCase("FAILED")) {
                t24TXNQueue.setT24responsecode("5");
                System.out.println("Processing status = " + split[1]);
            }
            if (split[1].equalsIgnoreCase("SUCCESS")) {
                t24TXNQueue.setT24responsecode("1");
                System.out.println("Processing status = " + split[1]);
            }
            if (split.length == 3) {
                if (!split[3].isEmpty()) {
                    System.out.println(split[3]);
                    t24TXNQueue.setT24failnarration(split[3]);
                }
            }
        } else {
            t24TXNQueue.setT24responsecode("5");
            t24TXNQueue.setT24failnarration("Invalid response: " + T24RawResponse);
        }
    }

    private boolean send(TelnetClient client, String data, String referenceNo) throws IOException {
        try {
            data += "\r\n";
            client.getOutputStream().write(data.getBytes());
            client.getOutputStream().flush();
            return true;
        } catch (IOException e) {
            System.out.printf("[Error] Gateway unable to send transaction RRN [%s] OFS Message [%s]", referenceNo, data);
            e.printStackTrace();
            throw new IOException("Gateway unable to send transaction RRN " + referenceNo);
        }
    }

    private String receive(TelnetClient client, T24TXNQueue lastpostedTxns) {
        StringBuffer strBuffer;

        try {
            strBuffer = new StringBuffer();
            byte[] buf = new byte[4096];
            int len = 0;

             Thread.sleep(100L);
            int datalen = -1;
            log.info("the data recieved from T24 is {}"+ len);

            System.out.println("thread is : "+client.getReaderThread());


           // System.out.println(client.getInputStream().read(buf) >1);


            while ((len = client.getInputStream().read(buf)) != 0) {
                strBuffer.append(new String(buf, 0, len));

                // if length of received data is greater than four ... store the
                // data length if its not set yet-
                Thread.sleep(20L);

                if (datalen == -1 && strBuffer.toString().length() > 4) {
                    datalen = Integer.parseInt(strBuffer.toString().substring(0, 4));
                }
                if (client.getInputStream().available() == 0) {
                    break;
                }
                if (strBuffer.length() == datalen) {
                    break;
                }
            }
            return strBuffer.toString();

        } catch (Exception e) {
            log.info("Error receiving data from T24 at " + new Date());
            lastpostedTxns.setPostedstatus("5");
            lastpostedTxns.setT24failnarration(e.getMessage());
            e.printStackTrace();
        }
        return "";
    }

    public  String receive2(TelnetClient client) {
        StringBuffer strBuffer;
        try {

            strBuffer = new StringBuffer();
            byte[] buf = new byte[4096];
            int len = 0, datalen = -1;

            while ((len = client.getInputStream().read(buf)) != 0) {
                strBuffer.append(new String(buf, 0, len));
                // if length of received data is greater than four ... store the
                // data length if its not set yet
                Thread.sleep(20L);
                if (datalen == -1 && strBuffer.toString().length() > 4) {
                    datalen = Integer.parseInt(strBuffer.toString().substring(0, 4));
                }
                if (client.getInputStream().available() == 0) {
                    break;
                }
                if (strBuffer.length() == datalen) {
                    break;
                }
            }
            return strBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }


    private GetStudentDetailsResponse parseT24AcademicBidgeInquiry(T24TXNQueue t24TXNQueue, String gatewayref) {
        String t24Response = t24TXNQueue.getResponseleg();
        System.out.println(t24Response.isEmpty());//kelvin to do
        t24TXNQueue.setPostedstatus("1");

        GetStudentDetailsResponse student = new GetStudentDetailsResponse();
        String respone = t24Response;

       // String respone = "0303,Y.SCHOOL.ID::SCHOOL ID/Y.SCHOOL.NAME::SCHOOL NAME/Y.STUDENT.NAME::STUDENT NAME/Y.STU.REG.NO::STUDENT REGNO/Y.PAY.TYPE::PAY TYPE/Y.SCHOOL.AC::SCHOOL ACCOUNT,\"45             \"\t\"Demo school              \"\t\"Gabriel  Imanikuzwe                          \"\t\"1001190067    \"\t\"          \"\t\"40810263810194      \"\n";
        System.out.println(respone);

        String [] res = respone.split(",");
        System.out.println(res[0]);

        respone = res[1];
        System.out.println("After substring "+respone);

        String [] data = res[2].substring(1,res[2].length()-1).split("\"");

        System.out.println(res[2]);


        student.setStudent_name(data[4]);//
        student.setStudent_reg_number(data[6]);//
        student.setSchool_account_number(data[8]);//
        student.setSchool_name(data[2]);
        student.setSchool_ide(Integer.parseInt(data[0].trim()));

        System.out.println("School name"+student.getSchool_name());
        System.out.println("School id "+student.getSchool_ide());
        System.out.println("Student name"+student.getStudent_name());
        System.out.println("School acct name"+student.getSchool_account_name());
        System.out.println("School acct number"+student.getSchool_account_number());



        t24TXNQueue.setAttempts(t24TXNQueue.getAttempts() < 1 ? 1 : t24TXNQueue.getAttempts() + 1);

        System.out.println(
                "Exiting t24 parse for Rec id  : "
                        + t24TXNQueue.getT24TXNQueueid()
                        + ", "
                        + "Gateway ref "
                        + gatewayref
                        + " at "
                        + System.currentTimeMillis());

        return student;
    }

    public CustomerProfileResponse t24TransactionPosting(T24TXNQueue transactionPendingProcessing, String t24ip, int t24port) {
        String t24RequestOFS = transactionPendingProcessing.getRequestleg();
        String transactionRRN = transactionPendingProcessing.getGatewayref();
        CustomerProfileResponse student = new CustomerProfileResponse();

        log.info(
                "Processing initialization for Transaction RRN [{}] at [{}] OFS Request: [{}]",
                transactionRRN, GATEWAY_SERVER_DATE_FORMAT.format(new Date()), t24RequestOFS);
        System.out.println("Pcode is : "+transactionPendingProcessing.getProcode());

        TelnetClient telnetClient = new TelnetClient();
        try {
            log.info(
                    "Connection established to T24 for Transaction RRN [{}] at [{}] ",
                    transactionRRN, GATEWAY_SERVER_DATE_FORMAT.format(new Date()));

//            String[] OFSSplit = t24RequestOFS.split(",");
//            String OFSContainingMaskedCredentials = OFSSplit[2];

            Optional<XSwitchParameter> optionalT24UserCredentials = xSwitchParameterRepository.findByParamName("T24USER");
            Optional<XSwitchParameter> optionalT24Pass = xSwitchParameterRepository.findByParamName("T24PASS");

            if (optionalT24Pass.isEmpty() || optionalT24UserCredentials.isEmpty()) {
                log.info("****************************** Missing T24 Pass or T24 User on the database. ******************************");
            }

            String t24usn = utilityService.decryptSensitiveData(optionalT24UserCredentials.get().getParamValue());
            String t24pwd = utilityService.decryptSensitiveData(optionalT24Pass.get().getParamValue());

//            String[] T24ChannelCredentials = OFSContainingMaskedCredentials.split("/");
//            String OFSWithPlainTextCredentials =
//                    t24RequestOFS.replaceAll(T24ChannelCredentials[0], t24usn);
//            OFSWithPlainTextCredentials =
//                    OFSWithPlainTextCredentials.replaceAll(T24ChannelCredentials[1], t24pwd);

            t24RequestOFS = t24RequestOFS.replace(MASKED_T24_USERNAME, t24usn.trim());
            String OFSWithPlainTextCredentials = t24RequestOFS.replace(MASKED_T24_PASSWORD, t24pwd.trim());


            String minus4 = OFSWithPlainTextCredentials.substring(4);
            String formattedOFSMessageString = String.format("%04d", minus4.length()) + minus4;

            log.info("Formatted OFS Message String: {}", formattedOFSMessageString);

            telnetClient.connect(t24ip, t24port);
            if(!telnetClient.isConnected()){
                //log that connection has failed
                //retun
                //to do waweru
            }
            log.info("Connection established >>>> {}", telnetClient.isConnected());
            boolean sent = send(telnetClient, formattedOFSMessageString.trim(), transactionRRN);
            log.info("Transaction sent to t24");
            String T24ResponseOFS = receive(telnetClient, transactionPendingProcessing);
            // String T24ResponseOFS = receive2(telnetClient);
            // String T24ResponseOFS = "yolo";
            System.out.println("####################################################################");
            System.out.println("Response from t24 is :> "+T24ResponseOFS);
            System.out.println("####################################################################");

            log.info(
                    "T24 response received for Transaction RRN [{}] at [{}] :: Response [{}]",
                    transactionRRN, GATEWAY_SERVER_DATE_FORMAT.format(new Date()), T24ResponseOFS);

            transactionPendingProcessing.setResponseleg(T24ResponseOFS);
            transactionPendingProcessing.setEndtime(System.currentTimeMillis());
            GetStudentDetailsResponse response = new GetStudentDetailsResponse();

            if (T24ResponseOFS.length() < 5) {
                log.info(
                        "[Error] T24 failed processing Transaction RRN [{}] at [{}] :: Response [UNEXPECTED RESPONSE FROM REMOTE SYSTEM]",
                        transactionRRN, GATEWAY_SERVER_DATE_FORMAT.format(new Date()));
                transactionPendingProcessing.setPostedstatus("4");
                transactionPendingProcessing.setT24failnarration("UNEXPECTED RESPONSE FROM REMOTE SYSTEM");
            } else {
                System.out.println("Pcode in else is : "+transactionPendingProcessing.getProcode());
                try {
                    switch (transactionPendingProcessing.getProcode() == null ? "" : transactionPendingProcessing.getProcode()) {
                        case "460001":
                            if (transactionPendingProcessing.getTxnmti().equals("1100")) {
                                parseT24EuclElecInquiry(transactionPendingProcessing, transactionRRN);
                            } if(transactionPendingProcessing.getTxnmti().equals("1200")) {
                            response = parseT24AcademicBidgeInquiry(transactionPendingProcessing, transactionRRN);
                            student.setMessage("Transaction successful");
                            student.setStatus("00");
                            student.setData(response);
                        }
                        else {
                            parseT24ResponseRefactored(transactionPendingProcessing, transactionRRN);
                        }
                            break;
                        case "510000":
                        case "430000":
                        case "500000":
                            parseT24EnquiryResponse(transactionPendingProcessing, transactionRRN);
                            break;
                        case "500001":
                            parseT24SMSResponse(transactionPendingProcessing, transactionRRN);
                            break;
                        default:
                            parseT24ResponseRefactored(transactionPendingProcessing, transactionRRN);
                            break;
                    }
                } catch (Exception e) {
                    log.info("Error Parsing T24 Response : for  {}", transactionRRN );
                    transactionPendingProcessing.setPostedstatus("5");
                    transactionPendingProcessing.setT24failnarration(e.getMessage());
                    e.printStackTrace();
                }
            }

        } catch (IOException ex) {
            transactionPendingProcessing.setPostedstatus("5");
            transactionPendingProcessing.setT24failnarration(ex.getMessage());
            System.out.printf(
                    "[Error] An exception occurred while processing Transaction RRN [%s] at [%s] :: Response [%s]\n",
                    transactionRRN,
                    GATEWAY_SERVER_DATE_FORMAT.format(new Date()),
                    ex.getMessage());
        } finally {
            if (telnetClient.isConnected()) {
                try {
                    System.out.printf(
                            "T24 channel closed for transaction RRN [%s] at [%s] ",
                            transactionRRN, GATEWAY_SERVER_DATE_FORMAT.format(new Date()));
                    telnetClient.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return student;
    }


    public CustomerProfileResponse processTransactionToT24(String t24Ip, int t24Port, T24TXNQueue transactionPendingT24Processing) {
        log.info("Processing T24 transaction");
        CustomerProfileResponse student = new CustomerProfileResponse();
        try {
            if (transactionPendingT24Processing != null) {
                log.info(
                        "Transaction {} fetched from database queue ready for processing at T24",
                        transactionPendingT24Processing.getGatewayref());

                transactionPendingT24Processing.setAttempts(
                        Math.max(transactionPendingT24Processing.getAttempts(), 1));

                transactionPendingT24Processing.setStarttime(System.currentTimeMillis());
                transactionPendingT24Processing.setPostedstatus("6");
//                boolean xx = t24TransactionPosting(transactionPendingT24Processing, t24Ip, t24Port);
                student = t24TransactionPosting(transactionPendingT24Processing, t24Ip, t24Port);
                log.info("After transaction posting >>>> {}", student.getMessage());
            }
        } catch (Exception e) {
            log.info(
                    "[Error] Unable to fetch transaction from database queue. Error message: {}",
                    e.getMessage());
            transactionPendingT24Processing.setPostedstatus("3");
            e.printStackTrace();
        }
        return student;
    }

}
