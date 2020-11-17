
package ke.tra.com.tsync.wrappers.ufslogin;

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
    "tids",
    "mids"
})
public class TidMid {

    @JsonProperty("tids")
    private String tids;
    @JsonProperty("mids")
    private String mids;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public TidMid() {
    }

    /**
     * 
     * @param tids
     * @param mids
     */
    public TidMid(String tids, String mids) {
        super();
        this.tids = tids;
        this.mids = mids;
    }

    @JsonProperty("tids")
    public String getTids() {
        return tids;
    }

    @JsonProperty("tids")
    public void setTids(String tids) {
        this.tids = tids;
    }

    public TidMid withTids(String tids) {
        this.tids = tids;
        return this;
    }

    @JsonProperty("mids")
    public String getMids() {
        return mids;
    }

    @JsonProperty("mids")
    public void setMids(String mids) {
        this.mids = mids;
    }

    public TidMid withMids(String mids) {
        this.mids = mids;
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

    public TidMid withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("tids", tids).append("mids", mids).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(mids).append(tids).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof TidMid) == false) {
            return false;
        }
        TidMid rhs = ((TidMid) other);
        return new EqualsBuilder().append(mids, rhs.mids).append(tids, rhs.tids).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
