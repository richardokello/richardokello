package co.ke.tracom.bprgateway.servers.tcpserver.data.academicBridge;

import co.ke.tracom.bprgateway.servers.tcpserver.dto.GenericRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AcademicBridgeRequest extends GenericRequest {
  private String billNumber;
}
