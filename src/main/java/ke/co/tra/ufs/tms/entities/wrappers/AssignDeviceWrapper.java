package ke.co.tra.ufs.tms.entities.wrappers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignDeviceWrapper {
    @NonNull
    String serialNo;
    @NonNull
    String vendorName;
}
