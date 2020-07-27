package ke.tra.ufs.webportal.entities.wrapper;


import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class OutletContactPerson {

    @NotNull
    private String contactPersonName;
    @NotNull
    private String contactPersonIdNumber;
    @NotNull
    private String contactPersonTelephone;
    @NotNull
    private String contactPersonEmail;
    private Long workGroupId;
}
