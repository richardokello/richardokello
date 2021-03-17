package co.ke.tracom.bprgatewaygen2.server.data.academicBridge;

import co.ke.tracom.bprgatewaygen2.server.data.GenericRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AcademicBridgeRequest extends GenericRequest {
  private String billNumber;
}
