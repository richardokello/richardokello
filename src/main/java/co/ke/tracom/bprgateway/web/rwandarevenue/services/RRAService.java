package co.ke.tracom.bprgateway.web.rwandarevenue.services;

import co.ke.tracom.bprgateway.web.agenttransactions.dto.response.AuthenticateAgentResponse;
import co.ke.tracom.bprgateway.web.agenttransactions.services.AgentTransactionService;
import co.ke.tracom.bprgateway.web.bankbranches.entity.BPRBranches;
import co.ke.tracom.bprgateway.web.bankbranches.service.BPRBranchService;
import co.ke.tracom.bprgateway.web.rwandarevenue.dto.requests.RRAPaymentRequest;
import co.ke.tracom.bprgateway.web.rwandarevenue.dto.requests.RRATINValidationRequest;
import co.ke.tracom.bprgateway.web.rwandarevenue.dto.responses.RRAData;
import co.ke.tracom.bprgateway.web.rwandarevenue.dto.responses.RRAPaymentResponse;
import co.ke.tracom.bprgateway.web.rwandarevenue.dto.responses.RRAPaymentResponseData;
import co.ke.tracom.bprgateway.web.rwandarevenue.dto.responses.RRATINValidationResponse;
import co.ke.tracom.bprgateway.web.switchparameters.XSwitchParameterService;
import co.ke.tracom.bprgateway.web.t24communication.services.T24Channel;
import co.ke.tracom.bprgateway.web.transactions.entities.T24TXNQueue;
import co.ke.tracom.bprgateway.web.transactions.entities.TransactionAdvices;
import co.ke.tracom.bprgateway.web.transactions.repository.TransactionAdvicesRepository;
import co.ke.tracom.bprgateway.web.transactions.services.TransactionService;
import co.ke.tracom.bprgateway.web.util.services.BaseServiceProcessor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Optional;

import static co.ke.tracom.bprgateway.web.t24communication.services.T24Channel.MASKED_T24_PASSWORD;
import static co.ke.tracom.bprgateway.web.t24communication.services.T24Channel.MASKED_T24_USERNAME;

@Slf4j
@RequiredArgsConstructor
@Service
public class RRAService {
    private final BaseServiceProcessor baseServiceProcessor;
    private final BPRBranchService bprBranchService;
    private final AgentTransactionService agentTransactionService;
    private final T24Channel t24Channel;
    private final TransactionService transactionService;

    private final XSwitchParameterService xSwitchParameterService;
    private final TransactionAdvicesRepository transactionAdvicesRepository;


