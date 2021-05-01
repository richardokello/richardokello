package co.ke.tracom.bprgateway.servers.tcpserver.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionData {
  @JsonProperty("name")
  private String name;

  @JsonProperty("value")
  private String value;
}
