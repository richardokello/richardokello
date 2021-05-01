package co.ke.tracom.bprgateway.web.tigopesa.data.walletPayment;

import co.ke.tracom.bprgateway.core.tracomchannels.xml.XMLRequestI;
import co.ke.tracom.bprgateway.web.tigopesa.data.TigopesaRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WalletPaymentRequest extends TigopesaRequest implements XMLRequestI {

  @ApiModelProperty(name = "Transaction ID", value = "Bank transaction ID", required = true)
  private String transactionId;

  @ApiModelProperty(name = "Bank code", value = "Unique code of bank", required = true)
  private String bankCode;

  private String amount;

  public String getRequestXML() {
    String xmlRequestFormat =
        "<TCSRequest>\n"
            + "<UserName>%s</UserName>\n"
            + "<Password>%s</Password>\n"
            + "<Function name=\"WALLETPAYMENT\">\n"
            + "<msisdn>%s</msisdn>\n"
            + "<amount>%s</amount>\n"
            + "<transactionId>%s</ transactionId >\n"
            + "<bankCode>%s</ bankCode>\n"
            + "</Function>\n"
            + "</TCSRequest>";

    return String.format(
        xmlRequestFormat,
        this.getUsername(),
        this.getPassword(),
        this.getMsisdn(),
        this.getAmount(),
        this.getTransactionId(),
        this.getBankCode());
  }
}
