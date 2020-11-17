package ke.tra.com.tsync.wrappers.revenueStream;


import com.fasterxml.jackson.annotation.JsonProperty;
import ke.tra.com.tsync.wrappers.DataWrapper;
import lombok.Data;
import java.sql.Timestamp;

@Data
public class RevenueStreamItem {

    @JsonProperty("code")
    private String Code;
    @JsonProperty("message")
    private String message;
    @JsonProperty("data")
    private DataWrapper data;
    @JsonProperty("timestamp")
    private Timestamp timestamp;

}
