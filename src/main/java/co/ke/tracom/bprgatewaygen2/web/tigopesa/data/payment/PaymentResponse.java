package co.ke.tracom.bprgatewaygen2.web.tigopesa.data.payment;

import lombok.Data;

@Data
public class PaymentResponse {
    private String errorCode;
    private String errorMessage;
    private String transactionId;
    private String referenceId;
    private String status;
    private int voucherCode;
    private String message;

    public String generateResponseXML () {
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
                errorCode,
                errorMessage,
                transactionId,
                referenceId,
                status,
                voucherCode,
                message);
    }
}
