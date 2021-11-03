package ke.tra.ufs.webportal.entities.wrapper;



import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Data
public class OutletsInformationWrapper {

    @NotNull
    private String outletName;
    private String outletCode;
    private BigDecimal bankBranchId;
    private String longitude;
    private String latitude;
    private BigDecimal geographicalRegionIds;

    /*Outlet Operating Hours*/
    private String operatingHours;;

    /*Contact Person*/
    private List<OutletContactPerson> contactPerson;

}
