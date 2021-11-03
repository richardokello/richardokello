package ke.tra.ufs.webportal.entities.wrapper;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

public class UfsTrainedAgentWrapper {


    @NotNull
    private Long agentSupervisorId;
    @NotNull
    private BigDecimal geographicalRegionId;
    @NotNull
    private Long customerId;
    @NotNull
    private BigDecimal customerOutletId;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date trainingDate;
    @NotNull
    private String title;
    @NotNull
    private String description;
    private MultipartFile file;

    public UfsTrainedAgentWrapper() {
    }

    public UfsTrainedAgentWrapper(Long agentSupervisorId, BigDecimal geographicalRegionId, Long customerId, BigDecimal customerOutletId, Date trainingDate
    ,String title,String description) {
        this.agentSupervisorId = agentSupervisorId;
        this.geographicalRegionId = geographicalRegionId;
        this.customerId = customerId;
        this.customerOutletId = customerOutletId;
        this.trainingDate = trainingDate;
        this.title = title;
        this.description = description;

    }

    public Long getAgentSupervisorId() {
        return agentSupervisorId;
    }

    public void setAgentSupervisorId(Long agentSupervisorId) {
        this.agentSupervisorId = agentSupervisorId;
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

    public BigDecimal getCustomerOutletId() {
        return customerOutletId;
    }

    public void setCustomerOutletId(BigDecimal customerOutletId) {
        this.customerOutletId = customerOutletId;
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
