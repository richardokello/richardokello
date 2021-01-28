/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tracom.ufs.entities;

import ke.axle.chassis.annotations.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author ASUS
 */
@NickName(name="Workgroup")
@Entity
@Table(name = "UFS_WORKGROUP")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "UfsWorkgroup.findAll", query = "SELECT u FROM UfsWorkgroup u")
        , @NamedQuery(name = "UfsWorkgroup.findByGroupId", query = "SELECT u FROM UfsWorkgroup u WHERE u.groupId = :groupId")
        , @NamedQuery(name = "UfsWorkgroup.findByGroupName", query = "SELECT u FROM UfsWorkgroup u WHERE u.groupName = :groupName")
        , @NamedQuery(name = "UfsWorkgroup.findByDescription", query = "SELECT u FROM UfsWorkgroup u WHERE u.description = :description")
        , @NamedQuery(name = "UfsWorkgroup.findByAction", query = "SELECT u FROM UfsWorkgroup u WHERE u.action = :action")
        , @NamedQuery(name = "UfsWorkgroup.findByActionStatus", query = "SELECT u FROM UfsWorkgroup u WHERE u.actionStatus = :actionStatus")
        , @NamedQuery(name = "UfsWorkgroup.findByCreatedOn", query = "SELECT u FROM UfsWorkgroup u WHERE u.createdOn = :createdOn")
        , @NamedQuery(name = "UfsWorkgroup.findByIntrash", query = "SELECT u FROM UfsWorkgroup u WHERE u.intrash = :intrash")})
public class UfsWorkgroup implements Serializable {

    @Basic(optional = false)
    @NotNull
    @ModifiableField
    @Unique
    @Size(min = 1, max = 50)
    @Column(name = "GROUP_NAME")
    @Searchable
    @EntityName
    private String groupName;
    @Size(max = 100)
    @Searchable
    @ModifiableField
    @Column(name = "DESCRIPTION")
    private String description;
    @Size(max = 15)
    @Filter
    @Searchable
    @Column(name = "ACTION")
    private String action;
    @Size(max = 15)
    @Filter
    @Searchable
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Size(max = 3)
    @Filter
    @Searchable
    @Column(name = "INTRASH",insertable = false)
    private String intrash;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GenericGenerator(
            name = "UFS_WORKGROUP_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "UFS_WORKGROUP_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "0"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )

    @GeneratedValue(generator = "UFS_WORKGROUP_SEQ")
    @Column(name = "GROUP_ID")
    private Long groupId;
    @Column(name = "CREATED_ON", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Filter(isDateRange = true)
    private Date createdOn;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "workgroup")
    private List<UfsWorkgroupRole> ufsWorkgroupRoleList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "workgroup")
    private List<UfsUserWorkgroup> ufsUserWorkgroupList;
    @JoinColumn(name = "TENANT_ID", referencedColumnName = "U_UID")
    @ManyToOne
    private UfsOrganizationUnits tenantId;

    @Transient
    @ModifiableField
    private List<Long> workgroupRolesIds;

    public UfsWorkgroup() {
    }

    public UfsWorkgroup(Long groupId) {
        this.groupId = groupId;
    }

    public UfsWorkgroup(Long groupId, String groupName) {
        this.groupId = groupId;
        this.groupName = groupName;
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


    @XmlTransient
    @com.fasterxml.jackson.annotation.JsonIgnore
    public List<UfsWorkgroupRole> getUfsWorkgroupRoleList() {
        return ufsWorkgroupRoleList;
    }

    public void setUfsWorkgroupRoleList(List<UfsWorkgroupRole> ufsWorkgroupRoleList) {
        this.ufsWorkgroupRoleList = ufsWorkgroupRoleList;
    }

    @XmlTransient
    @com.fasterxml.jackson.annotation.JsonIgnore
    public List<UfsUserWorkgroup> getUfsUserWorkgroupList() {
        return ufsUserWorkgroupList;
    }

    public void setUfsUserWorkgroupList(List<UfsUserWorkgroup> ufsUserWorkgroupList) {
        this.ufsUserWorkgroupList = ufsUserWorkgroupList;
    }

    public UfsOrganizationUnits getTenantId() {
        return tenantId;
    }

    public void setTenantId(UfsOrganizationUnits tenantId) {
        this.tenantId = tenantId;
    }

    public List<Long> getWorkgroupRolesIds() {
        return workgroupRolesIds;
    }

    public void setWorkgroupRolesIds(List<Long> workgroupRolesIds) {
        this.workgroupRolesIds = workgroupRolesIds;
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
        return "ke.tracom.ufs.entities.UfsWorkgroup[ groupId=" + groupId + " ]";
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