    @SneakyThrows
    public RRATINValidationResponse validateCustomerTIN(RRATINValidationRequest request, String transactionRRN) {
        AuthenticateAgentResponse optionalAuthenticateAgentResponse = baseServiceProcessor.authenticateAgentUsernamePassword(request.getCredentials());

        HttpPost httpPost = null;
        try {
            String rrasoapurl = xSwitchParameterService.fetchXSwitchParamValue("RRASOAPURL");
            Boolean exists = findPendingRRAPaymentOnQueueByRRATIN(request.getRrareferenceNo());

            if (exists) {
                String format = String.format(
                        "RRA Transaction with RRA TIN NO: %s and POS RRN [%s] could not be processed. RRA TIN has a pending transaction on the gateway queue",
                        request.getRrareferenceNo(), transactionRRN);
                log.info(format);
                return RRATINValidationResponse.builder()
                        .status("097")
                        .message("Duplicate RRA reference Payment detected. Kindly verify with BPR Customer care or try later")
                        .data(null).build();
            }

            System.err.printf("Preparing XML request for transaction RRN[%s] with RRA Ref [%s]",
                    transactionRRN, request.getRrareferenceNo());

            String rraValidationPayload = bootstrapRRAXMLRequest(request.getRrareferenceNo());
            System.err.println("rraValidationPayload = " + rraValidationPayload);


            System.err.println("rrasoapurl = " + rrasoapurl);
            httpPost = new HttpPost(rrasoapurl);
            StringEntity stringEntity = new StringEntity(rraValidationPayload, "UTF-8");
            stringEntity.setChunked(true);


            // Request parameters and other properties.
            httpPost.setEntity(stringEntity);
            httpPost.addHeader("Accept", "text/xml");
            httpPost.addHeader("SOAPAction", "http://WS.epay.rra.rw");
            RequestConfig.Builder requestBuilder = RequestConfig.custom();
            requestBuilder = requestBuilder.setConnectTimeout(30000);
            requestBuilder = requestBuilder.setConnectionRequestTimeout(30000);
            HttpClientBuilder builder = HttpClientBuilder.create();
            builder.setDefaultRequestConfig(requestBuilder.build());
            HttpClient httpClient = builder.build();

            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            String retrnedxml = null;

            // Check if the server responded
            if (entity != null) {
                retrnedxml = EntityUtils.toString(entity);
                retrnedxml = StringEscapeUtils.unescapeXml(retrnedxml);
            } else {
                // Server did not respond
                retrnedxml = "";
                System.out.printf(
                        "\n \n  Empty response for reference : %s \n \n " + request.getRrareferenceNo());
            }


            // always true
            if (!retrnedxml.isEmpty()) {

                System.out.println("RRA >> RETURNED RAW VALIDATION DATA  Start ::  \n" + retrnedxml);
                System.out.println("\n=============== <<< :: End");
                // Remove header that may cause errors during conversion using document builder factory
                String properxml =
                        retrnedxml.replace("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>", "");
                // print results
                System.out.println("RRA >> RETURNED VALIDATION DATA  Start ::  \n" + properxml);
                System.out.println("\n=============== <<< :: End");

                // Convert to xml string ...
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                //dbFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(new InputSource(new StringReader(properxml)));
                doc.getDocumentElement().normalize();
                // Look for specific node  // S:Envelope
                NodeList statusMessages = doc.getElementsByTagName("getDecResponse");
                Element getDecReturnINNER = (Element) statusMessages.item(0);
                NodeList nodes = getDecReturnINNER.getElementsByTagName("TO_BANK");

                DOMSource source = new DOMSource();
                StringWriter writer = new StringWriter();
                StreamResult result = new StreamResult(writer);
                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

                for (int i = 0; i < nodes.getLength(); ++i) {
                    source.setNode(nodes.item(i));
                    transformer.transform(source, result);
                }

                System.err.println(
                        "rra res writer unescape : \n " + StringEscapeUtils.unescapeXml(writer.toString()));

                // writer.toString()
                JSONObject xmlJSONObj = XML.toJSONObject(writer.toString());
                log.info("the Xml data is -->>" + xmlJSONObj);

                if (xmlJSONObj == null || xmlJSONObj.length() == 0) {
                    System.err.println("NO response for RRA R3EF " + request.getRrareferenceNo());
                    return RRATINValidationResponse.builder()
                            .status("908")
                            .message("Ref " + request.getRrareferenceNo() + " could not be verified by RRA system")
                            .data(null).build();

                } else {
                    JSONObject xmlJSONObj_DECLARATION =
                            xmlJSONObj.getJSONObject("TO_BANK").getJSONObject("DECLARATION");
                    System.err.println("response" + xmlJSONObj_DECLARATION.toString(4));
                    Object RRA_REF_OBJ = xmlJSONObj_DECLARATION.get("RRA_REF");
                    String RRA_REF = "";
                    if (RRA_REF_OBJ instanceof Long || RRA_REF_OBJ instanceof Integer) {
                        long intToUse = ((Number) RRA_REF_OBJ).longValue();
                        RRA_REF = intToUse + "";
                    } else {
                        RRA_REF = xmlJSONObj_DECLARATION.getString("RRA_REF");
                    }
                    int DEC_ID = xmlJSONObj_DECLARATION.getInt("DEC_ID");
                    Object TINOBJ = xmlJSONObj_DECLARATION.get("TIN");
                    String TIN = "";
                    if (TINOBJ instanceof Long || TINOBJ instanceof Integer) {
                        long intToUse = ((Number) TINOBJ).longValue();
                        TIN = intToUse + "";
                    } else {
                        TIN =
                                xmlJSONObj_DECLARATION.getString("TIN") == null
                                        ? ""
                                        : xmlJSONObj_DECLARATION.getString("TIN");
                    }

                    String TAX_PAYER_NAME =
                            xmlJSONObj_DECLARATION.getString("TAX_PAYER_NAME") == null
                                    ? ""
                                    : xmlJSONObj_DECLARATION.getString("TAX_PAYER_NAME");
                    Long AMOUNT_TO_PAY = xmlJSONObj_DECLARATION.getLong("AMOUNT_TO_PAY");
                    int TAX_TYPE_NO = xmlJSONObj_DECLARATION.getInt("TAX_TYPE_NO");
                    String TAX_TYPE_DESC =
                            xmlJSONObj_DECLARATION.getString("TAX_TYPE_DESC") == null
                                    ? ""
                                    : xmlJSONObj_DECLARATION.getString("TAX_TYPE_DESC");
                    int TAX_CENTRE_NO = xmlJSONObj_DECLARATION.getInt("TAX_CENTRE_NO");
                    String TAX_CENTRE_DESC =
                            xmlJSONObj_DECLARATION.getString("TAX_CENTRE_DESC") == null
                                    ? ""
                                    : xmlJSONObj_DECLARATION.getString("TAX_CENTRE_DESC");
                    int ASSESS_NO = xmlJSONObj_DECLARATION.getInt("ASSESS_NO");
                    int RRA_ORIGIN_NO = xmlJSONObj_DECLARATION.getInt("RRA_ORIGIN_NO");
                    String DEC_DATE =
                            xmlJSONObj_DECLARATION.getString("DEC_DATE") == null
                                    ? ""
                                    : xmlJSONObj_DECLARATION.getString("DEC_DATE");
                    String REQUEST_DATE =
                            xmlJSONObj_DECLARATION.getString("REQUEST_DATE") == null
                                    ? ""
                                    : xmlJSONObj_DECLARATION.getString("REQUEST_DATE");

                    // RRA_REF,DEC_ID,TIN,TAX_PAYER_NAME,AMOUNT_TO_PAY,TAX_TYPE_NO,TAX_TYPE_DESC,TAX_CENTRE_NO,TAX_CENTRE_DESC,ASSESS_NO,RRA_ORIGIN_NO,DEC_DATE,REQUEST_DATE
                    RRAData rraData = RRAData.builder()
                            .taxTypeDescription(TAX_TYPE_DESC)
                            .declarationDate(DEC_DATE)
                            .taxCentreNo(TAX_CENTRE_NO)
                            .TIN(TIN)
                            .RRAOriginNo(RRA_ORIGIN_NO)
                            .declarationID(DEC_ID)
                            .taxPayerName(TAX_PAYER_NAME)
                            .requestDate(REQUEST_DATE)
                            .taxCentreDescription(TAX_CENTRE_DESC)
                            .taxTypeNo(TAX_TYPE_NO)
                            .assessNo(ASSESS_NO)
                            .amountToPay(AMOUNT_TO_PAY)
                            .RRAReferenceNo(RRA_REF)
                            .build();

                    return RRATINValidationResponse.builder()
                            .status("00")
                            .message("Ref " + request.getRrareferenceNo() + " validated successfully")
                            .data(rraData).build();
                }
            } else {
                return RRATINValidationResponse.builder()
                        .status("098")
                        .message("No response from RRA for ref : " + request.getRrareferenceNo() + " system")
                        .data(null).build();
            }

        } catch (JSONException je) {
            log.info("RRA TIN Validation for " + request.getRrareferenceNo() + " failed. " + je.getMessage());
            return RRATINValidationResponse.builder()
                    .status("098")
                    .message("RRA Ref Could not be verified.")
                    .data(null).build();

        } catch (Exception je) {
            je.printStackTrace();
            System.out.println("je exception= " + je);
            return RRATINValidationResponse.builder()
                    .status("097")
                    .message("RRA System could not be reached. Please try later or contact contact BPR customer care")
                    .data(null).build();


        } finally {
            if (httpPost != null) {
                httpPost.releaseConnection();
            }
        }
    }

