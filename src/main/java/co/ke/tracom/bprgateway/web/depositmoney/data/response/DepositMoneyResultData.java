package co.ke.tracom.bprgateway.web.depositmoney.data.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DepositMoneyResultData {
       private String t24Reference;
       private String charges;
}
