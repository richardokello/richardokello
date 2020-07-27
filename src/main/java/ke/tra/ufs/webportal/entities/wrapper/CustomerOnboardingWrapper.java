package ke.tra.ufs.webportal.entities.wrapper;

import ke.axle.chassis.annotations.Unique;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class CustomerOnboardingWrapper {

    /*Customer Bio Information*/
    @NotNull
    private BigDecimal customerTypeId;
    @NotNull
    private BigDecimal customerClassId;
    @NotNull
    private Long businessTypeId;
    @NotNull
    private String businessName;
    @NotNull
    @Unique
    private String localRegistrationNumber;
    @NotNull
    @Unique
    private String businessLicenseNumber;
    @NotNull
    private Date dateIssued;
    @NotNull
    private Date validTo;
    @NotNull
    private String pinNumber;
    @NotNull
    private String address;

    /*Business Location*/
    @NotNull
    private String businessPrimaryContactNo;
    private String businessSecondaryContactNo;
    @NotNull
    private String businessEmailAddress;

    /*Business Directors*/
    private List<BusinessDirectorsWrapper> directors;

    /*Business Outlets Information*/
    private List<OutletsInformationWrapper> outletsInfo;



}
