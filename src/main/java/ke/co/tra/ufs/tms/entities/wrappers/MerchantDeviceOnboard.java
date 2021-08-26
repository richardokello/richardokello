package ke.co.tra.ufs.tms.entities.wrappers;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class MerchantDeviceOnboard extends DevicesWrapper {
    @NotNull
    Long customerId;
    Long outletIds;
}
