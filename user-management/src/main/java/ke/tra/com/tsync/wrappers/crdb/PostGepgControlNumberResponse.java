package ke.tra.com.tsync.wrappers.crdb;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.builder.ToStringSummary;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class PostGepgControlNumberResponse {


    @JsonProperty("txnReference")
    private String txnReference;

    @JsonProperty("requestID")
    private String requestID;

    private String owner;
    private String amount;

    @JsonProperty("paymentReference")
    private String paymentReference;

    @JsonProperty("gepgReceipt")
    private String gepgReceipt;

    @JsonProperty("partnerID")
    private String partnerID;

    @JsonProperty("message")
    private String message;

    public String toPosString(){
        return
                      this.txnReference
                +"#"+ this.requestID
                +"#"+ this.owner
                +"#"+ this.amount
                +"#"+ this.paymentReference
                +"#"+ this.gepgReceipt
                +"#"+ this.partnerID ;
    }

}
