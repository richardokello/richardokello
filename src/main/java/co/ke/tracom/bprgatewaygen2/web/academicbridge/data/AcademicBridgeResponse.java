package co.ke.tracom.bprgatewaygen2.web.academicbridge.data;

import lombok.Data;

@Data
public class AcademicBridgeResponse {

  private boolean success;
  private int error_code;
  private String error_msg;
  private String reference_number;
}
