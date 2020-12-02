package co.ke.tracom.bprgatewaygen2.web.tigopesa.data.payment;

import co.ke.tracom.bprgatewaygen2.web.tigopesa.data.TigopesaRequest;
import lombok.Data;

@Data
public class BillPaymentRequest extends TigopesaRequest {
    private float amount;
    private String transactionId;

    public String getRequestXML () {
        String xmlRequestFormat = "<TCSRequest>\n" +
                "<UserName>%s</UserName>\n" +
                "<Password>%s</Password>\n" +
                "<Function name=\"PAYMENT\">\n" +
                "<msisdn>%s</msisdn>\n" +
                "<amount>%s</amount>\n" +
                "<transactionId>%s</ transactionId >\n" +
                "<billNumber>%s</billNumber>\n" +
                "<companyCode>%s</ companyCode>\n" +
                "</Function>\n" +
                "</TCSRequest>";

        return String.format(xmlRequestFormat,
                this.getUsername(),
                this.getPassword(),
                this.getMsisdn(),
                this.getAmount(),
                this.getTransactionId(),
                this.getBillNumber(),
                this.getCompanyCode());
    }
}
