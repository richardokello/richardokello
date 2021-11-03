package ke.co.tra.ufs.tms.entities.wrappers;

import ke.co.tra.ufs.tms.entities.ParGlobalConfig;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
public class GlobalProfileRequest {
    private String name;
    private BigDecimal fileTypeId;
    private String description;
    private BigDecimal id;
    private List<ParGlobalConfig> configs;
}
