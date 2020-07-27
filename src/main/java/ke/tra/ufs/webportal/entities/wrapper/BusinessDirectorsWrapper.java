package ke.tra.ufs.webportal.entities.wrapper;


import lombok.Data;

@Data
public class BusinessDirectorsWrapper {

    private String directorName;
    private String directorIdNumber;
    private String directorPrimaryContactNumber;
    private String directorSecondaryContactNumber;
    private String directorEmailAddress;
    private Long directorDesignationId;
}
