package ke.tra.ufs.webportal.entities.wrapper;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

public class UfsCustomerComplaintsWrapper {

    @NotNull
    private BigDecimal geographicalRegionId;
    @NotNull
    private Long customerId;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateOfOccurence;
    @NotNull
    private String complaintNature;
    @NotNull
    private String complaints;
    @NotNull
    private String remedialActions;

    public UfsCustomerComplaintsWrapper() {
    }

    public UfsCustomerComplaintsWrapper(BigDecimal geographicalRegionId, Long customerId, Date dateOfOccurence, String complaintNature, String complaints, String remedialActions) {
        this.geographicalRegionId = geographicalRegionId;
        this.customerId = customerId;
        this.dateOfOccurence = dateOfOccurence;
        this.complaintNature = complaintNature;
        this.complaints = complaints;
        this.remedialActions = remedialActions;
    }

    public BigDecimal getGeographicalRegionId() {
        return geographicalRegionId;
    }

    public void setGeographicalRegionId(BigDecimal geographicalRegionId) {
        this.geographicalRegionId = geographicalRegionId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Date getDateOfOccurence() {
        return dateOfOccurence;
    }

    public void setDateOfOccurence(Date dateOfOccurence) {
        this.dateOfOccurence = dateOfOccurence;
    }

    public String getComplaintNature() {
        return complaintNature;
    }

    public void setComplaintNature(String complaintNature) {
        this.complaintNature = complaintNature;
    }

    public String getComplaints() {
        return complaints;
    }

    public void setComplaints(String complaints) {
        this.complaints = complaints;
    }

    public String getRemedialActions() {
        return remedialActions;
    }

    public void setRemedialActions(String remedialActions) {
        this.remedialActions = remedialActions;
    }
}
