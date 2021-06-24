package co.ke.tracom.bprgateway.web.eucl.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeterNoData {
    String meterNo;
    String accountName;
    String meterLocation;
    String rrn;
}
