package ke.co.tra.ufs.tms.entities.wrappers;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class DeviceOptionsIndicesRequest {
    private BigDecimal id;
    private Integer index;
}
