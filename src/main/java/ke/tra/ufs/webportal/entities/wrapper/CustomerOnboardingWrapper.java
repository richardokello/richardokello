package ke.tra.ufs.webportal.entities.wrapper;


import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.Set;

@Data
public class CustomerOnboardingWrapper {
    @NotNull
    private BigDecimal customerTypeId;
    @NotNull
    private BigInteger customerClassId;
    private Long businessTypeId;
    @NotNull
    private String businessName;
    private String localRegistrationNumber;
    private String businessLicenseNumber;
    private Date dateIssued;
    private Date validTo;
    private String pinNumber;
    private String address;
    private Long commercialActivityId;
    private BigDecimal estateId;
    @NotNull
    private String businessPrimaryContactNo;
    private String businessSecondaryContactNo;
    private String businessEmailAddress;
    private Set<BusinessDirectorsWrapper> directors;
    private Set<OutletsInformationWrapper> outletsInfo;
    private String mid;
}
