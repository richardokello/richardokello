
package ke.tra.com.tsync.wrappers.ufslogin;

import java.util.HashMap;
import java.util.List;
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
    "serialnumber",
    "tidMids"
})
public class DEVICEDETAILS {

    @JsonProperty("serialnumber")
    private String serialnumber;
    @JsonProperty("tidMids")
    private List<TidMid> tidMids = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public DEVICEDETAILS() {
    }

    /**
     * 
     * @param serialnumber
     * @param tidMids
     */
    public DEVICEDETAILS(String serialnumber, List<TidMid> tidMids) {
        super();
        this.serialnumber = serialnumber;
        this.tidMids = tidMids;
    }

    @JsonProperty("serialnumber")
    public String getSerialnumber() {
        return serialnumber;
    }

    @JsonProperty("serialnumber")
    public void setSerialnumber(String serialnumber) {
        this.serialnumber = serialnumber;
    }

    public DEVICEDETAILS withSerialnumber(String serialnumber) {
        this.serialnumber = serialnumber;
        return this;
    }

    @JsonProperty("tidMids")
    public List<TidMid> getTidMids() {
        return tidMids;
    }

    @JsonProperty("tidMids")
    public void setTidMids(List<TidMid> tidMids) {
        this.tidMids = tidMids;
    }

    public DEVICEDETAILS withTidMids(List<TidMid> tidMids) {
        this.tidMids = tidMids;
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

    public DEVICEDETAILS withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("serialnumber", serialnumber).append("tidMids", tidMids).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(additionalProperties).append(serialnumber).append(tidMids).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof DEVICEDETAILS) == false) {
            return false;
        }
        DEVICEDETAILS rhs = ((DEVICEDETAILS) other);
        return new EqualsBuilder().append(additionalProperties, rhs.additionalProperties).append(serialnumber, rhs.serialnumber).append(tidMids, rhs.tidMids).isEquals();
    }

}
