package co.ke.tracom.bprgateway.web.academicbridge.data.paymentstatus;

import co.ke.tracom.bprgateway.web.academicbridge.data.AcademicBridgeResponse;
import lombok.Data;

@Data
public class AcademicBridgePaymentStatusResponse extends AcademicBridgeResponse {

  private String ab_reference;
}
