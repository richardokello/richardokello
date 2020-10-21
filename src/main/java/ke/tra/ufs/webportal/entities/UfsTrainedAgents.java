/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.entities;

import ke.axle.chassis.annotations.Filter;
import ke.tra.ufs.webportal.utils.annotations.Searchable;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kmwangi
 */
@Entity
@Table(name = "UFS_TRAINED_AGENTS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UfsTrainedAgents.findAll", query = "SELECT u FROM UfsTrainedAgents u"),
    @NamedQuery(name = "UfsTrainedAgents.findById", query = "SELECT u FROM UfsTrainedAgents u WHERE u.id = :id"),
    @NamedQuery(name = "UfsTrainedAgents.findByAgentName", query = "SELECT u FROM UfsTrainedAgents u WHERE u.agentName = :agentName"),
    @NamedQuery(name = "UfsTrainedAgents.findByRegion", query = "SELECT u FROM UfsTrainedAgents u WHERE u.region = :region"),
    @NamedQuery(name = "UfsTrainedAgents.findByOutletName", query = "SELECT u FROM UfsTrainedAgents u WHERE u.outletName = :outletName"),
    @NamedQuery(name = "UfsTrainedAgents.findByAgentSupervisor", query = "SELECT u FROM UfsTrainedAgents u WHERE u.agentSupervisor = :agentSupervisor"),
    @NamedQuery(name = "UfsTrainedAgents.findByTrainingDate", query = "SELECT u FROM UfsTrainedAgents u WHERE u.trainingDate = :trainingDate"),
    @NamedQuery(name = "UfsTrainedAgents.findByCreationDate", query = "SELECT u FROM UfsTrainedAgents u WHERE u.creationDate = :creationDate"),
    @NamedQuery(name = "UfsTrainedAgents.findByAction", query = "SELECT u FROM UfsTrainedAgents u WHERE u.action = :action"),
    @NamedQuery(name = "UfsTrainedAgents.findByActionStatus", query = "SELECT u FROM UfsTrainedAgents u WHERE u.actionStatus = :actionStatus")})
public class UfsTrainedAgents implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @GenericGenerator(
            name = "UFS_TRAINED_AGENTS_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "UFS_TRAINED_AGENTS_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "0"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @GeneratedValue(generator = "UFS_TRAINED_AGENTS_SEQ")
    @Column(name = "ID")
    private BigDecimal id;
    @Searchable
    @Column(name = "AGENT_NAME")
    private String agentName;
    @Basic(optional = false)
    @Size(min = 1, max = 30)
    @Searchable
    @Column(name = "REGION")
    private String region;
    @Searchable
    @Column(name = "OUTLET_NAME")
    private String outletName;
    @Searchable
    @Column(name = "AGENT_SUPERVISOR")
    private String agentSupervisor;
    @Basic(optional = false)
    @Column(name = "TRAINING_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date trainingDate;
    @Column(name = "CREATION_DATE",insertable = false,updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Filter(isDateRange = true)
    private Date creationDate;
    @Size(max = 20)
    @Column(name = "ACTION",insertable = false)
    private String action;
    @Size(max = 20)
    @Column(name = "ACTION_STATUS",insertable = false)
    private String actionStatus;
    @Searchable
    @Column(name = "TITLE")
    private String title;
    @Searchable
    @Column(name = "DESCRIPTION")
    private String description;
    @JoinColumn(name = "BATCH_ID", referencedColumnName = "BATCH_ID", updatable = false, insertable = false)
    @ManyToOne
    private UfsTrainedAgentsBatch batchId;

    @Column(name = "BATCH_ID")
    private Long batchIds;

    @Column(name = "BATCH_ID", updatable = false, insertable = false)
    private String batchIdsStr;

    public UfsTrainedAgents() {
    }

    public UfsTrainedAgents(BigDecimal id) {
        this.id = id;
    }

    public UfsTrainedAgents(BigDecimal id, String agentName, String region, String outletName, String agentSupervisor, Date trainingDate) {
        this.id = id;
        this.agentName = agentName;
        this.region = region;
        this.outletName = outletName;
        this.agentSupervisor = agentSupervisor;
        this.trainingDate = trainingDate;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getOutletName() {
        return outletName;
    }

    public void setOutletName(String outletName) {
        this.outletName = outletName;
    }

    public String getAgentSupervisor() {
        return agentSupervisor;
    }

    public void setAgentSupervisor(String agentSupervisor) {
        this.agentSupervisor = agentSupervisor;
    }

    public Date getTrainingDate() {
        return trainingDate;
    }

    public void setTrainingDate(Date trainingDate) {
        this.trainingDate = trainingDate;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getActionStatus() {
        return actionStatus;
    }

    public void setActionStatus(String actionStatus) {
        this.actionStatus = actionStatus;
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

    public UfsTrainedAgentsBatch getBatchId() {
        return batchId;
    }

    public void setBatchId(UfsTrainedAgentsBatch batchId) {
        this.batchId = batchId;
    }

    public Long getBatchIds() {
        return batchIds;
    }

    public void setBatchIds(Long batchIds) {
        this.batchIds = batchIds;
    }

    public String getBatchIdsStr() {
        return batchIdsStr;
    }

    public void setBatchIdsStr(String batchIdsStr) {
        this.batchIdsStr = batchIdsStr;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UfsTrainedAgents)) {
            return false;
        }
        UfsTrainedAgents other = (UfsTrainedAgents) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ke.tra.ufs.webportal.entities.UfsTrainedAgents[ id=" + id + " ]";
    }
    
}
