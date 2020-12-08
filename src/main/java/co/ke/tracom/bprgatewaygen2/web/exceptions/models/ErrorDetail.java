package co.ke.tracom.bprgatewaygen2.web.exceptions.models;

import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ErrorDetail {

  private String title;
  private int status; // error code
  private String message;
  private Date timestamp;
}
