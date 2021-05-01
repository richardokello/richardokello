package co.ke.tracom.bprgateway.web.rwandarevenue.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RRATINValidationRequest {
  String agentId;
  String RRATIN;
}
