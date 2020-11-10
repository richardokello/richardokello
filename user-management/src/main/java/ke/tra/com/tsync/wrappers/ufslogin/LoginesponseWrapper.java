
package ke.tra.com.tsync.wrappers.ufslogin;

import java.sql.Timestamp;
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
    "code",
    "message",
    "data",
    "timestamp"
})
public class LoginesponseWrapper {

    @JsonProperty("code")
    private Integer code;
    @JsonProperty("message")
    private String message;
    @JsonProperty("data")
    private Data data;
    @JsonProperty("timestamp")
    private Timestamp timestamp;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public LoginesponseWrapper() {
    }

    /**
     * 
     * @param code
     * @param data
     * @param message
     * @param timestamp
     */
    public LoginesponseWrapper(Integer code, String message, Data data, Timestamp  timestamp) {
        super();
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = timestamp;
    }

    @JsonProperty("code")
    public Integer getCode() {
        return code;
    }

    @JsonProperty("code")
    public void setCode(Integer code) {
        this.code = code;
    }

    public LoginesponseWrapper withCode(Integer code) {
        this.code = code;
        return this;
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    @JsonProperty("message")
    public void setMessage(String message) {
        this.message = message;
    }

    public LoginesponseWrapper withMessage(String message) {
        this.message = message;
        return this;
    }

    @JsonProperty("data")
    public Data getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(Data data) {
        this.data = data;
    }

    public LoginesponseWrapper withData(Data data) {
        this.data = data;
        return this;
    }

    @JsonProperty("timestamp")
    public Timestamp getTimestamp() {
        return timestamp;
    }

    @JsonProperty("timestamp")
    public void setTimestamp(Timestamp  timestamp) {
        this.timestamp = timestamp;
    }

    public LoginesponseWrapper withTimestamp(Timestamp  timestamp) {
        this.timestamp = timestamp;
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

    public LoginesponseWrapper withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("code", code).append("message", message).append("data", data).append("timestamp", timestamp).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(code).append(additionalProperties).append(message).append(data).append(timestamp).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof LoginesponseWrapper) == false) {
            return false;
        }
        LoginesponseWrapper rhs = ((LoginesponseWrapper) other);
        return new EqualsBuilder().append(code, rhs.code).append(additionalProperties, rhs.additionalProperties).append(message, rhs.message).append(data, rhs.data).append(timestamp, rhs.timestamp).isEquals();
    }

}
