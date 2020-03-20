
package ke.tra.com.tsync.wrappers.support;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FeedbackIds {

    @JsonProperty("action")
    private String action;
    @JsonProperty("actionStatus")
    private String actionStatus;
    @JsonProperty("attendantId")
    private Integer attendantId;
    @JsonProperty("attendantIds")
    private AttendantIds attendantIds;
    @JsonProperty("createdAt")
    private String createdAt;
    @JsonProperty("feedback")
    private String feedback;
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("intrash")
    private String intrash;
    @JsonProperty("supervisorId")
    private Integer supervisorId;
    @JsonProperty("supportId")
    private Integer supportId;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

}
