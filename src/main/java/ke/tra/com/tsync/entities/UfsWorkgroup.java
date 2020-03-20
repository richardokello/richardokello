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
@Table(name = "UFS_WORKGROUP")
@NamedQueries({
    @NamedQuery(name = "UfsWorkgroup.findAll", query = "SELECT u FROM UfsWorkgroup u")})
public class UfsWorkgroup implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "GROUP_ID")
    private Long groupId;
    @Basic(optional = false)
    @Column(name = "GROUP_NAME")
    private String groupName;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "ACTION")
    private String action;
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Basic(optional = false)
    @Column(name = "CREATED_ON")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;
    @Column(name = "INTRASH")
    private String intrash;
    @JoinColumn(name = "TENANT_ID", referencedColumnName = "ID")
    @ManyToOne
    private UfsOrganizationUnits tenantId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "workgroup")
    private Collection<UfsWorkgroupRole> ufsWorkgroupRoleCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "workgroup")
    private Collection<UfsUserWorkgroup> ufsUserWorkgroupCollection;

    public UfsWorkgroup() {
    }

    public UfsWorkgroup(Long groupId) {
        this.groupId = groupId;
    }

    public UfsWorkgroup(Long groupId, String groupName, Date createdOn) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.createdOn = createdOn;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
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

    public Collection<UfsWorkgroupRole> getUfsWorkgroupRoleCollection() {
        return ufsWorkgroupRoleCollection;
    }

    public void setUfsWorkgroupRoleCollection(Collection<UfsWorkgroupRole> ufsWorkgroupRoleCollection) {
        this.ufsWorkgroupRoleCollection = ufsWorkgroupRoleCollection;
    }

    public Collection<UfsUserWorkgroup> getUfsUserWorkgroupCollection() {
        return ufsUserWorkgroupCollection;
    }

    public void setUfsUserWorkgroupCollection(Collection<UfsUserWorkgroup> ufsUserWorkgroupCollection) {
        this.ufsUserWorkgroupCollection = ufsUserWorkgroupCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (groupId != null ? groupId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UfsWorkgroup)) {
            return false;
        }
        UfsWorkgroup other = (UfsWorkgroup) object;
        if ((this.groupId == null && other.groupId != null) || (this.groupId != null && !this.groupId.equals(other.groupId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.oracleufs.UfsWorkgroup[ groupId=" + groupId + " ]";
    }
    
}
