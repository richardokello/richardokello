package co.ke.tracom.bprgatewaygen2.web.ltss.data;

import java.util.Date;
import lombok.Data;

@Data
public class LTSSRequest {

  private String identification;
  private String extRefNo;
  private String intermediary;
  private String description;
  private Date datetime;
  private long amount;
}
