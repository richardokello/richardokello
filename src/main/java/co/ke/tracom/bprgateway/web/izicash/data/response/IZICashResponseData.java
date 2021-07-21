package co.ke.tracom.bprgateway.web.izicash.data.response;

import co.ke.tracom.bprgateway.web.util.data.BaseResponseData;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
@Builder
public class IZICashResponseData extends BaseResponseData {
    private String t24Reference;
    private String IZIReference;
    private String rrn;
    private String tid;
    private String mid;
}