    private Boolean findPendingRRAPaymentOnQueueByRRATIN(String RRATIN) {
        Optional<TransactionAdvices> optionalTransactionAdvices = transactionAdvicesRepository.findByAdvisedAndTrialsLessThanAndTransactionTypeAndRequestTypeAndTransactionReference(
                "N", 3, "470000", "1200_RRAPAYMENT", RRATIN
        );
        return optionalTransactionAdvices.isPresent();
    }

    private String bootstrapRRAXMLRequest(String rwandaRevenueAuthorityTIN) {
        String id = xSwitchParameterService.fetchXSwitchParamValue("RRA_ID");
        String rrapass = xSwitchParameterService.fetchXSwitchParamValue("RRA_PASS");

        System.err.println("ID = " + id);
        System.err.println("rrapass = " + rrapass);

        String body =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ws=\"http://WS.epay.rra.rw\">\n"
                        + "   <soapenv:Header/>\n"
                        + "   <soapenv:Body>\n"
                        + "      <ws:getDec>\n"
                        + "         <ws:userID>"
                        + id
                        + "</ws:userID>\n"
                        + "         <ws:userPassword>"
                        + rrapass
                        + "</ws:userPassword>\n"
                        + "         <ws:RRA_ref>"
                        + rwandaRevenueAuthorityTIN
                        + "</ws:RRA_ref>\n"
                        + "      </ws:getDec>\n"
                        + "   </soapenv:Body>\n"
                        + "</soapenv:Envelope>";

        return body;
    }

