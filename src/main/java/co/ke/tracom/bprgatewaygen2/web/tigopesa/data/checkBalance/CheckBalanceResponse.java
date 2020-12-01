package co.ke.tracom.bprgatewaygen2.web.tigopesa.data.checkBalance;

import lombok.Data;

@Data
public class CheckBalanceResponse {
    private String errorCode;
    private String errorMessage;
    private String amount;
    private String referenceId;

    public String generateRequestXML () {

        String xmlMessage = "<TCSReply>\n" +
                "<Result>%s</Result>\n" +
                "<Message>%s</Message>\n" +
                "<amount>%s</amount>\n" +
                "<referenceId>%s</referenceId>\n" +
                "</TCSReply>";

        return String.format(xmlMessage,
                this.getErrorCode(),
                this.getErrorMessage(),
                this.getAmount(),
                this.getReferenceId());
    }
}
