package co.ke.tracom.bprgateway.web.agaciro.data.paymentNotification;

import co.ke.tracom.bprgateway.web.agaciro.data.AgaciroResponse;
import lombok.Data;

@Data
public class PaymentNotificationResponse extends AgaciroResponse {

  private Object payment;
}