    @SneakyThrows
    public RRAPaymentResponse processRRAPayment(RRAPaymentRequest request, String transactionRRN) {
        AuthenticateAgentResponse authenticateAgentResponse = baseServiceProcessor.authenticateAgentUsernamePassword(request.getCredentials());
        try {

            String tid = "PC";
            final String RRA_REF = request.getRRAReferenceNo();
            long DEC_ID = request.getDeclarationID();
            String TIN = request.getTIN();
            String TAX_PAYER_NAME = request.getTaxPayerName();
            double AMOUNT_TO_PAY = request.getAmountToPay();
            int TAX_TYPE_NO = request.getTaxTypeNo();
            int TAX_CENTRE_NO = request.getTaxCentreNo();
            long ASSESS_NO = request.getAssessNo();
            int RRA_ORIGIN_NO = request.getRRAOriginNo();

            String agentFloatAccount = authenticateAgentResponse.getData().getAccountNumber();
            BPRBranches branch = bprBranchService.fetchBranchAccountsByBranchCode(agentFloatAccount);
            if (branch.getCompanyName() == null) {
                log.info("Agent float deposit transaction [" + transactionRRN + "] failed. Error: Agent branch details could not be verified.");

                return RRAPaymentResponse.builder()
                        .status("065")
                        .message("Agent branch details could not be verified. Kindly contact BPR customer care")
                        .data(null).build();
            }

            String agentBranchId = branch.getId();
            if (agentBranchId.isEmpty()) {
                return RRAPaymentResponse.builder()
                        .status("065")
                        .message("Agent branch details could not be verified. Kindly contact BPR customer care")
                        .data(null).build();
            }

            long agentFloatBalance = agentTransactionService.fetchAgentAccountBalanceOnly(agentFloatAccount);
            log.info("Fet balance success >>> is greater {}", (agentFloatBalance > AMOUNT_TO_PAY));
            if (agentFloatBalance < AMOUNT_TO_PAY) {
                return RRAPaymentResponse.builder()
                        .status("065")
                        .message("Insufficient agent float balance.")
                        .data(null).build();
            }
            String channel = "PC";
            String sanitizedTaxPayerName = TAX_PAYER_NAME.length() > 49 ? TAX_PAYER_NAME.substring(0, 49) : TAX_PAYER_NAME;

            String RRAOFSMsg =
                    "0000AFUNDS.TRANSFER,BPR.AGB.ETAX/I/PROCESS," +
                            MASKED_T24_USERNAME +
                            "/"
                            + MASKED_T24_PASSWORD
                            + "/"
                            + agentBranchId
                            + ",,TRANSACTION.TYPE::=ACTT,RRA.REF::="
                            + RRA_REF
                            + ",TCM.REF::="
                            + transactionRRN
                            + ",RRA.DEC.ID::="
                            + DEC_ID
                            + ",DEBIT.CURRENCY::=RWF,DEBIT.ACCT.NO::="
                            + agentFloatAccount
                            + ",DEBIT.AMOUNT::="
                            + AMOUNT_TO_PAY
                            + ","
                            + "PAYMENT.DETAILS:1::="
                            + RRA_REF
                            + "/"
                            + TIN
                            + ",RRA.TIN.NO::="
                            + TIN
                            + ",TAX.PAYER.NAME::="
                            + sanitizedTaxPayerName
                            + ",RRA.ASSESS.NO::="
                            + ASSESS_NO
                            + ",RRA.ORIGIN.NO::="
                            + RRA_ORIGIN_NO
                            + ",RRA.TAX.TYPE.NO::="
                            + TAX_TYPE_NO
                            + ",RRA.CENTER.NO::="
                            + TAX_CENTRE_NO;


            String tot24str = String.format("%04d", RRAOFSMsg.length()) + RRAOFSMsg;
            log.info("RRA T24 OFS REQ: {}", tot24str);

            log.info("channel :" + channel);
            log.info("tid :" + tid);

            T24TXNQueue tot24 = new T24TXNQueue();
            tot24.setRequestleg(tot24str);
            tot24.setStarttime(System.currentTimeMillis());
            tot24.setTxnchannel(channel);
            tot24.setGatewayref(transactionRRN);
            tot24.setPostedstatus("0");
            tot24.setTid(tid);
            tot24.setProcode("460000");
            tot24.setDebitacctno(agentFloatAccount);

            final String t24Ip = xSwitchParameterService.fetchXSwitchParamValue("T24_IP");
            final String t24Port = xSwitchParameterService.fetchXSwitchParamValue("T24_PORT");
            log.info("Fetched port and ip success, ip {}, port {}", (t24Ip != null), (t24Port != null));
            t24Channel.processTransactionToT24(t24Ip, Integer.parseInt(t24Port), tot24);

            transactionService.updateT24TransactionDTO(tot24);

            String t24ref = tot24.getT24reference() == null ? "NA" : tot24.getT24reference();

            if (tot24.getT24responsecode().equalsIgnoreCase("1")) {
                return processSuccessfulT24RRATransaction(request, transactionRRN, authenticateAgentResponse, tot24, t24ref);
            } else {
                transactionService.updateT24TransactionDTO(tot24);
                transactionService.saveCardLessTransactionToAllTransactionTable(tot24, "RRA", "1200",
                        request.getAmountToPay(), "118",
                        authenticateAgentResponse.getData().getTid(), authenticateAgentResponse.getData().getMid());
                RRAPaymentResponseData data = RRAPaymentResponseData.builder()
                        .T24Reference(t24ref)
                        .transactionCharges(0.0)
                        .RRAReference(RRA_REF)
                        .taxPayerName(TAX_PAYER_NAME)
                        .rrn(transactionRRN)
                        .build();

                data.setUsername(authenticateAgentResponse.getData().getUsername());
                data.setNames(authenticateAgentResponse.getData().getNames());
                data.setBusinessName(authenticateAgentResponse.getData().getBusinessName());
                data.setLocation(authenticateAgentResponse.getData().getLocation());

                data.setTid(authenticateAgentResponse.getData().getTid());
                data.setMid(authenticateAgentResponse.getData().getMid());

                return RRAPaymentResponse.builder()
                        .status("118")
                        .message("Transaction processing failed. " + tot24.getT24failnarration())
                        .data(data).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("RRA Transaction [" + transactionRRN + "] failed during processing. Kindly contact BPR Customer Care");
            return RRAPaymentResponse.builder()
                    .status("118")
                    .message("RRA Transaction [" + authenticateAgentResponse.getData().getAccountNumber() + " ] failed during processing. Kindly contact BPR Customer Care")
                    .data(null).build();
        }
    }

    private RRAPaymentResponse processSuccessfulT24RRATransaction(RRAPaymentRequest request, String transactionRRN, AuthenticateAgentResponse authenticateAgentResponse, T24TXNQueue tot24, String t24ref) {
        try {
            String charges = tot24.getTotalchargeamt();
            String cleanedChargeAmount = charges.replace("RWF", "");

            String ISOFormatAmount = String.format("%012d", Integer.parseInt(cleanedChargeAmount));
            log.info("RRA Transaction [" + transactionRRN + "] charged amount " + ISOFormatAmount);

            transactionService.updateT24TransactionDTO(tot24);
            transactionService.saveCardLessTransactionToAllTransactionTable(tot24, "RRA", "1200",
                    request.getAmountToPay(), "000",
                    authenticateAgentResponse.getData().getTid(), authenticateAgentResponse.getData().getMid());

            RRAPaymentResponseData data = RRAPaymentResponseData.builder()
                    .T24Reference(t24ref)
                    .transactionCharges(Double.parseDouble(cleanedChargeAmount))
                    .rrn(transactionRRN)
                    .RRAReference(request.getRRAReferenceNo())
                    .taxPayerName(request.getTaxPayerName())
                    .mid(authenticateAgentResponse.getData().getMid())
                    .tid(authenticateAgentResponse.getData().getTid())
                    .build();
            data.setUsername(authenticateAgentResponse.getData().getUsername());
            data.setUsername(authenticateAgentResponse.getData().getUsername());
            data.setNames(authenticateAgentResponse.getData().getNames());
            data.setBusinessName(authenticateAgentResponse.getData().getBusinessName());
            data.setLocation(authenticateAgentResponse.getData().getLocation());


            return RRAPaymentResponse.builder()
                    .status("00")
                    .message("RRA Transaction successful")
                    .data(data).build();

        } catch (Exception e) {
            e.printStackTrace();
            transactionService.updateT24TransactionDTO(tot24);
            transactionService.saveCardLessTransactionToAllTransactionTable(tot24, "RRA", "1200",
                    request.getAmountToPay(), "098",
                    authenticateAgentResponse.getData().getTid(), authenticateAgentResponse.getData().getMid());
            RRAPaymentResponseData data = RRAPaymentResponseData.builder()
                    .T24Reference(t24ref)
                    .transactionCharges(0.0)
                    .rrn(transactionRRN)
                    .build();

            data.setUsername(authenticateAgentResponse.getData().getUsername());
            data.setNames(authenticateAgentResponse.getData().getNames());
            data.setBusinessName(authenticateAgentResponse.getData().getBusinessName());
            data.setLocation(authenticateAgentResponse.getData().getLocation());
            data.setTid(authenticateAgentResponse.getData().getTid());
            data.setMid(authenticateAgentResponse.getData().getMid());

            return RRAPaymentResponse.builder()
                    .status("00")
                    .message("RRA Transaction successful")
                    .data(data).build();
        }
    }
}
