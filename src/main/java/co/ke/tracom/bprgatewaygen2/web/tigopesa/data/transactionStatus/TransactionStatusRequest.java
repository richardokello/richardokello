package co.ke.tracom.bprgatewaygen2.web.tigopesa.data.transactionStatus;

import co.ke.tracom.bprgatewaygen2.core.tracomhttp.xmlHttp.XMLRequestI;
import co.ke.tracom.bprgatewaygen2.web.tigopesa.data.TigopesaRequest;
import lombok.Data;

@Data
public class TransactionStatusRequest extends TigopesaRequest implements XMLRequestI {
    private String transactionId;

    public String getRequestXML () {
        String xmlMessage = "<TCSRequest>\n" +
                "<UserName>%s</UserName>" +
                "<Password>%s</Password>\n" +
                "<Function name=\"TRANSACTIONSTATUS\">\n" +
                "<msisdn>%s</msisdn>\n" +
                "<transactionId>%s</transactionId>\n" +
                "<billNumber>%s</billNumber>\n" +
                "<companyCode>%s</ companyCode >\n" +
                "</Function>\n" +
                "</TCSRequest>";

        return String.format(xmlMessage,
                this.getUsername(),
                this.getPassword(),
                this.getMsisdn(),
                this.getTransactionId(),
                this.getBillNumber(),
                this.getCompanyCode());
    }
}
