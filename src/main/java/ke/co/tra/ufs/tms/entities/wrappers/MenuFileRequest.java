package ke.co.tra.ufs.tms.entities.wrappers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

// TODO merge menu file request and global config file request
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuFileRequest {
    BigDecimal deviceModel;
    BigDecimal menuProfile;
}
