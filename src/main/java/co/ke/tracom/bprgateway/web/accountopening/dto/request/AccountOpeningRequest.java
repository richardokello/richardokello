package co.ke.tracom.bprgateway.web.accountopening.dto.request;

import co.ke.tracom.bprgateway.web.util.data.MerchantAuthInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountOpeningRequest {
    String motherBranchID;
    String province;
    String district;
    String sector;
    String cell;
    String firstName;
    String secondName;
    String lastName;
    MerchantAuthInfo credentials;
}
