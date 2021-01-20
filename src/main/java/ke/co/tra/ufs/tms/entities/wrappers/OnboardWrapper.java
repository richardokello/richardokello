package ke.co.tra.ufs.tms.entities.wrappers;

import java.math.BigDecimal;
import java.util.List;
import javax.validation.constraints.NotNull;

import ke.co.tra.ufs.tms.entities.TmsDeviceSimcard;
import ke.co.tra.ufs.tms.entities.TmsDeviceTidsMids;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Owori Juma
 */
@Data
@NoArgsConstructor
public class OnboardWrapper {
    BigDecimal deviceId;
    BigDecimal modelId;
    BigDecimal estateId;
    BigDecimal appId;
    @NotNull
    String serialNo;
    String imeiNo;
    String partNumber;
    BigDecimal paramDefId;
    String values;
    MultipartFile[] file;
    BigDecimal productId;
    String agentMerchantId;
    String tenantIds;
    BigDecimal outletIds;
    List<TmsDeviceTidsMids> tmsDeviceTidsMids;
    List<TmsDeviceSimcard> tmsDeviceSimcards;
    String customerOwnerName;
    BigDecimal deviceTypeId;
    String posRole;
    @NotNull
    Long customerOwnerId;
    BigDecimal masterProfileId;
    List<BigDecimal> deviceOptionsIds;

}
