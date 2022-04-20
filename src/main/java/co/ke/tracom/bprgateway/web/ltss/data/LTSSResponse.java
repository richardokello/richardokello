package co.ke.tracom.bprgateway.web.ltss.data;

import lombok.Data;

import java.util.Date;

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
    String rrn;
  String t24Reference;
}
