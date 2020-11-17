
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
    "deviceFileExt",
    "modelId",
    "makeId",
    "deviceTypeId",
    "model",
    "description",
    "action",
    "actionStatus",
    "pFileExt"
})
public class ModelId {

    @JsonProperty("deviceFileExt")
    private Object deviceFileExt;
    @JsonProperty("modelId")
    private Integer modelId;
    @JsonProperty("makeId")
    private Integer makeId;
    @JsonProperty("deviceTypeId")
    private Integer deviceTypeId;
    @JsonProperty("model")
    private String model;
    @JsonProperty("description")
    private String description;
    @JsonProperty("action")
    private String action;
    @JsonProperty("actionStatus")
    private String actionStatus;
    @JsonProperty("pFileExt")
    private Object pFileExt;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("deviceFileExt")
    public Object getDeviceFileExt() {
        return deviceFileExt;
    }

    @JsonProperty("deviceFileExt")
    public void setDeviceFileExt(Object deviceFileExt) {
        this.deviceFileExt = deviceFileExt;
    }

    @JsonProperty("modelId")
    public Integer getModelId() {
        return modelId;
    }

    @JsonProperty("modelId")
    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    @JsonProperty("makeId")
    public Integer getMakeId() {
        return makeId;
    }

    @JsonProperty("makeId")
    public void setMakeId(Integer makeId) {
        this.makeId = makeId;
    }

    @JsonProperty("deviceTypeId")
    public Integer getDeviceTypeId() {
        return deviceTypeId;
    }

    @JsonProperty("deviceTypeId")
    public void setDeviceTypeId(Integer deviceTypeId) {
        this.deviceTypeId = deviceTypeId;
    }

    @JsonProperty("model")
    public String getModel() {
        return model;
    }

    @JsonProperty("model")
    public void setModel(String model) {
        this.model = model;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
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

    @JsonProperty("pFileExt")
    public Object getPFileExt() {
        return pFileExt;
    }

    @JsonProperty("pFileExt")
    public void setPFileExt(Object pFileExt) {
        this.pFileExt = pFileExt;
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
        return new ToStringBuilder(this).append("deviceFileExt", deviceFileExt).append("modelId", modelId).append("makeId", makeId).append("deviceTypeId", deviceTypeId).append("model", model).append("description", description).append("action", action).append("actionStatus", actionStatus).append("pFileExt", pFileExt).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(makeId).append(pFileExt).append(deviceFileExt).append(deviceTypeId).append(actionStatus).append(modelId).append(description).append(action).append(model).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ModelId) == false) {
            return false;
        }
        ModelId rhs = ((ModelId) other);
        return new EqualsBuilder().append(makeId, rhs.makeId).append(pFileExt, rhs.pFileExt).append(deviceFileExt, rhs.deviceFileExt).append(deviceTypeId, rhs.deviceTypeId).append(actionStatus, rhs.actionStatus).append(modelId, rhs.modelId).append(description, rhs.description).append(action, rhs.action).append(model, rhs.model).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
