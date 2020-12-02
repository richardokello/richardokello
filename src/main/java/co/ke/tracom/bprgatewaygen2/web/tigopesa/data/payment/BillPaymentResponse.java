package co.ke.tracom.bprgatewaygen2.web.tigopesa.data.payment;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import org.springframework.data.annotation.Transient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@Data
public class BillPaymentResponse {
    @JsonProperty("Result")
    private String Result; // errorCode

    @JsonProperty("Message")
    private String Message; // error Message

    @JsonProperty("transactionId")
    private String transactionId;

    @JsonProperty("referenceId")
    private String referenceId;

    @JsonProperty("status")
    private String status;

    @JsonProperty("voucherCode")
    private int voucherCode;

    @JsonProperty("message")
    private String message;

    @JsonGetter("Message")
    public String getMessage() {
        return this.Message;
    }

    @JsonSetter("Message")
    public void setMessage(String Message) {
        this.Message = Message;
    }

    @JsonGetter("message")
    public String get_message() {
        return this.message;
    }

    @JsonSetter("message")
    public void set_message(String message) {
        this.message = message;
    }

}
