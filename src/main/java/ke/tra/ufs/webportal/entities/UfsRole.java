/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.entities;


import ke.axle.chassis.annotations.ModifiableField;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author ASUS
 */
@Entity
@Table(name = "UFS_ROLE", catalog = "", schema = "UFS_SMART_SUITE")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "UfsRole.findAll", query = "SELECT u FROM UfsRole u")
        , @NamedQuery(name = "UfsRole.findByRoleId", query = "SELECT u FROM UfsRole u WHERE u.roleId = :roleId")
        , @NamedQuery(name = "UfsRole.findByRoleName", query = "SELECT u FROM UfsRole u WHERE u.roleName = :roleName")
        , @NamedQuery(name = "UfsRole.findByDescription", query = "SELECT u FROM UfsRole u WHERE u.description = :description")
        , @NamedQuery(name = "UfsRole.findByCreationDate", query = "SELECT u FROM UfsRole u WHERE u.creationDate = :creationDate")
        , @NamedQuery(name = "UfsRole.findByAction", query = "SELECT u FROM UfsRole u WHERE u.action = :action")
        , @NamedQuery(name = "UfsRole.findByActionStatus", query = "SELECT u FROM UfsRole u WHERE u.actionStatus = :actionStatus")
        , @NamedQuery(name = "UfsRole.findByIntrash", query = "SELECT u FROM UfsRole u WHERE u.intrash = :intrash")})
public class UfsRole implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "ROLE_NAME")
    private String roleName;
    @Size(max = 100)
    @Column(name = "DESCRIPTION")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CREATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Size(max = 15)
    @Column(name = "ACTION")
    private String action;
    @Size(max = 15)
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Size(max = 3)
    @Column(name = "INTRASH")
    private String intrash;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ROLE_ID")
    private Long roleId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "role")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private List<UfsRolePermission> ufsRolePermissionList;
    @JoinColumn(name = "TENANT_ID", referencedColumnName = "ID")
    @ManyToOne
    @com.fasterxml.jackson.annotation.JsonIgnore
    private UfsOrganizationUnits tenantId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "role")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private List<UfsWorkgroupRole> ufsWorkgroupRoleList;

    @Transient
    @ModifiableField
    private Set<Short> permissions;

    public UfsRole() {
    }

    public UfsRole(Long roleId) {
        this.roleId = roleId;
    }

    public UfsRole(Long roleId, String roleName, Date creationDate) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.creationDate = creationDate;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }


    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }


    public String getActionStatus() {
        return actionStatus;
    }

    public void setActionStatus(String actionStatus) {
        this.actionStatus = actionStatus;
    }


    @XmlTransient
    public List<UfsRolePermission> getUfsRolePermissionList() {
        return ufsRolePermissionList;
    }

    public void setUfsRolePermissionList(List<UfsRolePermission> ufsRolePermissionList) {
        this.ufsRolePermissionList = ufsRolePermissionList;
    }

    public UfsOrganizationUnits getTenantId() {
        return tenantId;
    }

    public void setTenantId(UfsOrganizationUnits tenantId) {
        this.tenantId = tenantId;
    }

    @XmlTransient
    public List<UfsWorkgroupRole> getUfsWorkgroupRoleList() {
        return ufsWorkgroupRoleList;
    }

    public void setUfsWorkgroupRoleList(List<UfsWorkgroupRole> ufsWorkgroupRoleList) {
        this.ufsWorkgroupRoleList = ufsWorkgroupRoleList;
    }

    public Set<Short> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Short> permissions) {
        this.permissions = permissions;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roleId != null ? roleId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UfsRole)) {
            return false;
        }
        UfsRole other = (UfsRole) object;
        if ((this.roleId == null && other.roleId != null) || (this.roleId != null && !this.roleId.equals(other.roleId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ke.tracom.ufs.entities.UfsRole[ roleId=" + roleId + " ]";
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

    public String getIntrash() {
        return intrash;
    }

    public void setIntrash(String intrash) {
        this.intrash = intrash;
    }

}
