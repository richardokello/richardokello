package co.ke.tracom.bprgateway.web.tigopesa.data.transactionStatus;

import co.ke.tracom.bprgateway.core.tracomchannels.xml.XMLRequestI;
import co.ke.tracom.bprgateway.web.tigopesa.data.TigopesaRequest;
import lombok.Data;

@Data
public class TransactionStatusRequest extends TigopesaRequest implements XMLRequestI {

  public String getRequestXML() {
    String xmlMessage =
        "<TCSRequest>\n"
            + "<UserName>%s</UserName>"
            + "<Password>%s</Password>\n"
            + "<Function name=\"TRANSACTIONSTATUS\">\n"
            + "<msisdn>%s</msisdn>\n"
            + "<transactionId>%s</transactionId>\n"
            + "<billNumber>%s</billNumber>\n"
            + "<companyCode>%s</ companyCode >\n"
            + "</Function>\n"
            + "</TCSRequest>";

    return String.format(
        xmlMessage,
        this.getUsername(),
        this.getPassword(),
        this.getMsisdn(),
        this.getTransactionId(),
        this.getBillNumber(),
        this.getCompanyCode());
  }
}
