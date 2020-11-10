
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
    "deviceId",
    "serialNo",
    "modelIds",
    "hierachyIds",
    "tenantId",
    "partNumber",
    "status",
    "action",
    "actionStatus",
    "intrash",
    "createdAt",
    "countyId",
    "countyIds",
    "modelId",
    "hierachyId"
})
public class Data {

    @JsonProperty("deviceId")
    private Integer deviceId;
    @JsonProperty("serialNo")
    private String serialNo;
    @JsonProperty("modelIds")
    private Integer modelIds;
    @JsonProperty("hierachyIds")
    private Integer hierachyIds;
    @JsonProperty("tenantId")
    private Integer tenantId;
    @JsonProperty("partNumber")
    private String partNumber;
    @JsonProperty("status")
    private String status;
    @JsonProperty("action")
    private String action;
    @JsonProperty("actionStatus")
    private String actionStatus;
    @JsonProperty("intrash")
    private String intrash;
    @JsonProperty("createdAt")
    private Integer createdAt;
    @JsonProperty("countyId")
    private Integer countyId;
    @JsonProperty("countyIds")
    private CountyIds countyIds;
    @JsonProperty("modelId")
    private ModelId modelId;
    @JsonProperty("hierachyId")
    private HierachyId hierachyId;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("deviceId")
    public Integer getDeviceId() {
        return deviceId;
    }

    @JsonProperty("deviceId")
    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    @JsonProperty("serialNo")
    public String getSerialNo() {
        return serialNo;
    }

    @JsonProperty("serialNo")
    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    @JsonProperty("modelIds")
    public Integer getModelIds() {
        return modelIds;
    }

    @JsonProperty("modelIds")
    public void setModelIds(Integer modelIds) {
        this.modelIds = modelIds;
    }

    @JsonProperty("hierachyIds")
    public Integer getHierachyIds() {
        return hierachyIds;
    }

    @JsonProperty("hierachyIds")
    public void setHierachyIds(Integer hierachyIds) {
        this.hierachyIds = hierachyIds;
    }

    @JsonProperty("tenantId")
    public Integer getTenantId() {
        return tenantId;
    }

    @JsonProperty("tenantId")
    public void setTenantId(Integer tenantId) {
        this.tenantId = tenantId;
    }

    @JsonProperty("partNumber")
    public String getPartNumber() {
        return partNumber;
    }

    @JsonProperty("partNumber")
    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
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
    public Integer getCreatedAt() {
        return createdAt;
    }

    @JsonProperty("createdAt")
    public void setCreatedAt(Integer createdAt) {
        this.createdAt = createdAt;
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
    public CountyIds getCountyIds() {
        return countyIds;
    }

    @JsonProperty("countyIds")
    public void setCountyIds(CountyIds countyIds) {
        this.countyIds = countyIds;
    }

    @JsonProperty("modelId")
    public ModelId getModelId() {
        return modelId;
    }

    @JsonProperty("modelId")
    public void setModelId(ModelId modelId) {
        this.modelId = modelId;
    }

    @JsonProperty("hierachyId")
    public HierachyId getHierachyId() {
        return hierachyId;
    }

    @JsonProperty("hierachyId")
    public void setHierachyId(HierachyId hierachyId) {
        this.hierachyId = hierachyId;
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
        return new ToStringBuilder(this).append("deviceId", deviceId).append("serialNo", serialNo).append("modelIds", modelIds).append("hierachyIds", hierachyIds).append("tenantId", tenantId).append("partNumber", partNumber).append("status", status).append("action", action).append("actionStatus", actionStatus).append("intrash", intrash).append("createdAt", createdAt).append("countyId", countyId).append("countyIds", countyIds).append("modelId", modelId).append("hierachyId", hierachyId).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(modelId).append(intrash).append(countyIds).append(deviceId).append(serialNo).append(createdAt).append(hierachyId).append(actionStatus).append(modelIds).append(countyId).append(tenantId).append(action).append(partNumber).append(additionalProperties).append(hierachyIds).append(status).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Data) == false) {
            return false;
        }
        Data rhs = ((Data) other);
        return new EqualsBuilder().append(modelId, rhs.modelId).append(intrash, rhs.intrash).append(countyIds, rhs.countyIds).append(deviceId, rhs.deviceId).append(serialNo, rhs.serialNo).append(createdAt, rhs.createdAt).append(hierachyId, rhs.hierachyId).append(actionStatus, rhs.actionStatus).append(modelIds, rhs.modelIds).append(countyId, rhs.countyId).append(tenantId, rhs.tenantId).append(action, rhs.action).append(partNumber, rhs.partNumber).append(additionalProperties, rhs.additionalProperties).append(hierachyIds, rhs.hierachyIds).append(status, rhs.status).isEquals();
    }

}
