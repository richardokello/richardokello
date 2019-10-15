package ke.tra.ufs.webportal.entities.wrapper;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class UfsTrainedAgentMobileWrapper {


    @NotNull
    private Long agentSupervisorId;
    @NotNull
    private String outletCode;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date trainingDate;
    @NotNull
    private String title;
    @NotNull
    private String description;

    public UfsTrainedAgentMobileWrapper() {
    }

    public UfsTrainedAgentMobileWrapper(Long agentSupervisorId, String outletCode, Date trainingDate, String title, String description) {
        this.agentSupervisorId = agentSupervisorId;
        this.outletCode = outletCode;
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

    public String getOutletCode() {
        return outletCode;
    }

    public void setOutletCode(String outletCode) {
        this.outletCode = outletCode;
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
