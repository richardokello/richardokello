package co.ke.tracom.bprgateway.web.agenttransactions.dto.response;

import co.ke.tracom.bprgateway.web.util.data.BaseResponseData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AgentBalanceInquiryResponse extends BaseResponseData {
    String status;
    String message;
    String balance;
    String tid;
    String mid;
}
