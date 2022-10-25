package co.ke.tracom.bprgateway.web.accountopening.service;

import co.ke.tracom.bprgateway.web.accountopening.dto.request.NIDValidationRequest;
import co.ke.tracom.bprgateway.web.accountopening.dto.response.NIDData;
import co.ke.tracom.bprgateway.web.accountopening.dto.response.NIDValidationResponse;
import co.ke.tracom.bprgateway.web.switchparameters.entities.XSwitchParameter;
import co.ke.tracom.bprgateway.web.switchparameters.repository.XSwitchParameterRepository;
import co.ke.tracom.bprgateway.web.util.services.BaseServiceProcessor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
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

@Slf4j
@RequiredArgsConstructor
@Service
public class NIDValidationService {

    private final BaseServiceProcessor baseServiceProcessor;
    private final XSwitchParameterRepository xSwitchParameterRepository;

    @SneakyThrows
    public NIDValidationResponse validateNationalID(NIDValidationRequest request, String referenceNo) {
 baseServiceProcessor.authenticateAgentUsernamePassword(request.getCredentials());

        String returneddocumentid;

        String sendernationalid = request.getNationalID();
        String typeofid = request.getIDType();
        String othertypes=request.getIDType();
        String frmattednid;

        if(typeofid.equals("1"))
        {
          prepareAndSendRequest(othertypes);
            NIDData data=new NIDData();
            return NIDValidationResponse
                    .builder()
                    .status("00")
                    .message("The document number verified successfully. ")
                    .data(data)
                    .build();
        }
        if (typeofid.equals("0")) {
            HashMap<String, String> validationResults  = prepareAndSendRequest(sendernationalid);

            if (validationResults.get("txnstatus").equalsIgnoreCase("OK")) {
                returneddocumentid = validationResults.get("DocumentNumber").isEmpty() ? "" : validationResults.get("DocumentNumber");
                frmattednid = returneddocumentid.replaceAll(" ", "").trim();
                System.out.println("Formatted ID : " + frmattednid);
                System.out.println("Returned NID Data ~~ " + validationResults);
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
        XSwitchParameter nid_kyc_service_endpoint = xSwitchParameterRepository.findByParamName("NID_KYC_SERVICE_ENDPOINT").isPresent()?xSwitchParameterRepository.findByParamName("NID_KYC_SERVICE_ENDPOINT").get():null;
        assert nid_kyc_service_endpoint != null;
        String NID_KYC_SERVICE_ENDPOINT = nid_kyc_service_endpoint.getParamValue();
        System.out.println("~~~~~~~~~~~~Start NIDs RWANDA Request~~~~~~~~~~~~~~~~");
        System.out.println("~~~~ NIDS DOCUMENT NUMBER : " + documentNo);
        String reqxml = bootstrapNIDPayload(documentNo);
      //  HttpURLConnection con;

      //  BufferedReader in;
        int responseCode;

        String muReadTimeout = "10"; // set in configs
        String muConnectTimeout = "30"; //set in configs

        try {
            URL obj = new URL(NID_KYC_SERVICE_ENDPOINT);
            HttpURLConnection con = (HttpURLConnection)obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "text/xml");
            con.setUseCaches(false);
            con.setDoInput(true);
            con.setConnectTimeout(Integer.parseInt(muReadTimeout) * 1000);
            con.setReadTimeout(Integer.parseInt(muConnectTimeout) * 1000);
            con.setDefaultUseCaches(false);
            con.setDoOutput(true);
            DataOutputStream  wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(reqxml);
            wr.flush();
            responseCode = con.getResponseCode();
            System.out.println(" \n NIDS ws Response status Code(HTTP) : "
                    + responseCode);

            InputStream stream = con.getErrorStream();
            if(stream==null) {
                con.getInputStream();
            }else {con.getErrorStream();}
            // if (String.valueOf(responseCode).trim().equalsIgnoreCase("200")) {
            BufferedReader   in = new BufferedReader(new InputStreamReader(
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



            //applicationno
//            String applicationnumber = statusMessageElement
//                    .getElementsByTagName("ApplicationNumber").item(0)
//                    .getTextContent().trim();

            //cell

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
        String NID_KYC_SERVICE_BANKCODE = xSwitchParameterRepository.findByParamName("NID_KYC_SERVICE_BANKCODE").isPresent()?xSwitchParameterRepository.findByParamName("NID_KYC_SERVICE_BANKCODE").get().getParamValue():null;
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
