package co.ke.tracom.bprgateway.web.rwandarevenue.services;

import co.ke.tracom.bprgateway.web.agenttransactions.dto.response.AuthenticateAgentResponse;
import co.ke.tracom.bprgateway.web.irembo.dto.response.IremboPaymentResponse;
import co.ke.tracom.bprgateway.web.rwandarevenue.dto.requests.RRATINValidationRequest;
import co.ke.tracom.bprgateway.web.rwandarevenue.dto.responses.RRAData;
import co.ke.tracom.bprgateway.web.rwandarevenue.dto.responses.RRATINValidationResponse;
import co.ke.tracom.bprgateway.web.switchparameters.repository.XSwitchParameterRepository;
import co.ke.tracom.bprgateway.web.transactions.entities.TransactionAdvices;
import co.ke.tracom.bprgateway.web.transactions.repository.TransactionAdvicesRepository;
import co.ke.tracom.bprgateway.web.util.services.BaseServiceProcessor;
import lombok.RequiredArgsConstructor;
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
import org.json.XML;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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

@Slf4j
@RequiredArgsConstructor
@Service
public class RRAService {
    @Value("${merchant.account.validation}")
    private String agentValidation;
    private final BaseServiceProcessor baseServiceProcessor;
    private final XSwitchParameterRepository xSwitchParameterRepository;

    private final TransactionAdvicesRepository transactionAdvicesRepository;


    public RRATINValidationResponse validateCustomerTIN(RRATINValidationRequest request, String transactionRRN) {
        Optional<AuthenticateAgentResponse> optionalAuthenticateAgentResponse = baseServiceProcessor.authenticateAgentUsernamePassword(request.getCredentials(), agentValidation);
        if (optionalAuthenticateAgentResponse.isEmpty()) {
            log.info(
                    "RRA Validation :[Failed] Missing agent information.  Transaction RRN [" + transactionRRN + "]");
            return RRATINValidationResponse.builder()
                    .status("117")
                    .message("Missing agent information")
                    .data(null).build();
        }else if (optionalAuthenticateAgentResponse.get().getCode() != HttpStatus.OK.value()) {
            return RRATINValidationResponse
                    .builder()
                    .status(String.valueOf(
                            optionalAuthenticateAgentResponse.get().getCode()))
                    .message(optionalAuthenticateAgentResponse.get().getMessage())
                    .build();
        }
        HttpPost httpPost = null;

        try {
            Boolean exists = findPendingRRAPaymentOnQueueByRRATIN(request.getRRATIN());

            if (exists) {
                String format = String.format(
                        "RRA Transaction with RRA TIN NO: %s and POS RRN [%s] could not be processed. RRA TIN has a pending transaction on the gateway queue",
                        request.getRRATIN(), transactionRRN);
                log.info(format);
                return RRATINValidationResponse.builder()
                        .status("097")
                        .message("Duplicate RRA reference Payment detected. Kindly verify with BPR Customer care or try later")
                        .data(null).build();
            }

            System.out.printf("Preparing XML request for transaction RRN[%s] with RRA Ref [%s]",
                    transactionRRN, request.getRRATIN());

            String rraValidationPayload = bootstrapRRAXMLRequest(request.getRRATIN());


            String rrasoapurl = xSwitchParameterRepository.findByParamName("RRASOAPURL").get().getParamValue();
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
                        "\n \n  Empty response for reference : %s \n \n " + request.getRRATIN());
            }

            // always true
            if (!retrnedxml.isEmpty()) {

                // Remove header that may cause errors during conversion using document builder factory
                String properxml =
                        retrnedxml.replace("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>", "");
                // print results
                System.out.println("RRA >> RETURNED VALIDATION DATA  Start ::  \n" + properxml);
                System.out.println("\n=============== <<< :: End");

                // Convert to xml string ...
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
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
                // transformer.setOutputProperty(OutputKeys.METHOD, "xml");
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

                for (int i = 0; i < nodes.getLength(); ++i) {
                    source.setNode(nodes.item(i));
                    transformer.transform(source, result);
                }

                System.out.println(
                        "rra res writer unescape : \n " + StringEscapeUtils.unescapeXml(writer.toString()));

                // writer.toString()
                JSONObject xmlJSONObj = XML.toJSONObject(writer.toString());

                if (xmlJSONObj == null || xmlJSONObj.length() == 0) {
                    System.out.println("NO response for RRA REF " + request.getRRATIN());
                    return RRATINValidationResponse.builder()
                            .status("908")
                            .message("Ref " + request.getRRATIN() + " could not be verified by RRA system")
                            .data(null).build();

                } else {
                    JSONObject xmlJSONObj_DECLARATION =
                            xmlJSONObj.getJSONObject("TO_BANK").getJSONObject("DECLARATION");
                    System.out.println("response" + xmlJSONObj_DECLARATION.toString(4));
                    Object RRA_REF_OBJ = xmlJSONObj_DECLARATION.get("RRA_REF");
                    String RRA_REF = "";
                    // System.out.println(RRA_REF_OBJ.);
                    if (RRA_REF_OBJ instanceof Long || RRA_REF_OBJ instanceof Integer) {
                        // System.out.println(" this");
                        long intToUse = ((Number) RRA_REF_OBJ).longValue();
                        RRA_REF = intToUse + "";
                    } else {
                        // System.out.println("not this");
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
                            .message("Ref " + request.getRRATIN() + " validated successfully")
                            .data(rraData).build();
                }
            } else {
                return RRATINValidationResponse.builder()
                        .status("098")
                        .message("No response from RRA for ref : " + request.getRRATIN() + " system")
                        .data(null).build();
            }

        } catch (JSONException je) {
            log.info("RRA TIN Validation for " + request.getRRATIN() + " failed. " + je.getMessage());
            return RRATINValidationResponse.builder()
                    .status("098")
                    .message("RRA Ref Could not be verified.")
                    .data(null).build();

        } catch (Exception je) {
            je.printStackTrace();

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
        Optional<TransactionAdvices> optionalTransactionAdvices = transactionAdvicesRepository.findByAdvisedAndTrialsLessThanAndTranstypeAndReqtypeAndRefNumber(
                "N", 3, "470000", "1200_RRAPAYMENT", RRATIN
        );
        return optionalTransactionAdvices.isPresent();
    }

    private String bootstrapRRAXMLRequest(String rwandaRevenueAuthorityTIN) {
        String RRAID = xSwitchParameterRepository.findByParamName("RRA_ID").get().getParamValue();
        String RRAPass = xSwitchParameterRepository.findByParamName("RRA_PASS").get().getParamValue();

        return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ws=\"http://WS.epay.rra.rw\">\n"
                + "   <soapenv:Header/>\n"
                + "   <soapenv:Body>\n"
                + "      <ws:getDec>\n"
                + "         <ws:userID>"
                + RRAID
                + "</ws:userID>\n"
                + "         <ws:userPassword>"
                + RRAPass
                + "</ws:userPassword>\n"
                + "         <ws:RRA_ref>"
                + rwandaRevenueAuthorityTIN
                + "</ws:RRA_ref>\n"
                + "      </ws:getDec>\n"
                + "   </soapenv:Body>\n"
                + "</soapenv:Envelope>";
    }
}
