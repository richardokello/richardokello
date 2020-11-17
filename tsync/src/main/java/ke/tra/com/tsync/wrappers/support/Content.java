
package ke.tra.com.tsync.wrappers.support;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import ke.tra.com.tsync.wrappers.countydevicesresponse.CountyIds;
import lombok.Data;
import lombok.NonNull;
import lombok.ToString;


@Data @ToString @NonNull
public class Content {

    @JsonProperty("action")
    private String action;
    @JsonProperty("actionStatus")
    private String actionStatus;
    @JsonProperty("complaintName")
    private String complaintName;
    @JsonProperty("countyId")
    private String countyId;
    @JsonProperty("countyIds")
    private CountyIds countyIds;
    @JsonProperty("createdAt")
    private String createdAt;
    @JsonProperty("details")
    private String details;
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("intrash")
    private String intrash;
    @JsonProperty("location")
    private String location;
    @JsonProperty("mid")
    private String mid;
    @JsonProperty("natureId")
    private Integer natureId;
    @JsonProperty("natureIds")
    private NatureIds natureIds;
    @JsonProperty("occuranceTime")
    private String occuranceTime;
    @JsonProperty("refCode")
    private String refCode;
    @JsonProperty("remedialAction")
    private String remedialAction;
    @JsonProperty("sbmFeedbackSupportMapList")
    private List<SbmFeedbackSupportMapList> sbmFeedbackSupportMapList = null;
    @JsonProperty("supervisorId")
    private Integer supervisorId;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
}
