/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 * @author ASUS
 */
@Entity
@Table(name = "UFS_WORKGROUP_ROLE")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "UfsWorkgroupRole.findAll", query = "SELECT u FROM UfsWorkgroupRole u")
        , @NamedQuery(name = "UfsWorkgroupRole.findByWrkWrkgrpRole", query = "SELECT u FROM UfsWorkgroupRole u WHERE u.wrkWrkgrpRole = :wrkWrkgrpRole")
        , @NamedQuery(name = "UfsWorkgroupRole.findByCreationDate", query = "SELECT u FROM UfsWorkgroupRole u WHERE u.creationDate = :creationDate")
        , @NamedQuery(name = "UfsWorkgroupRole.findByIntrash", query = "SELECT u FROM UfsWorkgroupRole u WHERE u.intrash = :intrash")})
public class UfsWorkgroupRole implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "CREATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Size(max = 3)
    @Column(name = "INTRASH")
    private String intrash;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "WRK_WRKGRP_ROLE")
    private Long wrkWrkgrpRole;
    @Column(name = "WORKGROUP")
    private Long groupId;
    @Column(name = "ROLE")
    private Long roleId;
    @JoinColumn(name = "ROLE", referencedColumnName = "ROLE_ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private UfsRole role;
    @JoinColumn(name = "WORKGROUP", referencedColumnName = "GROUP_ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private UfsWorkgroup workgroup;

    public UfsWorkgroupRole() {
    }

    public UfsWorkgroupRole(Long wrkWrkgrpRole) {
        this.wrkWrkgrpRole = wrkWrkgrpRole;
    }

    public UfsWorkgroupRole(Long wrkWrkgrpRole, Date creationDate) {
        this.wrkWrkgrpRole = wrkWrkgrpRole;
        this.creationDate = creationDate;
    }
    public UfsWorkgroupRole(Long groupId, Long roleId) {
        this.groupId = groupId;
        this.roleId = roleId;
    }
    public Long getWrkWrkgrpRole() {
        return wrkWrkgrpRole;
    }

    public void setWrkWrkgrpRole(Long wrkWrkgrpRole) {
        this.wrkWrkgrpRole = wrkWrkgrpRole;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }


    public UfsRole getRole() {
        return role;
    }

    public void setRole(UfsRole role) {
        this.role = role;
    }

    public UfsWorkgroup getWorkgroup() {
        return workgroup;
    }

    public void setWorkgroup(UfsWorkgroup workgroup) {
        this.workgroup = workgroup;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (wrkWrkgrpRole != null ? wrkWrkgrpRole.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UfsWorkgroupRole)) {
            return false;
        }
        UfsWorkgroupRole other = (UfsWorkgroupRole) object;
        if ((this.wrkWrkgrpRole == null && other.wrkWrkgrpRole != null) || (this.wrkWrkgrpRole != null && !this.wrkWrkgrpRole.equals(other.wrkWrkgrpRole))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ke.tracom.ufs.entities.UfsWorkgroupRole[ wrkWrkgrpRole=" + wrkWrkgrpRole + " ]";
    }

    public String getIntrash() {
        return intrash;
    }

    public void setIntrash(String intrash) {
        this.intrash = intrash;
    }

}
