package co.ke.tracom.bprgatewaygen2.web.tigopesa.data.transactionStatus;

import co.ke.tracom.bprgatewaygen2.web.tigopesa.data.TigopesaResponse;
import lombok.Data;

@Data
public class TransactionStatusResponse extends TigopesaResponse {

    public String getResponseXML () {
        String xmlResponseFormat = "<TCSReply>\n" +
                "<Result>%s</Result>\n" +
                "<Message>%s</Message>\n" +
                "<transactionId>%s</transactionId>\n" +
                "<referenceId>%s</ referenceId >\n" +
                "<status>%s</status>\n" +
                "<voucherCode>%s</voucherCode>\n" +
                "<message>%s</message>\n" +
                "</TCSReply>";

        return String.format(xmlResponseFormat,
                this.getErrorCode(),
                this.getErrorMessage(),
                this.getTransactionId(),
                this.getReferenceId(),
                this.getStatus(),
                this.getVoucherCode(),
                this.getMessage());
    }
}

