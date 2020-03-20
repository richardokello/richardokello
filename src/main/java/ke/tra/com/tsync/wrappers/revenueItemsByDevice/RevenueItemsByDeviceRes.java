
package ke.tra.com.tsync.wrappers.revenueItemsByDevice;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;

@JsonInclude(JsonInclude.Include.NON_NULL)
@lombok.Data
@ToString
public class RevenueItemsByDeviceRes {

    @JsonProperty("code")
    private Integer code;
    @JsonProperty("message")
    private String message;
    @JsonProperty("data")
    private RevenueItemsData data;
    @JsonProperty("timestamp")
    private Timestamp timestamp;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

}
