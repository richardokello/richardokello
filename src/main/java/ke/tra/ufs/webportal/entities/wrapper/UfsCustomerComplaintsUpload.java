package ke.tra.ufs.webportal.entities.wrapper;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

public class UfsCustomerComplaintsUpload {

    @NotNull
    private String geographicalRegion;
    @NotNull
    private String customer;
    @NotNull
    private String phoneNumber;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateOfOccurence;
    @NotNull
    private String complaintNature;
    @NotNull
    private String complaints;
    @NotNull
    private String remedialActions;

    @Transient
    @NotNull
    private MultipartFile file;

    public UfsCustomerComplaintsUpload() {
    }

    public UfsCustomerComplaintsUpload( String geographicalRegion,  String customer,  String phoneNumber,  Date dateOfOccurence, String complaintNature, String complaints,  String remedialActions,  MultipartFile file) {
        this.geographicalRegion = geographicalRegion;
        this.customer = customer;
        this.phoneNumber = phoneNumber;
        this.dateOfOccurence = dateOfOccurence;
        this.complaintNature = complaintNature;
        this.complaints = complaints;
        this.remedialActions = remedialActions;
        this.file = file;
    }

    public String getGeographicalRegion() {
        return geographicalRegion;
    }

    public void setGeographicalRegion(String geographicalRegion) {
        this.geographicalRegion = geographicalRegion;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
