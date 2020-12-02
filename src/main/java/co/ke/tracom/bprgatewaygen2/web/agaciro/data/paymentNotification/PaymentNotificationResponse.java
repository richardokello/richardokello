package co.ke.tracom.bprgatewaygen2.web.agaciro.data.paymentNotification;

import co.ke.tracom.bprgatewaygen2.web.agaciro.data.AgaciroResponse;
import lombok.Data;

@Data
public class PaymentNotificationResponse extends AgaciroResponse {
    private Object payment;
}
