package ke.co.tra.ufs.tms.entities.wrappers;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author Owori Juma
 */
@Data
@NoArgsConstructor
public class OnboardWrapper extends DevicesWrapper {
    String imeiNo;
    String partNumber;
    BigDecimal paramDefId;
    String agentMerchantId;
    String tenantIds;
    BigDecimal outletIds;
    BigDecimal deviceTypeId;
    @NotNull
    Long customerOwnerId;

}
