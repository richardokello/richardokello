
package ke.tra.com.tsync.wrappers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import ke.tra.com.tsync.wrappers.revenueItemsByDevice.Child;
import ke.tra.com.tsync.wrappers.ufslogin.ATTENDANTROLE;
import ke.tra.com.tsync.wrappers.ufslogin.DEVICEDETAILS;
import ke.tra.com.tsync.wrappers.ufslogin.STREAMACCOUNT;
import ke.tra.com.tsync.wrappers.ufslogin.ZONE;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@lombok.Data
public class DataWrapper {

        /*
        "id": 6,
		"action": "Creation",
		"actionStatus": "Unapproved",
		"intrash": "NO",
		"createdAt": 1572385292146,
		"name": "EDUCATION, YOUTH AFFAIRS, CULTURE & SOCIAL SERVICES",
		"isParent": 1,
		"uniqueId": "EYACSS",
		"children": [],
		"parentIds": null,
		"text": "EDUCATION, YOUTH AFFAIRS, CULTURE & SOCIAL SERVICES",
		"nickname": null,
		"hasChildren": false,
		"file": null
         */

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("action")
    private String action;
    @JsonProperty("actionStatus")
    private String actionStatus;
    @JsonProperty("intrash")
    private String intrash;
    @JsonProperty("createdAt")
    private BigInteger createdAt;
    @JsonProperty("name")
    private String name;
    @JsonProperty("isParent")
    private Integer isParent;
    @JsonProperty("uniqueId")
    private String uniqueId;
    @JsonProperty("levels")
    private Integer levels;
    @JsonProperty("children")
    private List<ChildWrapper> children = null;
    @JsonProperty("parentIds")
    private Integer parentIds;
    @JsonProperty("text")
    private String text;
    @JsonProperty("nickname")
    private String nickname;
    @JsonProperty("hasChildren")
    private  Boolean hasChildren=false;

}

