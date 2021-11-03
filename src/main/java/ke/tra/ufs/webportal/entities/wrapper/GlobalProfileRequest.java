package ke.tra.ufs.webportal.entities.wrapper;

import ke.tra.ufs.webportal.entities.ParGlobalConfig;
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
