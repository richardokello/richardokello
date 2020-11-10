package ke.tra.com.tsync.wrappers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NonNull;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public class ChildWrapper {

        @JsonProperty("id")
        private String id;
        @JsonProperty("action")
        private String action;
        @JsonProperty("actionStatus")
        private String actionStatus;
        @JsonProperty("intrash")
        private String intrash;
        @JsonProperty("createdAt")
        private Timestamp createdAt;
        @JsonProperty("name")
        private String name;
        @JsonProperty("isParent")
        private Integer isParent;
        @JsonProperty("uniqueId")
        private String uniqueId;
        @JsonProperty("children")
        private List<Object> children = null;
        @JsonProperty("parentIds")
        private Integer parentIds;
        @JsonProperty("text")
        private String text;
        @JsonProperty("nickname")
        private String nickname;
        @JsonProperty("hasChildren")
        private  Boolean hasChildren=false;
        @JsonIgnore
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    }