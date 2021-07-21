package co.ke.tracom.bprgateway.util;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.StringReader;

public class XMLParser {

    @Test
    public void parseIZICashError() throws Exception {

        String respons = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><soapenv:Envelope xmlns:soapenv=\"http://www.w3.org/2003/05/soap-envelope\"><soapenv:Header></soapenv:Header><soapenv:Body><ns:processWebServiceReqResponse xmlns:ns=\"http://everest\"><ns:return>43|Not Valid Mobile Number|CDA79RPRAULA|T210709142858347137</ns:return></ns:processWebServiceReqResponse></soapenv:Body></soapenv:Envelope>";

        String response = respons.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>","");

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(new InputSource(new StringReader(response)));
        doc.getDocumentElement().normalize();

        NodeList childNodes = doc.getChildNodes();
        Node envelope = childNodes.item(0);
        Node body = envelope.getChildNodes().item(1);
        Node processWebServiceReq = body.getChildNodes().item(0);
        Node req = processWebServiceReq.getChildNodes().item(0);
        System.out.println("Element: " + req.getTextContent().split("\\|")[1]);


    }
}
