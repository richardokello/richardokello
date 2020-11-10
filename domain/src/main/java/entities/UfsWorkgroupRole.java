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
@Table(name = "UFS_WORKGROUP_ROLE")
@NamedQueries({
    @NamedQuery(name = "UfsWorkgroupRole.findAll", query = "SELECT u FROM UfsWorkgroupRole u")})
public class UfsWorkgroupRole implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "WRK_WRKGRP_ROLE")
    private Long wrkWrkgrpRole;
    @Basic(optional = false)
    @Column(name = "CREATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Column(name = "INTRASH")
    private String intrash;
    @JoinColumn(name = "WORKGROUP", referencedColumnName = "GROUP_ID",insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private UfsWorkgroup workgroup;

    @Column(name = "WORKGROUP")
    private Long groupId;
    @JoinColumn(name = "ROLE", referencedColumnName = "ROLE_ID")
    @ManyToOne(optional = false)
    private UfsRole role;

    public UfsWorkgroupRole() {
    }

    public UfsWorkgroupRole(Long wrkWrkgrpRole) {
        this.wrkWrkgrpRole = wrkWrkgrpRole;
    }

    public UfsWorkgroupRole(Long wrkWrkgrpRole, Date creationDate) {
        this.wrkWrkgrpRole = wrkWrkgrpRole;
        this.creationDate = creationDate;
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

    public String getIntrash() {
        return intrash;
    }

    public void setIntrash(String intrash) {
        this.intrash = intrash;
    }

    public UfsWorkgroup getWorkgroup() {
        return workgroup;
    }

    public void setWorkgroup(UfsWorkgroup workgroup) {
        this.workgroup = workgroup;
    }

    public UfsRole getRole() {
        return role;
    }

    public void setRole(UfsRole role) {
        this.role = role;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
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
        return "com.mycompany.oracleufs.UfsWorkgroupRole[ wrkWrkgrpRole=" + wrkWrkgrpRole + " ]";
    }
    
}
