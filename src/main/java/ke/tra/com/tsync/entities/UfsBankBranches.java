/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.tra.com.tsync.entities;

import java.io.Serializable;
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
@Table(name = "UFS_BANK_BRANCHES")
@NamedQueries({
    @NamedQuery(name = "UfsBankBranches.findAll", query = "SELECT u FROM UfsBankBranches u")})
public class UfsBankBranches implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @Column(name = "NAME")
    private String name;
    @Basic(optional = false)
    @Column(name = "CODE")
    private String code;
    @Column(name = "ACTION")
    private String action;
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Column(name = "CREATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column(name = "INTRASH")
    private String intrash;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "branchId")
    private Collection<UfsUserBranchManagers> ufsUserBranchManagersCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "branchId")
    private Collection<FieldFrauds> fieldFraudsCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "branchId")
    private Collection<FieldQuestionsCustomers> fieldQuestionsCustomersCollection;
    @OneToMany(mappedBy = "bankBranchId")
    private Collection<TmsDevice> tmsDeviceCollection;
    @JoinColumn(name = "GEOGRAPHICAL_REGION_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private UfsGeographicalRegion geographicalRegionId;
    @JoinColumn(name = "BANK_REGION_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private UfsBankRegion bankRegionId;
    @JoinColumn(name = "BANK_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private UfsBanks bankId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "branchId")
    private Collection<FieldQuestionsSupervisor> fieldQuestionsSupervisorCollection;
    @OneToMany(mappedBy = "branchId")
    private Collection<FieldTasks> fieldTasksCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "branchId")
    private Collection<UfsUserAgentSupervisor> ufsUserAgentSupervisorCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "branchId")
    private Collection<FieldTickets> fieldTicketsCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bankBranchId")
    private Collection<UfsCustomerOutlet> ufsCustomerOutletCollection;
    @OneToMany(mappedBy = "bankBranchId")
    private Collection<UfsGls> ufsGlsCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "currentBranchId")
    private Collection<UfsCustomerTransfer> ufsCustomerTransferCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "destinationBranchId")
    private Collection<UfsCustomerTransfer> ufsCustomerTransferCollection1;

    public UfsBankBranches() {
    }

    public UfsBankBranches(Long id) {
        this.id = id;
    }

    public UfsBankBranches(Long id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getIntrash() {
        return intrash;
    }

    public void setIntrash(String intrash) {
        this.intrash = intrash;
    }

    public Collection<UfsUserBranchManagers> getUfsUserBranchManagersCollection() {
        return ufsUserBranchManagersCollection;
    }

    public void setUfsUserBranchManagersCollection(Collection<UfsUserBranchManagers> ufsUserBranchManagersCollection) {
        this.ufsUserBranchManagersCollection = ufsUserBranchManagersCollection;
    }

    public Collection<FieldFrauds> getFieldFraudsCollection() {
        return fieldFraudsCollection;
    }

    public void setFieldFraudsCollection(Collection<FieldFrauds> fieldFraudsCollection) {
        this.fieldFraudsCollection = fieldFraudsCollection;
    }

    public Collection<FieldQuestionsCustomers> getFieldQuestionsCustomersCollection() {
        return fieldQuestionsCustomersCollection;
    }

    public void setFieldQuestionsCustomersCollection(Collection<FieldQuestionsCustomers> fieldQuestionsCustomersCollection) {
        this.fieldQuestionsCustomersCollection = fieldQuestionsCustomersCollection;
    }

    public Collection<TmsDevice> getTmsDeviceCollection() {
        return tmsDeviceCollection;
    }

    public void setTmsDeviceCollection(Collection<TmsDevice> tmsDeviceCollection) {
        this.tmsDeviceCollection = tmsDeviceCollection;
    }

    public UfsGeographicalRegion getGeographicalRegionId() {
        return geographicalRegionId;
    }

    public void setGeographicalRegionId(UfsGeographicalRegion geographicalRegionId) {
        this.geographicalRegionId = geographicalRegionId;
    }

    public UfsBankRegion getBankRegionId() {
        return bankRegionId;
    }

    public void setBankRegionId(UfsBankRegion bankRegionId) {
        this.bankRegionId = bankRegionId;
    }

    public UfsBanks getBankId() {
        return bankId;
    }

    public void setBankId(UfsBanks bankId) {
        this.bankId = bankId;
    }

    public Collection<FieldQuestionsSupervisor> getFieldQuestionsSupervisorCollection() {
        return fieldQuestionsSupervisorCollection;
    }

    public void setFieldQuestionsSupervisorCollection(Collection<FieldQuestionsSupervisor> fieldQuestionsSupervisorCollection) {
        this.fieldQuestionsSupervisorCollection = fieldQuestionsSupervisorCollection;
    }

    public Collection<FieldTasks> getFieldTasksCollection() {
        return fieldTasksCollection;
    }

    public void setFieldTasksCollection(Collection<FieldTasks> fieldTasksCollection) {
        this.fieldTasksCollection = fieldTasksCollection;
    }

    public Collection<UfsUserAgentSupervisor> getUfsUserAgentSupervisorCollection() {
        return ufsUserAgentSupervisorCollection;
    }

    public void setUfsUserAgentSupervisorCollection(Collection<UfsUserAgentSupervisor> ufsUserAgentSupervisorCollection) {
        this.ufsUserAgentSupervisorCollection = ufsUserAgentSupervisorCollection;
    }

    public Collection<FieldTickets> getFieldTicketsCollection() {
        return fieldTicketsCollection;
    }

    public void setFieldTicketsCollection(Collection<FieldTickets> fieldTicketsCollection) {
        this.fieldTicketsCollection = fieldTicketsCollection;
    }

    public Collection<UfsCustomerOutlet> getUfsCustomerOutletCollection() {
        return ufsCustomerOutletCollection;
    }

    public void setUfsCustomerOutletCollection(Collection<UfsCustomerOutlet> ufsCustomerOutletCollection) {
        this.ufsCustomerOutletCollection = ufsCustomerOutletCollection;
    }

    public Collection<UfsGls> getUfsGlsCollection() {
        return ufsGlsCollection;
    }

    public void setUfsGlsCollection(Collection<UfsGls> ufsGlsCollection) {
        this.ufsGlsCollection = ufsGlsCollection;
    }

    public Collection<UfsCustomerTransfer> getUfsCustomerTransferCollection() {
        return ufsCustomerTransferCollection;
    }

    public void setUfsCustomerTransferCollection(Collection<UfsCustomerTransfer> ufsCustomerTransferCollection) {
        this.ufsCustomerTransferCollection = ufsCustomerTransferCollection;
    }

    public Collection<UfsCustomerTransfer> getUfsCustomerTransferCollection1() {
        return ufsCustomerTransferCollection1;
    }

    public void setUfsCustomerTransferCollection1(Collection<UfsCustomerTransfer> ufsCustomerTransferCollection1) {
        this.ufsCustomerTransferCollection1 = ufsCustomerTransferCollection1;
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
        if (!(object instanceof UfsBankBranches)) {
            return false;
        }
        UfsBankBranches other = (UfsBankBranches) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.oracleufs.UfsBankBranches[ id=" + id + " ]";
    }
    
}
