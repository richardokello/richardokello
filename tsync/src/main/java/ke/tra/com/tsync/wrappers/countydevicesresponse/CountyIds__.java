
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
    "name",
    "countyCode",
    "action",
    "actionStatus",
    "intrash",
    "id",
    "creationDate"
})
public class CountyIds__ {

    @JsonProperty("name")
    private String name;
    @JsonProperty("countyCode")
    private String countyCode;
    @JsonProperty("action")
    private String action;
    @JsonProperty("actionStatus")
    private String actionStatus;
    @JsonProperty("intrash")
    private String intrash;
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("creationDate")
    private BigInteger creationDate;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("countyCode")
    public String getCountyCode() {
        return countyCode;
    }

    @JsonProperty("countyCode")
    public void setCountyCode(String countyCode) {
        this.countyCode = countyCode;
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

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("creationDate")
    public BigInteger getCreationDate() {
        return creationDate;
    }

    @JsonProperty("creationDate")
    public void setCreationDate(BigInteger creationDate) {
        this.creationDate = creationDate;
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
        return new ToStringBuilder(this).append("name", name).append("countyCode", countyCode).append("action", action).append("actionStatus", actionStatus).append("intrash", intrash).append("id", id).append("creationDate", creationDate).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(countyCode).append(actionStatus).append(name).append(intrash).append(action).append(id).append(additionalProperties).append(creationDate).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof CountyIds__) == false) {
            return false;
        }
        CountyIds__ rhs = ((CountyIds__) other);
        return new EqualsBuilder().append(countyCode, rhs.countyCode).append(actionStatus, rhs.actionStatus).append(name, rhs.name).append(intrash, rhs.intrash).append(action, rhs.action).append(id, rhs.id).append(additionalProperties, rhs.additionalProperties).append(creationDate, rhs.creationDate).isEquals();
    }

}
