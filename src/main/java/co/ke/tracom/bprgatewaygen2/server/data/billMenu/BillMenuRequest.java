package co.ke.tracom.bprgatewaygen2.server.data.billMenu;

import co.ke.tracom.bprgatewaygen2.server.data.GenericRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillMenuRequest extends GenericRequest {
  private String tnxType;
}
