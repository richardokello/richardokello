package ke.tra.com.tsync.wrappers.ufslogin;


import com.fasterxml.jackson.annotation.*;
import ke.tra.com.tsync.wrappers.countydevicesresponse.CountyIds;
import lombok.*;
import lombok.Data;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;




    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "id",
            "name",
            "code",
            "creationDate",
            "action",
            "actionStatus",
            "intrash",
            "countyId",
            "countyIds"
    })

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter @Setter

    public class ZONE {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("code")
    private String code;
    @JsonProperty("creationDate")
    private BigInteger creationDate;
    @JsonProperty("action")
    private String action;
    @JsonProperty("actionStatus")
    private String actionStatus;
    @JsonProperty("intrash")
    private String intrash;
    @JsonProperty("countyId")
    private String countyId;
    @JsonProperty("countyIds")
    private CountyIds countyIds;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

}