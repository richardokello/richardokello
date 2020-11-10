
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
public class Genders {

    @JsonProperty("action")
    private String action;
    @JsonProperty("actionStatus")
    private String actionStatus;
    @JsonProperty("creationDate")
    private String creationDate;
    @JsonProperty("description")
    private String description;
    @JsonProperty("gender")
    private String gender;
    @JsonProperty("genderId")
    private Integer genderId;
    @JsonProperty("intrash")
    private String intrash;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

}
