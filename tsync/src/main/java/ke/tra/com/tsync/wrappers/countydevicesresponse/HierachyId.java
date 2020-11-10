
package ke.tra.com.tsync.wrappers.countydevicesresponse;

import java.math.BigInteger;
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
    "action",
    "actionStatus",
    "intrash",
    "createdAt",
    "name",
    "isParent",
    "parentId",
    "uniqueId",
    "levelIds",
    "levelId",
    "accId",
    "accIds",
    "countyId",
    "countyIds"
})
public class HierachyId {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("action")
    private String action;
    @JsonProperty("actionStatus")
    private String actionStatus;
    @JsonProperty("intrash")
    private String intrash;
    @JsonProperty("createdAt")
    private BigInteger createdAt;
    @JsonProperty("name")
    private String name;
    @JsonProperty("isParent")
    private Integer isParent;
    @JsonProperty("parentId")
    private Integer parentId;
    @JsonProperty("uniqueId")
    private String uniqueId;
    @JsonProperty("levelIds")
    private Integer levelIds;
    @JsonProperty("levelId")
    private LevelId levelId;
    @JsonProperty("accId")
    private Integer accId;
    @JsonProperty("accIds")
    private AccIds accIds;
    @JsonProperty("countyId")
    private Integer countyId;
    @JsonProperty("countyIds")
    private CountyIds__ countyIds;
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

    @JsonProperty("createdAt")
    public BigInteger getCreatedAt() {
        return createdAt;
    }

    @JsonProperty("createdAt")
    public void setCreatedAt(BigInteger createdAt) {
        this.createdAt = createdAt;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("isParent")
    public Integer getIsParent() {
        return isParent;
    }

    @JsonProperty("isParent")
    public void setIsParent(Integer isParent) {
        this.isParent = isParent;
    }

    @JsonProperty("parentId")
    public Integer getParentId() {
        return parentId;
    }

    @JsonProperty("parentId")
    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    @JsonProperty("uniqueId")
    public String getUniqueId() {
        return uniqueId;
    }

    @JsonProperty("uniqueId")
    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    @JsonProperty("levelIds")
    public Integer getLevelIds() {
        return levelIds;
    }

    @JsonProperty("levelIds")
    public void setLevelIds(Integer levelIds) {
        this.levelIds = levelIds;
    }

    @JsonProperty("levelId")
    public LevelId getLevelId() {
        return levelId;
    }

    @JsonProperty("levelId")
    public void setLevelId(LevelId levelId) {
        this.levelId = levelId;
    }

    @JsonProperty("accId")
    public Integer getAccId() {
        return accId;
    }

    @JsonProperty("accId")
    public void setAccId(Integer accId) {
        this.accId = accId;
    }

    @JsonProperty("accIds")
    public AccIds getAccIds() {
        return accIds;
    }

    @JsonProperty("accIds")
    public void setAccIds(AccIds accIds) {
        this.accIds = accIds;
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
    public CountyIds__ getCountyIds() {
        return countyIds;
    }

    @JsonProperty("countyIds")
    public void setCountyIds(CountyIds__ countyIds) {
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
        return new ToStringBuilder(this).append("id", id).append("action", action).append("actionStatus", actionStatus).append("intrash", intrash).append("createdAt", createdAt).append("name", name).append("isParent", isParent).append("parentId", parentId).append("uniqueId", uniqueId).append("levelIds", levelIds).append("levelId", levelId).append("accId", accId).append("accIds", accIds).append("countyId", countyId).append("countyIds", countyIds).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(isParent).append(levelIds).append(intrash).append(countyIds).append(parentId).append(accIds).append(createdAt).append(actionStatus).append(countyId).append(levelId).append(name).append(action).append(accId).append(id).append(additionalProperties).append(uniqueId).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof HierachyId) == false) {
            return false;
        }
        HierachyId rhs = ((HierachyId) other);
        return new EqualsBuilder().append(isParent, rhs.isParent).append(levelIds, rhs.levelIds).append(intrash, rhs.intrash).append(countyIds, rhs.countyIds).append(parentId, rhs.parentId).append(accIds, rhs.accIds).append(createdAt, rhs.createdAt).append(actionStatus, rhs.actionStatus).append(countyId, rhs.countyId).append(levelId, rhs.levelId).append(name, rhs.name).append(action, rhs.action).append(accId, rhs.accId).append(id, rhs.id).append(additionalProperties, rhs.additionalProperties).append(uniqueId, rhs.uniqueId).isEquals();
    }

}
