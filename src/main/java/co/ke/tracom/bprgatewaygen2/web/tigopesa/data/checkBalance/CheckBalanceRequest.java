package co.ke.tracom.bprgatewaygen2.web.tigopesa.data.checkBalance;

import co.ke.tracom.bprgatewaygen2.web.tigopesa.data.XMLRequestI;
import co.ke.tracom.bprgatewaygen2.web.tigopesa.data.TigopesaRequest;
import lombok.Data;

@Data
public class CheckBalanceRequest extends TigopesaRequest implements XMLRequestI {

    public String getRequestXML() {
        String xmlMessage = "<TCSRequest>\n" +
                "<UserName>%s</UserName>\n" +
                "<Password>%s</Password>\n" +
                "<Function name=\"CHECKBALANCE\">\n" +
                "<msisdn>%s</ msisdn >" +
                "<billNumber>%s</billNumber>\n" +
                "<companyCode>%s</companyCode>\n" +
                "</Function>\n" +
                "</TCSRequest>";
        return String.format(xmlMessage,
                this.getUsername(),
                this.getPassword(),
                this.getMsisdn(),
                this.getBillNumber(),
                this.getCompanyCode()
                );
    }
}
