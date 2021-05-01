package co.ke.tracom.bprgateway.web.eucl.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeterNoValidation {
    String meterNo;
    String amount;
}
