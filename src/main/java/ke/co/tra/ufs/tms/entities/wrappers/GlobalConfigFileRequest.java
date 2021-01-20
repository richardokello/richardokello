package ke.co.tra.ufs.tms.entities.wrappers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GlobalConfigFileRequest {
    BigDecimal deviceModel;
    BigDecimal profile;
}
