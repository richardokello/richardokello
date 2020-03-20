
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
import ke.tra.com.tsync.wrappers.countydevicesresponse.CountyIds_;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class AttendantIds {

    @JsonProperty("action")
    private String action;
    @JsonProperty("actionStatus")
    private String actionStatus;
    @JsonProperty("agentId")
    private String agentId;
    @JsonProperty("countyId")
    private String countyId;
    @JsonProperty("countyIds")
    private CountyIds_ countyIds;
    @JsonProperty("creationDate")
    private String creationDate;
    @JsonProperty("email")
    private String email;
    @JsonProperty("fullName")
    private String fullName;
    @JsonProperty("gender")
    private Integer gender;
    @JsonProperty("genders")
    private Genders genders;
    @JsonProperty("intrash")
    private String intrash;
    @JsonProperty("phoneNumber")
    private String phoneNumber;
    @JsonProperty("sbmPosWorkgroup")
    private Integer sbmPosWorkgroup;
    @JsonProperty("status")
    private Integer status;
    @JsonProperty("userId")
    private Integer userId;
    @JsonProperty("zoneList")
    private List<Integer> zoneList = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

}
