package co.ke.tracom.bprgatewaygen2.server.data.academicBridge;

import co.ke.tracom.bprgatewaygen2.server.data.TcpRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AcademicBridgeRequest extends TcpRequest {
  private String billNumber;
}
