package co.ke.tracom.bprgateway.web.exceptions.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ErrorDetail {

  private String title;
  private int status; // error code
  private String message;
  private String timestamp;
}
