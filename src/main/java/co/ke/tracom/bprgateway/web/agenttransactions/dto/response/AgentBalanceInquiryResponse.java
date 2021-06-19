package co.ke.tracom.bprgateway.web.agenttransactions.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AgentBalanceInquiryResponse {
    String status;
    String message;
    String balance;
}
