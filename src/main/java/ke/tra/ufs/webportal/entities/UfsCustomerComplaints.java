/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ke.axle.chassis.annotations.Filter;
import ke.axle.chassis.annotations.Searchable;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
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
@Table(name = "UFS_CUSTOMER_COMPLAINTS")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "UfsCustomerComplaints.findAll", query = "SELECT u FROM UfsCustomerComplaints u"),
        @NamedQuery(name = "UfsCustomerComplaints.findById", query = "SELECT u FROM UfsCustomerComplaints u WHERE u.id = :id"),
        @NamedQuery(name = "UfsCustomerComplaints.findByComplaintNature", query = "SELECT u FROM UfsCustomerComplaints u WHERE u.complaintNature = :complaintNature"),
        @NamedQuery(name = "UfsCustomerComplaints.findByComplaints", query = "SELECT u FROM UfsCustomerComplaints u WHERE u.complaints = :complaints"),
        @NamedQuery(name = "UfsCustomerComplaints.findByAgentComplained", query = "SELECT u FROM UfsCustomerComplaints u WHERE u.agentComplained = :agentComplained"),
        @NamedQuery(name = "UfsCustomerComplaints.findByAgentPhonenumber", query = "SELECT u FROM UfsCustomerComplaints u WHERE u.agentPhonenumber = :agentPhonenumber"),
        @NamedQuery(name = "UfsCustomerComplaints.findByAgentLocation", query = "SELECT u FROM UfsCustomerComplaints u WHERE u.agentLocation = :agentLocation"),
        @NamedQuery(name = "UfsCustomerComplaints.findByDateOfOccurence", query = "SELECT u FROM UfsCustomerComplaints u WHERE u.dateOfOccurence = :dateOfOccurence"),
        @NamedQuery(name = "UfsCustomerComplaints.findByRemedialActions", query = "SELECT u FROM UfsCustomerComplaints u WHERE u.remedialActions = :remedialActions")})
public class UfsCustomerComplaints implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GenericGenerator(
            name = "CUSTOMER_COMPLAINTS_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "CUSTOMER_COMPLAINTS_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )

    @GeneratedValue(generator = "CUSTOMER_COMPLAINTS_SEQ")
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @Searchable
    @Column(name = "COMPLAINT_NATURE")
    private String complaintNature;
    @Basic(optional = false)
    @Searchable
    @Column(name = "COMPLAINTS")
    private String complaints;
    @Basic(optional = false)
    @Searchable
    @Column(name = "AGENT_COMPLAINED")
    private String agentComplained;
    @Basic(optional = false)
    @Searchable
    @Column(name = "AGENT_PHONENUMBER")
    private String agentPhonenumber;
    @Basic(optional = false)
    @Searchable
    @Column(name = "AGENT_LOCATION")
    private String agentLocation;
    @Column(name = "DATE_OF_OCCURENCE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOfOccurence;
    @Basic(optional = false)
    @Searchable
    @Column(name = "REMEDIAL_ACTIONS")
    private String remedialActions;
    @Column(name = "CREATION_DATE", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Filter(isDateRange = true)
    private Date creationDate;
    @Filter
    @Column(name = "ACTION",insertable = false)
    private String action;
    @Filter
    @Column(name = "ACTION_STATUS",insertable = false)
    private String actionStatus;
    @JoinColumn(name = "BATCH_ID", referencedColumnName = "BATCH_ID", updatable = false, insertable = false)
    @ManyToOne
    @JsonIgnore
    private UfsCustomerComplaintsBatch batchId;

    @Column(name = "BATCH_ID")
    private Long batchIds;

    @Column(name = "BATCH_ID", updatable = false, insertable = false)
    private String batchIdsStr;

    public UfsCustomerComplaints() {
    }

    public UfsCustomerComplaints(Long id) {
        this.id = id;
    }

    public UfsCustomerComplaints(String complaintNature, String complaints,String agentComplained,String agentPhonenumber,String agentLocation, Date dateOfOccurence,String remedialActions, Date creationDate, String action,String actionStatus) {
        this.complaintNature = complaintNature;
        this.complaints = complaints;
        this.agentComplained = agentComplained;
        this.agentPhonenumber = agentPhonenumber;
        this.agentLocation = agentLocation;
        this.dateOfOccurence = dateOfOccurence;
        this.remedialActions = remedialActions;
        this.creationDate = creationDate;
        this.action = action;
        this.actionStatus = actionStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getAgentComplained() {
        return agentComplained;
    }

    public void setAgentComplained(String agentComplained) {
        this.agentComplained = agentComplained;
    }

    public String getAgentPhonenumber() {
        return agentPhonenumber;
    }

    public void setAgentPhonenumber(String agentPhonenumber) {
        this.agentPhonenumber = agentPhonenumber;
    }

    public String getAgentLocation() {
        return agentLocation;
    }

    public void setAgentLocation(String agentLocation) {
        this.agentLocation = agentLocation;
    }

    public Date getDateOfOccurence() {
        return dateOfOccurence;
    }

    public void setDateOfOccurence(Date dateOfOccurence) {
        this.dateOfOccurence = dateOfOccurence;
    }

    public String getRemedialActions() {
        return remedialActions;
    }

    public void setRemedialActions(String remedialActions) {
        this.remedialActions = remedialActions;
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

    public UfsCustomerComplaintsBatch getBatchId() {
        return batchId;
    }

    public void setBatchId(UfsCustomerComplaintsBatch batchId) {
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
        if (!(object instanceof UfsCustomerComplaints)) {
            return false;
        }
        UfsCustomerComplaints other = (UfsCustomerComplaints) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ke.tra.ufs.webportal.entities.UfsCustomerComplaints[ id=" + id + " ]";
    }

}
