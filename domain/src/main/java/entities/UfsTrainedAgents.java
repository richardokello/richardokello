/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Mwagiru Kamoni
 */
@Entity
@Table(name = "UFS_TRAINED_AGENTS")
@NamedQueries({
    @NamedQuery(name = "UfsTrainedAgents.findAll", query = "SELECT u FROM UfsTrainedAgents u")})
public class UfsTrainedAgents implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private BigDecimal id;
    @Basic(optional = false)
    @Column(name = "AGENT_NAME")
    private String agentName;
    @Basic(optional = false)
    @Column(name = "REGION")
    private String region;
    @Basic(optional = false)
    @Column(name = "OUTLET_NAME")
    private String outletName;
    @Basic(optional = false)
    @Column(name = "AGENT_SUPERVISOR")
    private String agentSupervisor;
    @Basic(optional = false)
    @Column(name = "TRAINING_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date trainingDate;
    @Column(name = "CREATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Column(name = "ACTION")
    private String action;
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Basic(optional = false)
    @Column(name = "TITLE")
    private String title;
    @Basic(optional = false)
    @Column(name = "DESCRIPTION")
    private String description;
    @JoinColumn(name = "BATCH_ID", referencedColumnName = "BATCH_ID")
    @ManyToOne
    private UfsTrainedAgentsBatch batchId;

    public UfsTrainedAgents() {
    }

    public UfsTrainedAgents(BigDecimal id) {
        this.id = id;
    }

    public UfsTrainedAgents(BigDecimal id, String agentName, String region, String outletName, String agentSupervisor, Date trainingDate, String title, String description) {
        this.id = id;
        this.agentName = agentName;
        this.region = region;
        this.outletName = outletName;
        this.agentSupervisor = agentSupervisor;
        this.trainingDate = trainingDate;
        this.title = title;
        this.description = description;
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
        return "com.mycompany.oracleufs.UfsTrainedAgents[ id=" + id + " ]";
    }
    
}
