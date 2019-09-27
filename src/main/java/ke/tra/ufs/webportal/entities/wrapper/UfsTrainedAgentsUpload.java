package ke.tra.ufs.webportal.entities.wrapper;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.util.Date;

public class UfsTrainedAgentsUpload {


    private String agentSupervisor;
    private String geographicalRegion;
    private String customer;
    private String customerOutlet;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date trainingDate;
    private String title;
    private String description;

    @Transient
    @NotNull
    private MultipartFile file;

    public UfsTrainedAgentsUpload() {
    }

    public UfsTrainedAgentsUpload(String agentSupervisor, String geographicalRegion, String customer, String customerOutlet, Date trainingDate,
                                  String title, String description, MultipartFile file) {
        this.agentSupervisor = agentSupervisor;
        this.geographicalRegion = geographicalRegion;
        this.customer = customer;
        this.customerOutlet = customerOutlet;
        this.trainingDate = trainingDate;
        this.title = title;
        this.description = description;
        this.file = file;
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

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
