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
public class ValidationRequest extends GenericRequest {

  @JsonProperty("data")
  private List<TransactionData> data = new ArrayList<>();

  @JsonProperty("field")
  private String field;

  @JsonProperty("value")
  private String value;
}
