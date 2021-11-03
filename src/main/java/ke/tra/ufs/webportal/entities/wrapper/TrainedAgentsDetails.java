package ke.tra.ufs.webportal.entities.wrapper;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import ke.tra.ufs.webportal.utils.annotations.ExportField;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

@JsonIgnoreProperties(value = {"", " "})
public class TrainedAgentsDetails {

    @JsonProperty("Agent Supervisor")
    @ExportField(name = "Agent Supervisor")
    @NotNull
    private String agentSupervisor;

    @JsonProperty("Geographical Region")
    @ExportField(name = "Geographical Region")
    @NotNull
    private String geographicalRegion;

    @JsonProperty("Customer")
    @ExportField(name = "Customer")
    @NotNull
    private String customer;

    @JsonProperty("Customer Outlet")
    @ExportField(name = "Customer Outlet")
    @NotNull
    private String customerOutlet;

    @JsonProperty("Training Date")
    @ExportField(name = "Training Date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private Date trainingDate;

    @JsonProperty("Title")
    @ExportField(name = "Title")
    @NotNull
    private String title;

    @JsonProperty("Description")
    @ExportField(name = "Description")
    @NotNull
    private String description;

    public TrainedAgentsDetails() {
    }

    public TrainedAgentsDetails(String agentSupervisor, String geographicalRegion, String customer, String customerOutlet, Date trainingDate, String title, String description) {
        this.agentSupervisor = agentSupervisor;
        this.geographicalRegion = geographicalRegion;
        this.customer = customer;
        this.customerOutlet = customerOutlet;
        this.trainingDate = trainingDate;
        this.title = title;
        this.description = description;
    }

    public String getAgentSupervisor() {
        return agentSupervisor;
    }

    public void setAgentSupervisor(String agentSupervisor) {
        this.agentSupervisor = agentSupervisor;
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

    public String getCustomerOutlet() {
        return customerOutlet;
    }

    public void setCustomerOutlet(String customerOutlet) {
        this.customerOutlet = customerOutlet;
    }

    public Date getTrainingDate() {
        return trainingDate;
    }

    public void setTrainingDate(Date trainingDate) {
        this.trainingDate = trainingDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
