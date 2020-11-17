/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
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
@Table(name = "UFS_GLS")
@NamedQueries({
    @NamedQuery(name = "UfsGls.findAll", query = "SELECT u FROM UfsGls u")})
public class UfsGls implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @Column(name = "GL_NAME")
    private String glName;
    @Basic(optional = false)
    @Column(name = "GL_CODE")
    private String glCode;
    @Basic(optional = false)
    @Column(name = "GL_ACCOUNT_NUMBER")
    private String glAccountNumber;
    @Basic(optional = false)
    @Column(name = "GL_LOCATION")
    private String glLocation;
    @Basic(optional = false)
    @Column(name = "ACTION")
    private String action;
    @Basic(optional = false)
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Basic(optional = false)
    @Column(name = "CREATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Basic(optional = false)
    @Column(name = "INTRASH")
    private String intrash;
    @JoinColumn(name = "TENANT_ID", referencedColumnName = "U_UID")
    @ManyToOne(optional = false)
    private UfsOrganizationUnits tenantId;
    @JoinColumn(name = "BATCH_ID", referencedColumnName = "BATCH_ID")
    @ManyToOne
    private UfsGlsBatch batchId;
    @JoinColumn(name = "BANK_BRANCH_ID", referencedColumnName = "ID")
    @ManyToOne
    private UfsBankBranches bankBranchId;
    @JoinColumn(name = "BANK_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private UfsBanks bankId;

    public UfsGls() {
    }

    public UfsGls(Long id) {
        this.id = id;
    }

    public UfsGls(Long id, String glName, String glCode, String glAccountNumber, String glLocation, String action, String actionStatus, Date createdAt, String intrash) {
        this.id = id;
        this.glName = glName;
        this.glCode = glCode;
        this.glAccountNumber = glAccountNumber;
        this.glLocation = glLocation;
        this.action = action;
        this.actionStatus = actionStatus;
        this.createdAt = createdAt;
        this.intrash = intrash;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGlName() {
        return glName;
    }

    public void setGlName(String glName) {
        this.glName = glName;
    }

    public String getGlCode() {
        return glCode;
    }

    public void setGlCode(String glCode) {
        this.glCode = glCode;
    }

    public String getGlAccountNumber() {
        return glAccountNumber;
    }

    public void setGlAccountNumber(String glAccountNumber) {
        this.glAccountNumber = glAccountNumber;
    }

    public String getGlLocation() {
        return glLocation;
    }

    public void setGlLocation(String glLocation) {
        this.glLocation = glLocation;
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

    public UfsOrganizationUnits getTenantId() {
        return tenantId;
    }

    public void setTenantId(UfsOrganizationUnits tenantId) {
        this.tenantId = tenantId;
    }

    public UfsGlsBatch getBatchId() {
        return batchId;
    }

    public void setBatchId(UfsGlsBatch batchId) {
        this.batchId = batchId;
    }

    public UfsBankBranches getBankBranchId() {
        return bankBranchId;
    }

    public void setBankBranchId(UfsBankBranches bankBranchId) {
        this.bankBranchId = bankBranchId;
    }

    public UfsBanks getBankId() {
        return bankId;
    }

    public void setBankId(UfsBanks bankId) {
        this.bankId = bankId;
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
        if (!(object instanceof UfsGls)) {
            return false;
        }
        UfsGls other = (UfsGls) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.oracleufs.UfsGls[ id=" + id + " ]";
    }
    
}
