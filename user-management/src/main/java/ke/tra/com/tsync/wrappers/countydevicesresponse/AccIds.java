
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
import ke.tra.com.tsync.wrappers.countyrevenues.GeographicalRegionId;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "countyId",
    "accNumber",
    "createdAt",
    "intrash",
    "action",
    "action_status",
    "branchId",
    "branchIds",
    "sbmCounty"
})
public class AccIds {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("countyId")
    private Integer countyId;
    @JsonProperty("accNumber")
    private String accNumber;
    @JsonProperty("createdAt")
    private BigInteger createdAt;
    @JsonProperty("intrash")
    private String intrash;
    @JsonProperty("action")
    private String action;
    @JsonProperty("action_status")
    private String actionStatus;
    @JsonProperty("branchId")
    private Object branchId;
    @JsonProperty("branchIds")
    private BranchIds branchIds;
    @JsonProperty("sbmCounty")
    private SbmCounty sbmCounty;
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

    @JsonProperty("countyId")
    public Integer getCountyId() {
        return countyId;
    }

    @JsonProperty("countyId")
    public void setCountyId(Integer countyId) {
        this.countyId = countyId;
    }

    @JsonProperty("accNumber")
    public String getAccNumber() {
        return accNumber;
    }

    @JsonProperty("accNumber")
    public void setAccNumber(String accNumber) {
        this.accNumber = accNumber;
    }

    @JsonProperty("createdAt")
    public BigInteger getCreatedAt() {
        return createdAt;
    }

    @JsonProperty("createdAt")
    public void setCreatedAt(BigInteger createdAt) {
        this.createdAt = createdAt;
    }

    @JsonProperty("intrash")
    public String getIntrash() {
        return intrash;
    }

    @JsonProperty("intrash")
    public void setIntrash(String intrash) {
        this.intrash = intrash;
    }

    @JsonProperty("action")
    public String getAction() {
        return action;
    }

    @JsonProperty("action")
    public void setAction(String action) {
        this.action = action;
    }

    @JsonProperty("action_status")
    public String getActionStatus() {
        return actionStatus;
    }

    @JsonProperty("action_status")
    public void setActionStatus(String actionStatus) {
        this.actionStatus = actionStatus;
    }

    @JsonProperty("branchId")
    public Object getBranchId() {
        return branchId;
    }

    @JsonProperty("branchId")
    public void setBranchId(Object branchId) {
        this.branchId = branchId;
    }

    @JsonProperty("branchIds")
    public BranchIds getBranchIds() {
        return branchIds;
    }

    @JsonProperty("branchIds")
    public void setBranchIds(BranchIds branchIds) {
        this.branchIds = branchIds;
    }

    @JsonProperty("sbmCounty")
    public SbmCounty getSbmCounty() {
        return sbmCounty;
    }

    @JsonProperty("sbmCounty")
    public void setSbmCounty(SbmCounty sbmCounty) {
        this.sbmCounty = sbmCounty;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }


    public static class BranchIds{
        private int id;
        private String name;
        private String code;
        private String action;
        private String actionStatus;
        private BigInteger createdAt;
        private String intrash;
        private int bankIds;
        private GeographicalRegionId geographicalRegionId;

        public int getId(){
            return id;
        }
        public void setId(int input){
            this.id = input;
        }
        public String getName(){
            return name;
        }
        public void setName(String input){
            this.name = input;
        }
        public String getCode(){
            return code;
        }
        public void setCode(String input){
            this.code = input;
        }
        public String getAction(){
            return action;
        }
        public void setAction(String input){
            this.action = input;
        }
        public String getActionStatus(){
            return actionStatus;
        }
        public void setActionStatus(String input){
            this.actionStatus = input;
        }
        public BigInteger getCreatedAt(){
            return createdAt;
        }
        public void setCreatedAt(BigInteger input){
            this.createdAt = input;
        }
        public String getIntrash(){
            return intrash;
        }
        public void setIntrash(String input){
            this.intrash = input;
        }
        public int getBankIds(){
            return bankIds;
        }
        public void setBankIds(int input){
            this.bankIds = input;
        }
        public GeographicalRegionId getGeographicalRegionId(){
            return geographicalRegionId;
        }
        public void setGeographicalRegionId(GeographicalRegionId input){
            this.geographicalRegionId = input;
        }
    }

}
