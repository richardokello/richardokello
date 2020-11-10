package entities.wrappers;

import entities.UfsWorkgroup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsWrapper {
    private String accountNumber;
    private String localRegNumber;
    private String customerName;
    private String outletName;
    private String outletCode;
    private String contactPerson;
    private String idNumber;
    private String phoneNumber;
    private String assistantRole;
    private UfsWorkgroup ufsWorkgroup;

}
