package co.ke.tracom.bprgateway.servers.tcpserver.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class GenericRequest {
//  private String svcCode;
//  private String tnxType;
//  private String bill;
//  private Credentials credentials;
@JsonProperty("svcCode")
private String svcCode;

  @JsonProperty("bill")
  private String bill;

  @JsonProperty("tnxType")
  private String tnxType;  // valida

  @JsonProperty("credentials")
  private Credentials credentials;



}
