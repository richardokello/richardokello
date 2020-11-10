/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Mwagiru Kamoni
 */
@Entity
@Table(name = "FIELD_TASKS")
@NamedQueries({
    @NamedQuery(name = "FieldTasks.findAll", query = "SELECT f FROM FieldTasks f")})
public class FieldTasks implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private BigDecimal id;
    @Basic(optional = false)
    @Column(name = "NAME")
    private String name;
    @Basic(optional = false)
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "TASK_STATUS")
    private String taskStatus;
    @Column(name = "CREATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column(name = "COMPLETED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date completedAt;
    @Column(name = "ACTION")
    private String action;
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Column(name = "INTRASH")
    private String intrash;
    @JoinColumn(name = "SUPERVISOR_ID", referencedColumnName = "USER_ID")
    @ManyToOne
    private UfsUser supervisorId;
    @JoinColumn(name = "REGION_ID", referencedColumnName = "ID")
    @ManyToOne
    private UfsBankRegion regionId;
    @JoinColumn(name = "BRANCH_ID", referencedColumnName = "ID")
    @ManyToOne
    private UfsBankBranches branchId;
    @JoinColumn(name = "TICKET_ID", referencedColumnName = "ID")
    @ManyToOne
    private FieldTickets ticketId;
    @JoinColumn(name = "TASK_TYPE_ID", referencedColumnName = "ID")
    @ManyToOne
    private FieldTaskType taskTypeId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "taskId")
    private Collection<FieldTaskNotes> fieldTaskNotesCollection;

    public FieldTasks() {
    }

    public FieldTasks(BigDecimal id) {
        this.id = id;
    }

    public FieldTasks(BigDecimal id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Date completedAt) {
        this.completedAt = completedAt;
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

    public String getIntrash() {
        return intrash;
    }

    public void setIntrash(String intrash) {
        this.intrash = intrash;
    }

    public UfsUser getSupervisorId() {
        return supervisorId;
    }

    public void setSupervisorId(UfsUser supervisorId) {
        this.supervisorId = supervisorId;
    }

    public UfsBankRegion getRegionId() {
        return regionId;
    }

    public void setRegionId(UfsBankRegion regionId) {
        this.regionId = regionId;
    }

    public UfsBankBranches getBranchId() {
        return branchId;
    }

    public void setBranchId(UfsBankBranches branchId) {
        this.branchId = branchId;
    }

    public FieldTickets getTicketId() {
        return ticketId;
    }

    public void setTicketId(FieldTickets ticketId) {
        this.ticketId = ticketId;
    }

    public FieldTaskType getTaskTypeId() {
        return taskTypeId;
    }

    public void setTaskTypeId(FieldTaskType taskTypeId) {
        this.taskTypeId = taskTypeId;
    }

    public Collection<FieldTaskNotes> getFieldTaskNotesCollection() {
        return fieldTaskNotesCollection;
    }

    public void setFieldTaskNotesCollection(Collection<FieldTaskNotes> fieldTaskNotesCollection) {
        this.fieldTaskNotesCollection = fieldTaskNotesCollection;
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
        if (!(object instanceof FieldTasks)) {
            return false;
        }
        FieldTasks other = (FieldTasks) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.oracleufs.FieldTasks[ id=" + id + " ]";
    }
    
}
