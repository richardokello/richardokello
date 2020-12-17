package ke.tra.ufs.webportal.entities.wrapper;


import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class OutletContactPerson {

    private String contactPersonName;
    private String contactPersonIdNumber;
    private String contactPersonTelephone;
    private String contactPersonEmail;
    private String posRole;
    private String userName;
}
