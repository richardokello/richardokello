package co.ke.tracom.bprgatewaygen2.server.data;

import lombok.Data;

import java.util.List;

@Data
public class TcpResponse {
    private int status;
    private String message;
    private List data;
}
