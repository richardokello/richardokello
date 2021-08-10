package co.ke.tracom.bprgateway.web.accountopening.service;

import co.ke.tracom.bprgateway.web.accountopening.dto.request.NIDValidationRequest;
import co.ke.tracom.bprgateway.web.accountopening.dto.response.NIDData;
import co.ke.tracom.bprgateway.web.accountopening.dto.response.NIDValidationResponse;
import co.ke.tracom.bprgateway.web.accountvalidation.data.BPRAccountValidationResponse;
import co.ke.tracom.bprgateway.web.agenttransactions.dto.response.AuthenticateAgentResponse;
import co.ke.tracom.bprgateway.web.switchparameters.repository.XSwitchParameterRepository;
import co.ke.tracom.bprgateway.web.util.services.BaseServiceProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class NIDValidationService {

    private final BaseServiceProcessor baseServiceProcessor;
    private final XSwitchParameterRepository xSwitchParameterRepository;

    public NIDValidationResponse validateNationalID(NIDValidationRequest request, String referenceNo) {
        AuthenticateAgentResponse optionalAuthenticateAgentResponse = baseServiceProcessor.authenticateAgentUsernamePassword(request.getCredentials());

        String returneddocumentid = "";

        String sendernationalid = request.getNationalID();
        String typeofid = request.getIDType();
        String frmattednid = "";

        if (typeofid.equals("0")) {
            HashMap<String, String> validationResults  = prepareAndSendRequest(sendernationalid);

            if (validationResults.get("txnstatus").equalsIgnoreCase("OK")) {
                returneddocumentid = validationResults.get("DocumentNumber").isEmpty() ? "" : validationResults.get("DocumentNumber");
                frmattednid = returneddocumentid.replaceAll(" ", "").trim();
                System.out.println("Formatted ID : " + frmattednid);
                System.out.println("Returned NID Data ~~ " + validationResults.toString());
                System.out.println("Sender national ID ~~ " + sendernationalid);

                if (frmattednid.equalsIgnoreCase(sendernationalid)) {
                    log.info("NID validation for transaction " + referenceNo + " was successful");
                    NIDData data = NIDData.builder()
                            .nationalId(request.getNationalID())
                            .firstName(validationResults.get("ForeName"))
                            .surname(validationResults.get("Surnames"))
                            .build();

                    return NIDValidationResponse
                            .builder()
                            .status("00")
                            .message("The document number verified successfully. ")
                            .data(data)
                            .build();
                } else {
                    log.info("NID validation for transaction " + referenceNo + " failed. No match found for document no " + returneddocumentid);
                    return NIDValidationResponse
                            .builder()
                            .status("01")
                            .message("The document number could not be fetched from NID. kindly verify details")
                            .data(null)
                            .build();
                }
            }
        }

        return NIDValidationResponse
                .builder()
                .status("01")
                .message("Document number validation failed. Please try again later")
                .data(null)
                .build();
    }

    public HashMap<String, String> prepareAndSendRequest(String documentNo) {
        HashMap<String, String> niddata = new HashMap<>();

        niddata.put("txnDescription", "");
        niddata.put("txnstatus", "");
        String NID_KYC_SERVICE_ENDPOINT = xSwitchParameterRepository.findByParamName("NID_KYC_SERVICE_ENDPOINT").get().getParamValue();
        System.out.println("~~~~~~~~~~~~Start NIDs RWANDA Request~~~~~~~~~~~~~~~~");
        System.out.println("~~~~ NIDS DOCUMENT NUMBER : " + documentNo);
        String reqxml = bootstrapNIDPayload(documentNo);
        HttpURLConnection con = null;
        DataOutputStream wr = null;
        BufferedReader in = null;
        int responseCode = 0;

        String muReadTimeout = "10"; // set in configs
        String muConnectTimeout = "30"; //set in configs

        try {
            URL obj = new URL(NID_KYC_SERVICE_ENDPOINT);
            con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "text/xml");
            con.setUseCaches(false);
            con.setDoInput(true);
            con.setConnectTimeout(Integer.parseInt(muReadTimeout) * 1000);
            con.setReadTimeout(Integer.parseInt(muConnectTimeout) * 1000);
            con.setDefaultUseCaches(false);
            con.setDoOutput(true);

            wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(reqxml);
            wr.flush();
            responseCode = con.getResponseCode();
            System.out.println(" \n NIDS ws Response status Code(HTTP) : "
                    + responseCode);
            // if (String.valueOf(responseCode).trim().equalsIgnoreCase("200")) {
            in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            StringBuilder responseBuffer = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                responseBuffer.append(inputLine);
            }
            in.close();
            wr.close();
            con.disconnect();
            String getdocumentRes = responseBuffer.toString();
            //  System.out.println("~~~~~~~~~~~~  AuthenticateDocumentResponse  Response  ~~~~~~~~~~~~ \n"
            //        + getdocumentRes);

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new InputSource(new StringReader(getdocumentRes)));
            doc.getDocumentElement().normalize();
            NodeList statusMessages = doc
                    .getElementsByTagName("AuthenticateDocumentResponse");
            Element statusMessagesElement = (Element) statusMessages
                    .item(0);
            NodeList statusMessage = statusMessagesElement
                    .getElementsByTagName("AuthenticateDocumentResult");
            Element statusMessageElement = (Element) statusMessage.item(0);

            //applicationno
            String applicationnumber = statusMessageElement
                    .getElementsByTagName("ApplicationNumber").item(0)
                    .getTextContent().trim();

            //cell
            String cell = statusMessageElement
                    .getElementsByTagName("Cell").item(0)
                    .getTextContent().trim();

            //cell
            String civilStatus = statusMessageElement
                    .getElementsByTagName("CivilStatus").item(0)
                    .getTextContent().trim();

            //dateOfBirth
            String dateOfBirth = statusMessageElement
                    .getElementsByTagName("DateOfBirth").item(0)
                    .getTextContent().trim();
            //dateOfExpiry
            String dateOfExpiry = statusMessageElement
                    .getElementsByTagName("DateOfExpiry").item(0)
                    .getTextContent().trim();
            //dateOfIssue
            String dateOfIssue = statusMessageElement
                    .getElementsByTagName("DateOfIssue").item(0)
                    .getTextContent().trim();

            //district
            String district = statusMessageElement
                    .getElementsByTagName("District").item(0)
                    .getTextContent().trim();

            //documentNumber
            String documentNumber = statusMessageElement
                    .getElementsByTagName("DocumentNumber").item(0)
                    .getTextContent().trim();
            niddata.put("DocumentNumber", documentNumber);
            System.out.println("Returned Document Number " + documentNumber);

            //DocumentType
            String documentType = statusMessageElement
                    .getElementsByTagName("DocumentType").item(0)
                    .getTextContent().trim();

            //fatherNames
            String fatherNames = statusMessageElement
                    .getElementsByTagName("FatherNames").item(0)
                    .getTextContent().trim();

            //foreName
            String foreName = statusMessageElement
                    .getElementsByTagName("ForeName").item(0)
                    .getTextContent().trim();

            niddata.put("ForeName", foreName);

            //id
            String id = statusMessageElement
                    .getElementsByTagName("Id").item(0)
                    .getTextContent().trim();
            //issueNumber
            String issueNumber = statusMessageElement
                    .getElementsByTagName("IssueNumber").item(0)
                    .getTextContent().trim();

            //motherNames
            String motherNames = statusMessageElement
                    .getElementsByTagName("MotherNames").item(0)
                    .getTextContent().trim();
            //nationality
            String nationality = statusMessageElement
                    .getElementsByTagName("Nationality").item(0)
                    .getTextContent().trim();
            //photo
            String photo = statusMessageElement
                    .getElementsByTagName("Photo").item(0)
                    .getTextContent().trim();
            //placeOfBirth
            String placeOfBirth = statusMessageElement
                    .getElementsByTagName("PlaceOfBirth").item(0)
                    .getTextContent().trim();
            //placeOfIssue
            String placeOfIssue = statusMessageElement
                    .getElementsByTagName("PlaceOfIssue").item(0)
                    .getTextContent().trim();

            //province
            String province = statusMessageElement
                    .getElementsByTagName("Province").item(0)
                    .getTextContent().trim();
            //sector
            String sector = statusMessageElement
                    .getElementsByTagName("Sector").item(0)
                    .getTextContent().trim();

            //sex
            String sex = statusMessageElement
                    .getElementsByTagName("Sex").item(0)
                    .getTextContent().trim();
            //signature
            String signature = statusMessageElement
                    .getElementsByTagName("Signature").item(0)
                    .getTextContent().trim();

            //spouse
            String spouse = statusMessageElement
                    .getElementsByTagName("Spouse").item(0)
                    .getTextContent().trim();
            //Status
            String status = statusMessageElement
                    .getElementsByTagName("Status").item(0)
                    .getTextContent().trim();

            //Surnames
            String surnames = statusMessageElement
                    .getElementsByTagName("Surnames").item(0)
                    .getTextContent().trim();
            niddata.put("Surnames", surnames);

            //TimeSubmitted
            String timeSubmitted = statusMessageElement
                    .getElementsByTagName("TimeSubmitted").item(0)
                    .getTextContent().trim();

            //Village
            String village = statusMessageElement
                    .getElementsByTagName("Village").item(0)
                    .getTextContent().trim();

            //VillageID
            String villageID = statusMessageElement
                    .getElementsByTagName("VillageID").item(0)
                    .getTextContent().trim();

            /***   System.out.println("Applicationnumber : " + applicationnumber);
             System.out.println("Cell : " + cell);
             System.out.println("CivilStatus : " + civilStatus);
             System.out.println("DateOfBirth : " + dateOfBirth);
             System.out.println("DateOfExpiry : " + dateOfExpiry);
             System.out.println("DateOfIssue : " + dateOfIssue);
             System.out.println("District : " + district); **/
            System.out.println("DocumentNumber : " + documentNumber);
            System.out.println("DocumentType : " + documentType);
            System.out.println("IssueNumber : " + issueNumber);
            System.out.println("Nationality : " + nationality);
            System.out.println("PlaceOfIssue : " + placeOfIssue);
            System.out.println("Sex : " + sex);
            System.out.println("Status : " + status);
            System.out.println("TimeSubmitted : " + timeSubmitted);
            niddata.put("txnstatus", "OK");
        } catch (MalformedURLException | SAXException | ParserConfigurationException ex) {
            ex.printStackTrace();
            niddata.put("txnDescription", "Error processing transaction. Try later or call BPR Customer Agency Desk");
            niddata.put("txnstatus", "ERR");
        } catch (IOException ex) {
            ex.printStackTrace();
            niddata.put("txnDescription", "System Error During Validation. Try later or call BPR Customer Agency Desk");
            niddata.put("txnstatus", "ERR");
        }
        // return ""; // In real app return IsoMsg
        System.out.printf("\n ~~~~~~~~   End NID Request  for ID : %s ~~~~~~~~~~~~ ", documentNo);

        return niddata;
    }

    public String bootstrapNIDPayload(String IDDocumentNo) {
        String NID_KYC_SERVICE_BANKCODE = xSwitchParameterRepository.findByParamName("NID_KYC_SERVICE_BANKCODE").get().getParamValue();
        System.out.println("~~~~ NIDS BANKCODE : " + NID_KYC_SERVICE_BANKCODE);
        return "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                + "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\n"
                + "  <soap12:Body>\n"
                + "    <AuthenticateDocument xmlns=\"http://tempuri.org/\">\n"
                + "      <documentNumber>" + IDDocumentNo + "</documentNumber>\n"
                + "      <BankCode>" + NID_KYC_SERVICE_BANKCODE + "</BankCode>\n"
                + "    </AuthenticateDocument>\n"
                + "  </soap12:Body>\n"
                + "</soap12:Envelope>";
    }


}
