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
@Table(name = "FIELD_TICKETS")
@NamedQueries({
    @NamedQuery(name = "FieldTickets.findAll", query = "SELECT f FROM FieldTickets f")})
public class FieldTickets implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private BigDecimal id;
    @Column(name = "USER_TYPE")
    private Short userType;
    @Column(name = "CREATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column(name = "RESOLVED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date resolvedAt;
    @Column(name = "ACTION")
    private String action;
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Column(name = "INTRASH")
    private String intrash;
    @Column(name = "TICKET_STATUS")
    private String ticketStatus;
    @Column(name = "DESCRIPTION")
    private String description;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ticketId")
    private Collection<FieldFrauds> fieldFraudsCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ticketId")
    private Collection<FieldTicketsComments> fieldTicketsCommentsCollection;
    @OneToMany(mappedBy = "ticketId")
    private Collection<FieldTasks> fieldTasksCollection;
    @JoinColumn(name = "SUPERVISOR_ID", referencedColumnName = "USER_ID")
    @ManyToOne
    private UfsUser supervisorId;
    @JoinColumn(name = "CUSTOMER_ID", referencedColumnName = "ID")
    @ManyToOne
    private UfsCustomer customerId;
    @JoinColumn(name = "BANK_REGION_ID", referencedColumnName = "ID")
    @ManyToOne
    private UfsBankRegion bankRegionId;
    @JoinColumn(name = "BRANCH_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private UfsBankBranches branchId;
    @JoinColumn(name = "TYPE_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private FieldTicketsType typeId;

    public FieldTickets() {
    }

    public FieldTickets(BigDecimal id) {
        this.id = id;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public Short getUserType() {
        return userType;
    }

    public void setUserType(Short userType) {
        this.userType = userType;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getResolvedAt() {
        return resolvedAt;
    }

    public void setResolvedAt(Date resolvedAt) {
        this.resolvedAt = resolvedAt;
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

    public String getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(String ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Collection<FieldFrauds> getFieldFraudsCollection() {
        return fieldFraudsCollection;
    }

    public void setFieldFraudsCollection(Collection<FieldFrauds> fieldFraudsCollection) {
        this.fieldFraudsCollection = fieldFraudsCollection;
    }

    public Collection<FieldTicketsComments> getFieldTicketsCommentsCollection() {
        return fieldTicketsCommentsCollection;
    }

    public void setFieldTicketsCommentsCollection(Collection<FieldTicketsComments> fieldTicketsCommentsCollection) {
        this.fieldTicketsCommentsCollection = fieldTicketsCommentsCollection;
    }

    public Collection<FieldTasks> getFieldTasksCollection() {
        return fieldTasksCollection;
    }

    public void setFieldTasksCollection(Collection<FieldTasks> fieldTasksCollection) {
        this.fieldTasksCollection = fieldTasksCollection;
    }

    public UfsUser getSupervisorId() {
        return supervisorId;
    }

    public void setSupervisorId(UfsUser supervisorId) {
        this.supervisorId = supervisorId;
    }

    public UfsCustomer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UfsCustomer customerId) {
        this.customerId = customerId;
    }

    public UfsBankRegion getBankRegionId() {
        return bankRegionId;
    }

    public void setBankRegionId(UfsBankRegion bankRegionId) {
        this.bankRegionId = bankRegionId;
    }

    public UfsBankBranches getBranchId() {
        return branchId;
    }

    public void setBranchId(UfsBankBranches branchId) {
        this.branchId = branchId;
    }

    public FieldTicketsType getTypeId() {
        return typeId;
    }

    public void setTypeId(FieldTicketsType typeId) {
        this.typeId = typeId;
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
        if (!(object instanceof FieldTickets)) {
            return false;
        }
        FieldTickets other = (FieldTickets) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.oracleufs.FieldTickets[ id=" + id + " ]";
    }
    
}
