package ke.co.tra.ufs.tms.entities.wrappers;

import ke.co.tra.ufs.tms.entities.TmsDeviceSimcard;
import ke.co.tra.ufs.tms.entities.TmsDeviceTidsMids;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
public class DevicesWrapper {
    BigDecimal modelId;
    BigDecimal estateId;
    BigDecimal appId;
    @NotNull
    String serialNo;
    List<TmsDeviceTidsMids> tmsDeviceTidsMids;
    List<TmsDeviceSimcard> tmsDeviceSimcards;
    String customerOwnerName;
    String posRole;
    BigDecimal masterProfileId;
    Set<BigDecimal> deviceOptionsIds;
    String values;
    MultipartFile[] file;
    BigDecimal productId;
    BigDecimal deviceId;
}
