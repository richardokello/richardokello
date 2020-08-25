package ke.tra.ufs.webportal.entities.wrapper;


import ke.axle.chassis.annotations.Unique;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Data
public class OutletsInformationWrapper {

    @NotNull
    @Unique
    private String outletName;
    @NotNull
    @Unique
    private String outletCode;
    @NotNull
    private BigDecimal bankBranchId;
    private String longitude;
    private String latitude;
    private BigDecimal geographicalRegionIds;

    /*Outlet Operating Hours*/
    @NotNull
    private String operatingHours;;

    /*Contact Person*/
    private List<OutletContactPerson> contactPerson;

}
