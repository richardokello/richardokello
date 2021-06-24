package co.ke.tracom.bprgateway.web.izicash.data.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IZICashResponseData {
    private String t24Reference;
    private String IZIReference;
    private String rrn;
}
