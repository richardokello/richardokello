/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import ke.co.tra.ufs.tms.utils.annotations.ModifiableField;

/**
 *
 * @author tracom9
 */
@Entity
@Table(name = "UFS_USER_ROLE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UfsUserRole.findAll", query = "SELECT u FROM UfsUserRole u")
    , @NamedQuery(name = "UfsUserRole.findByRoleId", query = "SELECT u FROM UfsUserRole u WHERE u.roleId = :roleId")
    , @NamedQuery(name = "UfsUserRole.findByRoleName", query = "SELECT u FROM UfsUserRole u WHERE u.roleName = :roleName")
    , @NamedQuery(name = "UfsUserRole.findByDescription", query = "SELECT u FROM UfsUserRole u WHERE u.description = :description")
    , @NamedQuery(name = "UfsUserRole.findByCreationDate", query = "SELECT u FROM UfsUserRole u WHERE u.creationDate = :creationDate")
    , @NamedQuery(name = "UfsUserRole.findByAction", query = "SELECT u FROM UfsUserRole u WHERE u.action = :action")
    , @NamedQuery(name = "UfsUserRole.findByActionStatus", query = "SELECT u FROM UfsUserRole u WHERE u.actionStatus = :actionStatus")
    , @NamedQuery(name = "UfsUserRole.findByIntrash", query = "SELECT u FROM UfsUserRole u WHERE u.intrash = :intrash")})
public class UfsUserRole implements Serializable {

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "role", fetch = FetchType.LAZY)    
    @org.codehaus.jackson.annotate.JsonIgnore
    private Set<UfsUserRoleMap> userRoleMaps;

    @JsonIgnore
    @org.codehaus.jackson.annotate.JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "roleId", fetch = FetchType.LAZY)
    private List<UfsRolePermissionMap> permissionMaps;

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @SequenceGenerator(name = "UFS_SEQ_ROLE_ID", sequenceName = "UFS_SEQ_ROLE_ID")
    @GeneratedValue(generator = "UFS_SEQ_ROLE_ID")
    @Basic(optional = false)
    @Column(name = "ROLE_ID")
    private BigDecimal roleId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "ROLE_NAME")
    @ModifiableField
    private String roleName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "DESCRIPTION")
    @ModifiableField
    private String description;
    @Basic(optional = false)
    @Column(name = "CREATION_DATE", updatable = false, insertable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Basic(optional = false)
    @Size(min = 1, max = 10)
    @Column(name = "ACTION")
    private String action;
    @Basic(optional = false)
    @Size(min = 1, max = 10)
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Size(max = 5)
    @Column(name = "INTRASH")
    private String intrash;

    @Transient
    @Size(min = 0, max = 1000)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<BigDecimal> rolePermissions;

    public UfsUserRole() {
    }

    public UfsUserRole(BigDecimal roleId) {
        this.roleId = roleId;
    }

    public UfsUserRole(BigDecimal roleId, String roleName, String description, Date creationDate, String action, String actionStatus) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.description = description;
        this.creationDate = creationDate;
        this.action = action;
        this.actionStatus = actionStatus;
    }

    public BigDecimal getRoleId() {
        return roleId;
    }

    public void setRoleId(BigDecimal roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getIntrash() {
        return intrash;
    }

    public void setIntrash(String intrash) {
        this.intrash = intrash;
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
        if (!(object instanceof UfsUserRole)) {
            return false;
        }
        UfsUserRole other = (UfsUserRole) object;
        if ((this.roleId == null && other.roleId != null) || (this.roleId != null && !this.roleId.equals(other.roleId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ke.co.tra.ufs.tms.entities.UfsUserRole[ roleId=" + roleId + " ]";
    }

    public List<UfsRolePermissionMap> getPermissionMaps() {
        return permissionMaps;
    }

    public void setPermissionMaps(List<UfsRolePermissionMap> permissionMaps) {
        this.permissionMaps = permissionMaps;
    }

    public Set<UfsUserRoleMap> getUserRoleMaps() {
        return userRoleMaps;
    }

    public void setUserRoleMaps(Set<UfsUserRoleMap> userRoleMaps) {
        this.userRoleMaps = userRoleMaps;
    }

    public List<BigDecimal> getRolePermissions() {
        return rolePermissions;
    }

    public void setRolePermissions(List<BigDecimal> rolePermissions) {
        this.rolePermissions = rolePermissions;
    }

}
