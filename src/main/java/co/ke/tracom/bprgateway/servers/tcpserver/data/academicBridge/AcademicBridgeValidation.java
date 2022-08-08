package co.ke.tracom.bprgateway.servers.tcpserver.data.academicBridge;

import co.ke.tracom.bprgateway.servers.tcpserver.dto.TransactionData;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AcademicBridgeValidation {
  @JsonProperty("responseCode")
  private String responseCode;

  @JsonProperty("responseMessage")
  private String responseMessage;

  @JsonProperty("data")
  private List<TransactionData> data;
}
