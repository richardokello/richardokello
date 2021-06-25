package co.ke.tracom.bprgateway.web.util.data;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class BaseResponseData {
    String username;
    String names;
    String businessName;
    String location;
}
