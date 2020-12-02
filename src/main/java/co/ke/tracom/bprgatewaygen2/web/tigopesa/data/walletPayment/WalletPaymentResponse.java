package co.ke.tracom.bprgatewaygen2.web.tigopesa.data.walletPayment;

import co.ke.tracom.bprgatewaygen2.web.tigopesa.data.TigopesaResponse;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name="TCSReply")
@XmlAccessorType(XmlAccessType.NONE)
public class WalletPaymentResponse {
    @XmlAttribute(name="Result")
    private int errorCode;
    @XmlAttribute(name="Message")
    private String errorMessage;
    @XmlAttribute(name="transactionId")
    private String transactionId;
    @XmlAttribute(name="referenceId")
    private String referenceId;
    @XmlAttribute(name="status")
    private String status;
    @XmlAttribute(name="message")
    private String message;
}


