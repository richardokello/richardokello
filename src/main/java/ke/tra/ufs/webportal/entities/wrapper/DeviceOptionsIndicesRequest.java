package ke.tra.ufs.webportal.entities.wrapper;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class DeviceOptionsIndicesRequest {
    private BigDecimal id;
    private Integer index;
}
