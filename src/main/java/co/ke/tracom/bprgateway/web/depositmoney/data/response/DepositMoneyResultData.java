package co.ke.tracom.bprgateway.web.depositmoney.data.response;

import co.ke.tracom.bprgateway.web.util.data.BaseResponseData;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
@Builder
public class DepositMoneyResultData extends BaseResponseData {
       private String t24Reference;
       private String charges;
       private String rrn;

}
