package co.ke.tracom.bprgatewaygen2.web.tigopesa.data.checkBalance;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name="TCSReply")
@XmlAccessorType(XmlAccessType.NONE)
public class CheckBalanceResponse {
    @XmlAttribute(name="Result")
    private int errorCode;
    @XmlAttribute(name="Message")
    private String errorMessage;
    @XmlAttribute(name="referenceId")
    private String referenceId;
    @XmlAttribute(name="amount")
    private String amount;
}
