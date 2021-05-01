package co.ke.tracom.bprgateway;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

public class XMLSoap {


//  String response =
//          "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
//                  + "<S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\">\n"
//                  + "   <S:Body>\n"
//                  + "      <ns4:CustomerOnboardingNFSResponse \n"
//                  + "      xmlns:ns4=\"http://temenos.com/NfsCustomerOnboard\" xmlns:ns2=\"http://temenos.com/CUSTOMERCREATE\" xmlns:ns3=\"http://temenos.com/CUSTOMER\">\n"
//                  + "         <Status>\n"
//                  + "            <transactionId>100028187</transactionId>\n"
//                  + "            <messageId>TWS210699427258985.00</messageId>\n"
//                  + "            <successIndicator>Success</successIndicator>\n"
//                  + "            <application>CUSTOMER</application>\n"
//                  + "         </Status>\n"
//                  + "         <CUSTOMERType id=\"100028187\">\n"
//                  + "            <ns3:MNEMONIC>ANT.800101</ns3:MNEMONIC>\n"
//                  + "            <ns3:gSHORTNAME>\n"
//                  + "               <ns3:SHORTNAME>ANTHONY</ns3:SHORTNAME>\n"
//                  + "            </ns3:gSHORTNAME>\n"
//                  + "            <ns3:gNAME1>\n"
//                  + "               <ns3:NAME1>KAMUNDIA</ns3:NAME1>\n"
//                  + "            </ns3:gNAME1>\n"
//                  + "            <ns3:gNAME2>\n"
//                  + "               <ns3:NAME2>WANJAU</ns3:NAME2>\n"
//                  + "            </ns3:gNAME2>\n"
//                  + "            <ns3:SECTOR>1001</ns3:SECTOR>\n"
//                  + "            <ns3:ACCOUNTOFFICER>200</ns3:ACCOUNTOFFICER>\n"
//                  + "            <ns3:INDUSTRY>8000</ns3:INDUSTRY>\n"
//                  + "            <ns3:TARGET>99</ns3:TARGET>\n"
//                  + "            <ns3:NATIONALITY>KE</ns3:NATIONALITY>\n"
//                  + "            <ns3:CUSTOMERSTATUS>69</ns3:CUSTOMERSTATUS>\n"
//                  + "            <ns3:RESIDENCE>KE</ns3:RESIDENCE>\n"
//                  + "            <ns3:gLEGALID>\n"
//                  + "               <ns3:mLEGALID>\n"
//                  + "                  <ns3:LEGALID>123456987</ns3:LEGALID>\n"
//                  + "               </ns3:mLEGALID>\n"
//                  + "            </ns3:gLEGALID>\n"
//                  + "            <ns3:BIRTHINCORPDATE>19800101</ns3:BIRTHINCORPDATE>\n"
//                  + "            <ns3:LANGUAGE>1</ns3:LANGUAGE>\n"
//                  + "            <ns3:COMPANYBOOK>KE0010003</ns3:COMPANYBOOK>\n"
//                  + "            <ns3:CLSCPARTY>NO</ns3:CLSCPARTY>\n"
//                  + "            <ns3:GENDER>MALE</ns3:GENDER>\n"
//                  + "            <ns3:MARITALSTATUS>MARRIED</ns3:MARITALSTATUS>\n"
//                  + "            <ns3:gLEGALIDDOCNAME>\n"
//                  + "               <ns3:LEGALIDDOCNAME>123456987-</ns3:LEGALIDDOCNAME>\n"
//                  + "            </ns3:gLEGALIDDOCNAME>\n"
//                  + "            <ns3:AMLCHECK>NULL</ns3:AMLCHECK>\n"
//                  + "            <ns3:AMLRESULT>NULL</ns3:AMLRESULT>\n"
//                  + "            <ns3:INTERNETBANKINGSERVICE>NULL</ns3:INTERNETBANKINGSERVICE>\n"
//                  + "            <ns3:MOBILEBANKINGSERVICE>NULL</ns3:MOBILEBANKINGSERVICE>\n"
//                  + "            <ns3:CURRNO>1</ns3:CURRNO>\n"
//                  + "            <ns3:gINPUTTER>\n"
//                  + "               <ns3:INPUTTER>94272_ATM.SWITCH__OFS_TWS</ns3:INPUTTER>\n"
//                  + "            </ns3:gINPUTTER>\n"
//                  + "            <ns3:gDATETIME>\n"
//                  + "               <ns3:DATETIME>2103101623</ns3:DATETIME>\n"
//                  + "            </ns3:gDATETIME>\n"
//                  + "            <ns3:AUTHORISER>94272_ATM.SWITCH_OFS_TWS</ns3:AUTHORISER>\n"
//                  + "            <ns3:COCODE>KE0010001</ns3:COCODE>\n"
//                  + "            <ns3:DEPTCODE>1</ns3:DEPTCODE>\n"
//                  + "            <ns3:POSTALCODE>002369</ns3:POSTALCODE>\n"
//                  + "            <ns3:gPOBOXNO>\n"
//                  + "               <ns3:POBOXNO>1939</ns3:POBOXNO>\n"
//                  + "            </ns3:gPOBOXNO>\n"
//                  + "            <ns3:TELMOBILE>254722023654</ns3:TELMOBILE>\n"
//                  + "            <ns3:RESIDEYN>Y</ns3:RESIDEYN>\n"
//                  + "            <ns3:EMPLOYEENO>02325895</ns3:EMPLOYEENO>\n"
//                  + "            <ns3:OPENINGDATE>20200123</ns3:OPENINGDATE>\n"
//                  + "            <ns3:CUSTTYPE>I</ns3:CUSTTYPE>\n"
//                  + "            <ns3:IDTYPES>1</ns3:IDTYPES>\n"
//                  + "            <ns3:CLASSIFICATION>1</ns3:CLASSIFICATION>\n"
//                  + "            <ns3:AGE>41</ns3:AGE>\n"
//                  + "            <ns3:EMPLYRSCODE>04</ns3:EMPLYRSCODE>\n"
//                  + "            <ns3:SPNAME>WANJAU</ns3:SPNAME>\n"
//                  + "            <ns3:SPOCCUP>IT</ns3:SPOCCUP>\n"
//                  + "            <ns3:gSPEMPADD>\n"
//                  + "               <ns3:SPEMPADD>1939</ns3:SPEMPADD>\n"
//                  + "            </ns3:gSPEMPADD>\n"
//                  + "            <ns3:MEMBERNUMBER>321456</ns3:MEMBERNUMBER>\n"
//                  + "            <ns3:KRAPIN>A0062384WFGV0</ns3:KRAPIN>\n"
//                  + "            <ns3:gONBIMAGETYPE>\n"
//                  + "               <ns3:mONBIMAGETYPE>\n"
//                  + "                  <ns3:ONBIMAGETYPE>PHOTOS</ns3:ONBIMAGETYPE>\n"
//                  + "                  <ns3:ONBIMAGENAME>image1.jpg</ns3:ONBIMAGENAME>\n"
//                  + "               </ns3:mONBIMAGETYPE>\n"
//                  + "               <ns3:mONBIMAGETYPE>\n"
//                  + "                  <ns3:ONBIMAGETYPE>SIGNATURE</ns3:ONBIMAGETYPE>\n"
//                  + "                  <ns3:ONBIMAGENAME>sign.png</ns3:ONBIMAGENAME>\n"
//                  + "               </ns3:mONBIMAGETYPE>\n"
//                  + "            </ns3:gONBIMAGETYPE>\n"
//                  + "            <ns3:PARENTIDTYPE>ID</ns3:PARENTIDTYPE>\n"
//                  + "            <ns3:PARENTIDNUM>22365849</ns3:PARENTIDNUM>\n"
//                  + "            <ns3:PARENTNAME>ANTHONY</ns3:PARENTNAME>\n"
//                  + "         </CUSTOMERType>\n"
//                  + "      </ns4:CustomerOnboardingNFSResponse>\n"
//                  + "   </S:Body>\n"
//                  + "</S:Envelope>";

  @Test
  public void processNewFortisReq(String response) throws ParserConfigurationException, IOException, SAXException {
    String properxml =
        response.replace("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>", "");

    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    Document doc = dBuilder.parse(new InputSource(new StringReader(properxml)));
    doc.getDocumentElement().normalize();

    Map<String, String> results = new HashMap<>();

    NodeList nodeList = doc.getElementsByTagName("*");
    for (int i = 0; i < nodeList.getLength(); i++) {

      Node node = nodeList.item(i);

      if (node.getNodeType() == Node.ELEMENT_NODE) {
        // do something with the current element
        if (node.getNodeName().equalsIgnoreCase("application")
            || node.getNodeName().equalsIgnoreCase("transactionId")
            || node.getNodeName().equalsIgnoreCase("messageId")
            || node.getNodeName().equalsIgnoreCase("successIndicator")) {
          System.out.print(node.getNodeName());
          System.out.println("=" + node.getTextContent());

          results.put(node.getNodeName(), String.valueOf(node.getTextContent()));
        }
      }
    }

    System.out.println("results.get(\"messageId\") = " + results.get("messageId"));
  }
}
