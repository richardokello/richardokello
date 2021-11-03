package ke.tra.ufs.webportal.entities.wrapper;

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
