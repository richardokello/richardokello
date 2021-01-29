package co.ke.tracom.bprgatewaygen2.server.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenericRequest {
  private String tnxType;
  private String bill;
  private Credentials credentials;
}
