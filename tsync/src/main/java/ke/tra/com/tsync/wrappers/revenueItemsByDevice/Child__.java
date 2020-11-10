
package ke.tra.com.tsync.wrappers.revenueItemsByDevice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)


@Data
public class Child__ {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("action")
    private String action;
    @JsonProperty("actionStatus")
    private String actionStatus;
    @JsonProperty("intrash")
    private String intrash;
    @JsonProperty("createdAt")
    private Integer createdAt;
    @JsonProperty("name")
    private String name;
    @JsonProperty("isParent")
    private Integer isParent;
    @JsonProperty("uniqueId")
    private String uniqueId;
    @JsonProperty("children")
    private List<Child> children = null;
    @JsonProperty("parentIds")
    private Integer parentIds;
    @JsonProperty("text")
    private String text;
    @JsonProperty("nickname")
    private Object nickname;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();


}
