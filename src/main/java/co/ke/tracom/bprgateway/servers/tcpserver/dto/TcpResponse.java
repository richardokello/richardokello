package co.ke.tracom.bprgateway.servers.tcpserver.dto;

import lombok.Data;

import java.util.List;

@Data
public class TcpResponse {
  private int status;
  private String message;
  private List data;
}
