
package ke.tra.com.tsync.wrappers.ufslogin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "COUNTY",
        "MDA",
        "ZONE",
        "ATTENDANT",
        "AGENT ID",
        "PHONE NUMBER",
        "DEVICE DETAILS",
        "ATTENDANT ROLE",
        "STREAM ACCOUNT"
})

@lombok.Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Data {
    @JsonProperty("COUNTY")
    private String cOUNTY;

    @JsonProperty("MDA")
    private String mDA;

    @JsonProperty("ACCOUNT NUMBER")
    private String accountNumber;

    @JsonProperty("ZONE")
    private List<ZONE> zONE = null;

    @JsonProperty("ATTENDANT")
    private String aTTENDANT;

    @JsonProperty("AGENT ID")
    private String aGENTID;
    @JsonProperty("PHONE NUMBER")
    private String pHONENUMBER;
    @JsonProperty("DEVICE DETAILS")
    private DEVICEDETAILS dEVICEDETAILS;
    @JsonProperty("ATTENDANT ROLE")
    private List<ATTENDANTROLE> aTTENDANTROLE = null;
    @JsonProperty("STREAM ACCOUNT")
    private List<STREAMACCOUNT> sTREAMACCOUNT = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

}
