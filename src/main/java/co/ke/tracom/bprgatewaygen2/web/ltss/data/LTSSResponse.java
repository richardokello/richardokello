package co.ke.tracom.bprgatewaygen2.web.ltss.data;

import java.util.Date;
import lombok.Data;

@Data
public class LTSSResponse {

  private String identification;
  private String name;
  private long amount;
  private String description;
  private String ExtRefNo;
  private String extNo;
  private String intermediary;
  private Date datetime;
  private String status;
}
