package co.ke.tracom.bprgatewaygen2.server.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Credentials {
  private String username;
  private String password;
  private String serialNumber;
  private String tid;
}
