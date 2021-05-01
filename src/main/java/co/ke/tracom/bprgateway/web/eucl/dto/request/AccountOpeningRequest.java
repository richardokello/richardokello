package co.ke.tracom.bprgateway.web.eucl.dto.request;

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
}
