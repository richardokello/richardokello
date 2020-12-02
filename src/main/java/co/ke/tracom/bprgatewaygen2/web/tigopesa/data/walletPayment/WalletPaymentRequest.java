package co.ke.tracom.bprgatewaygen2.web.tigopesa.data.walletPayment;

import co.ke.tracom.bprgatewaygen2.web.tigopesa.data.TigopesaRequest;
import lombok.Data;

@Data
public class WalletPaymentRequest extends TigopesaRequest {
    private String transactionId;
    private String bankCode;
    private String amount;

    public String getRequestXML () {
        String xmlRequestFormat = "<TCSRequest>\n" +
                "<UserName>%s</UserName>\n" +
                "<Password>%s</Password>\n" +
                "<Function name=\"WALLETPAYMENT\">\n" +
                "<msisdn>%s</msisdn>\n" +
                "<amount>%s</amount>\n" +
                "<transactionId>%s</ transactionId >\n" +
                "<bankCode>%s</ bankCode>\n" +
                "</Function>\n" +
                "</TCSRequest>";

        return String.format(xmlRequestFormat,
                this.getUsername(),
                this.getPassword(),
                this.getMsisdn(),
                this.getAmount(),
                this.getTransactionId(),
                this.getBankCode());
    }
}
