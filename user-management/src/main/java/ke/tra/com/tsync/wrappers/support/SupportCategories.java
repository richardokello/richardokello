package ke.tra.com.tsync.wrappers.support;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;


@Data
public class SupportCategories {
    /*
    {
        "id": 1,
        "nature": "Complain",
        "description": "Issue reportin",
        "action": "Creation",
        "actionStatus": "Approved",
        "intrash": "NO",
        "createdAt": 1574459877422
      },
     */

    @JsonProperty("id")
    private String id;

    @JsonProperty("nature")
    private String nature;

    @JsonProperty("description")
    private String description;

    @JsonProperty("action")
    private String action;
    @JsonProperty("actionStatus")
    private String actionStatus;
    @JsonProperty("intrash")
    private String intrash;
    @JsonProperty("createdAt")
    private Timestamp createdAt;

}
