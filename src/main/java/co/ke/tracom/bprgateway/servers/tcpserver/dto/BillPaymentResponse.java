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
public class BillPaymentResponse {
  @JsonProperty("responseCode")
  private String responseCode;

  @JsonProperty("responseMessage")
  private String responseMessage;

  @JsonProperty("data")
  private List<TransactionData> data = new ArrayList<>();

  private List<AcademicTransactionData> paymentData = new ArrayList<>();
}
