package co.ke.tracom.bprgateway.servers.tcpserver.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidationRequest {
//  @JsonProperty("bill")
//  private String bill;

  @JsonProperty("tnxType")
  private String tnxType;  // valida

  @JsonProperty("credentials")
  private Credentials credentials;

 /* @JsonProperty("data")
  private List<TransactionData> data = new ArrayList<>();

  @JsonProperty("svcCode")
  private String svcCode;*/

  private String svcCode;

<<<<<<< HEAD
  @JsonProperty("field")
  private String field;

  @JsonProperty("value")
=======
  private String field;

>>>>>>> efcbb2b65c6e0f7d10ac63d3583f9325e0a4220c
  private String value;
}
