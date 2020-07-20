/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.tra.com.tsync.entities;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "UFS_USER_WORKGROUP")
@NamedQueries({
    @NamedQuery(name = "UfsUserWorkgroup.findAll", query = "SELECT u FROM UfsUserWorkgroup u")})
public class UfsUserWorkgroup implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "USR_GROUP_ID")
    private Long usrGroupId;
    @Column(name = "CREATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Column(name = "INTRASH")
    private String intrash;
    @JoinColumn(name = "WORKGROUP", referencedColumnName = "GROUP_ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private UfsWorkgroup workgroup;

    @Column(name = "WORKGROUP")
    private Long workgroupId;

    @JoinColumn(name = "USER_", referencedColumnName = "USER_ID", insertable = false,updatable = false)
    @ManyToOne(optional = false)
    private UfsUser user;

    @Column(name = "USER_")
    private Long userId;


    public UfsUserWorkgroup() {
    }

    public UfsUserWorkgroup(Long usrGroupId) {
        this.usrGroupId = usrGroupId;
    }

    public Long getUsrGroupId() {
        return usrGroupId;
    }

    public void setUsrGroupId(Long usrGroupId) {
        this.usrGroupId = usrGroupId;
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

    public UfsUser getUser() {
        return user;
    }

    public Long getWorkgroupId() {
        return workgroupId;
    }

    public void setWorkgroupId(Long workgroupId) {
        this.workgroupId = workgroupId;
    }

    public void setUser(UfsUser user) {
        this.user = user;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (usrGroupId != null ? usrGroupId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UfsUserWorkgroup)) {
            return false;
        }
        UfsUserWorkgroup other = (UfsUserWorkgroup) object;
        if ((this.usrGroupId == null && other.usrGroupId != null) || (this.usrGroupId != null && !this.usrGroupId.equals(other.usrGroupId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.oracleufs.UfsUserWorkgroup[ usrGroupId=" + usrGroupId + " ]";
    }
    
}
