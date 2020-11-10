
package ke.tra.com.tsync.wrappers.countydevicesresponse;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "levelName",
    "levelNo",
    "isRootTenant",
    "action",
    "actionStatus",
    "intrash",
    "countyId",
    "countyIds"
})
public class LevelId {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("levelName")
    private String levelName;
    @JsonProperty("levelNo")
    private Integer levelNo;
    @JsonProperty("isRootTenant")
    private Integer isRootTenant;
    @JsonProperty("action")
    private String action;
    @JsonProperty("actionStatus")
    private String actionStatus;
    @JsonProperty("intrash")
    private String intrash;
    @JsonProperty("countyId")
    private Integer countyId;
    @JsonProperty("countyIds")
    private CountyIds_ countyIds;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("levelName")
    public String getLevelName() {
        return levelName;
    }

    @JsonProperty("levelName")
    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    @JsonProperty("levelNo")
    public Integer getLevelNo() {
        return levelNo;
    }

    @JsonProperty("levelNo")
    public void setLevelNo(Integer levelNo) {
        this.levelNo = levelNo;
    }

    @JsonProperty("isRootTenant")
    public Integer getIsRootTenant() {
        return isRootTenant;
    }

    @JsonProperty("isRootTenant")
    public void setIsRootTenant(Integer isRootTenant) {
        this.isRootTenant = isRootTenant;
    }

    @JsonProperty("action")
    public String getAction() {
        return action;
    }

    @JsonProperty("action")
    public void setAction(String action) {
        this.action = action;
    }

    @JsonProperty("actionStatus")
    public String getActionStatus() {
        return actionStatus;
    }

    @JsonProperty("actionStatus")
    public void setActionStatus(String actionStatus) {
        this.actionStatus = actionStatus;
    }

    @JsonProperty("intrash")
    public String getIntrash() {
        return intrash;
    }

    @JsonProperty("intrash")
    public void setIntrash(String intrash) {
        this.intrash = intrash;
    }

    @JsonProperty("countyId")
    public Integer getCountyId() {
        return countyId;
    }

    @JsonProperty("countyId")
    public void setCountyId(Integer countyId) {
        this.countyId = countyId;
    }

    @JsonProperty("countyIds")
    public CountyIds_ getCountyIds() {
        return countyIds;
    }

    @JsonProperty("countyIds")
    public void setCountyIds(CountyIds_ countyIds) {
        this.countyIds = countyIds;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("levelName", levelName).append("levelNo", levelNo).append("isRootTenant", isRootTenant).append("action", action).append("actionStatus", actionStatus).append("intrash", intrash).append("countyId", countyId).append("countyIds", countyIds).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(actionStatus).append(countyId).append(intrash).append(action).append(levelName).append(id).append(additionalProperties).append(isRootTenant).append(countyIds).append(levelNo).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof LevelId) == false) {
            return false;
        }
        LevelId rhs = ((LevelId) other);
        return new EqualsBuilder().append(actionStatus, rhs.actionStatus).append(countyId, rhs.countyId).append(intrash, rhs.intrash).append(action, rhs.action).append(levelName, rhs.levelName).append(id, rhs.id).append(additionalProperties, rhs.additionalProperties).append(isRootTenant, rhs.isRootTenant).append(countyIds, rhs.countyIds).append(levelNo, rhs.levelNo).isEquals();
    }

}
