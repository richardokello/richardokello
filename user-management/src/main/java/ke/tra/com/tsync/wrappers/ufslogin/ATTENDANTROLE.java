
package ke.tra.com.tsync.wrappers.ufslogin;

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
    "name",
    "description",
    "action",
    "actionStatus",
    "createdAt",
    "intrash"
})
public class ATTENDANTROLE {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("description")
    private String description;
    @JsonProperty("action")
    private String action;
    @JsonProperty("actionStatus")
    private String actionStatus;
    @JsonProperty("createdAt")
    private BigInteger createdAt;
    @JsonProperty("intrash")
    private String intrash;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public ATTENDANTROLE() {
    }

    /**
     * 
     * @param createdAt
     * @param actionStatus
     * @param name
     * @param intrash
     * @param description
     * @param action
     * @param id
     */
    public ATTENDANTROLE(Integer id, String name, String description, String action, String actionStatus, BigInteger createdAt, String intrash) {
        super();
        this.id = id;
        this.name = name;
        this.description = description;
        this.action = action;
        this.actionStatus = actionStatus;
        this.createdAt = createdAt;
        this.intrash = intrash;
    }

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    public ATTENDANTROLE withId(Integer id) {
        this.id = id;
        return this;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    public ATTENDANTROLE withName(String name) {
        this.name = name;
        return this;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    public ATTENDANTROLE withDescription(String description) {
        this.description = description;
        return this;
    }

    @JsonProperty("action")
    public String getAction() {
        return action;
    }

    @JsonProperty("action")
    public void setAction(String action) {
        this.action = action;
    }

    public ATTENDANTROLE withAction(String action) {
        this.action = action;
        return this;
    }

    @JsonProperty("actionStatus")
    public String getActionStatus() {
        return actionStatus;
    }

    @JsonProperty("actionStatus")
    public void setActionStatus(String actionStatus) {
        this.actionStatus = actionStatus;
    }

    public ATTENDANTROLE withActionStatus(String actionStatus) {
        this.actionStatus = actionStatus;
        return this;
    }

    @JsonProperty("createdAt")
    public BigInteger getCreatedAt() {
        return createdAt;
    }

    @JsonProperty("createdAt")
    public void setCreatedAt(BigInteger createdAt) {
        this.createdAt = createdAt;
    }

    public ATTENDANTROLE withCreatedAt(BigInteger createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    @JsonProperty("intrash")
    public String getIntrash() {
        return intrash;
    }

    @JsonProperty("intrash")
    public void setIntrash(String intrash) {
        this.intrash = intrash;
    }

    public ATTENDANTROLE withIntrash(String intrash) {
        this.intrash = intrash;
        return this;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public ATTENDANTROLE withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("name", name).append("description", description).append("action", action).append("actionStatus", actionStatus).append("createdAt", createdAt).append("intrash", intrash).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(createdAt).append(actionStatus).append(name).append(intrash).append(description).append(action).append(id).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ATTENDANTROLE) == false) {
            return false;
        }
        ATTENDANTROLE rhs = ((ATTENDANTROLE) other);
        return new EqualsBuilder().append(createdAt, rhs.createdAt).append(actionStatus, rhs.actionStatus).append(name, rhs.name).append(intrash, rhs.intrash).append(description, rhs.description).append(action, rhs.action).append(id, rhs.id).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
