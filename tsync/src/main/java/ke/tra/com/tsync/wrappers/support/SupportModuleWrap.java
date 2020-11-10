package ke.tra.com.tsync.wrappers.support;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class SupportModuleWrap {

    @JsonProperty("content")
    private List<Content> content = null;

}
