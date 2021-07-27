package co.ke.tracom.bprgateway.servers.tcpserver.data.billMenu;

import co.ke.tracom.bprgateway.servers.tcpserver.dto.GenericRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillMenuRequest extends GenericRequest {
  private String tnxType;
  private String lang;
}
