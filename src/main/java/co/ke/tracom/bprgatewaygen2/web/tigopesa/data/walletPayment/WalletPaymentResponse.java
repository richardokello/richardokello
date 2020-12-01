package co.ke.tracom.bprgatewaygen2.web.tigopesa.data.walletPayment;

import co.ke.tracom.bprgatewaygen2.web.tigopesa.data.TigopesaResponse;
import lombok.Data;

@Data
public class WalletPaymentResponse extends TigopesaResponse {

    public String generateResponseXML () {
        String xmlResponseFormat = "<TCSReply>\n" +
                "<Result>%s</Result>\n" +
                "<Message>%s</Message>\n" +
                "<transactionId>%s</transactionId>\n" +
                "<referenceId>%s</ referenceId >\n" +
                "<status>%s</status>\n" +
                "<message>%s</message>\n" +
                "</TCSReply>";

        return String.format(xmlResponseFormat,
                this.getErrorCode(),
                this.getErrorMessage(),
                this.getTransactionId(),
                this.getReferenceId(),
                this.getStatus(),
                this.getMessage());
    }
}

