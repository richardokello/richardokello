package co.ke.tracom.bprgateway.servers.tcpserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Credentials {
  private String username;
  private String password;
  private String serialNumber;
  private String tid;
<<<<<<< HEAD

 /* private String bill;
  private List<TransactionData> data = new ArrayList<>();
  private String svcCode;*/
=======
  private String bill;
  //private List<TransactionData> data = new ArrayList<>();
  private String data;
  private String svcCode;
>>>>>>> efcbb2b65c6e0f7d10ac63d3583f9325e0a4220c
}
