
package ke.tra.com.tsync.wrappers.support;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@Data
public class SbmFeedbackSupportMapList {

    @JsonProperty("feedbackId")
    private Integer feedbackId;
    @JsonProperty("feedbackIds")
    private FeedbackIds feedbackIds;
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("supportId")
    private Integer supportId;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

}
