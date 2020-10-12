package ke.tra.ufs.webportal.entities.wrapper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import ke.tra.ufs.webportal.utils.annotations.ExportField;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

@JsonIgnoreProperties(value = {"", " "})
public class CustomerComplaintsDetails {

    @JsonProperty("Geographical Region")
    @ExportField(name = "Geographical Region")
    @NotNull
    private String geographicalRegion;

    @JsonProperty("Agent Name")
    @ExportField(name = "Agent Name")
    @NotNull
    private String agentName;


    @JsonProperty("Phone Number")
    @ExportField(name = "Phone Number")
    @NotNull
    private String phoneNumber;

    @JsonProperty("Occurence Date(yyyy-MM-dd e.g 2020-10-11)")
    @ExportField(name = "Occurence Date(yyyy-MM-dd e.g 2020-10-11)")
    @NotNull
    private Date dateOfOccurence;

    @JsonProperty("Complaint Nature")
    @ExportField(name = "Complaint Nature")
    @NotNull
    private String complaintNature;

    @JsonProperty("Complaints")
    @ExportField(name = "Complaints")
    @NotNull
    private String complaints;

    @JsonProperty("Remedial Actions")
    @ExportField(name = "Remedial Actions")
    @NotNull
    private String remedialActions;



    public CustomerComplaintsDetails() {
    }

    public CustomerComplaintsDetails( String geographicalRegion,  String agentName,  Date dateOfOccurence,  String complaintNature,  String complaints,  String remedialActions,
                                      String phoneNumber) {
        this.geographicalRegion = geographicalRegion;
        this.agentName = agentName;
        this.dateOfOccurence = dateOfOccurence;
        this.complaintNature = complaintNature;
        this.complaints = complaints;
        this.remedialActions = remedialActions;
        this.phoneNumber = phoneNumber;
    }

    public String getGeographicalRegion() {
        return geographicalRegion;
    }

    public void setGeographicalRegion(String geographicalRegion) {
        this.geographicalRegion = geographicalRegion;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
