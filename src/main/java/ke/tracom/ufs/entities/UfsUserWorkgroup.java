/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tracom.ufs.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author ASUS
 */
@Entity
@Table(name = "UFS_USER_WORKGROUP")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "UfsUserWorkgroup.findAll", query = "SELECT u FROM UfsUserWorkgroup u")
        , @NamedQuery(name = "UfsUserWorkgroup.findByUsrGroupId", query = "SELECT u FROM UfsUserWorkgroup u WHERE u.usrGroupId = :usrGroupId")
        , @NamedQuery(name = "UfsUserWorkgroup.findByCreationDate", query = "SELECT u FROM UfsUserWorkgroup u WHERE u.creationDate = :creationDate")
        , @NamedQuery(name = "UfsUserWorkgroup.findByIntrash", query = "SELECT u FROM UfsUserWorkgroup u WHERE u.intrash = :intrash")})
public class UfsUserWorkgroup implements Serializable {

    @Size(max = 3)
    @Column(name = "INTRASH",insertable = false)
    private String intrash;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GenericGenerator(
            name = "UFS_USER_WORKGROUP_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "UFS_USER_WORKGROUP_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "0"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )

    @GeneratedValue(generator = "UFS_USER_WORKGROUP_SEQ")
    @Column(name = "USR_GROUP_ID")
    private Long usrGroupId;
    @Column(name = "USER_")
    private Long userId;
    @Column(name = "WORKGROUP")
    private BigDecimal groupId;
    @Column(name = "CREATION_DATE", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @JoinColumn(name = "USER_", referencedColumnName = "USER_ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private UfsUser user;
    @JoinColumn(name = "WORKGROUP", referencedColumnName = "GROUP_ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private UfsWorkgroup workgroup;

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


    public UfsUser getUser() {
        return user;
    }

    public void setUser(UfsUser user) {
        this.user = user;
    }

    public UfsWorkgroup getWorkgroup() {
        return workgroup;
    }

    public BigDecimal getGroupId() {
        return groupId;
    }

    public void setGroupId(BigDecimal groupId) {
        this.groupId = groupId;
    }

    public void setWorkgroup(UfsWorkgroup workgroup) {
        this.workgroup = workgroup;
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
        return "ke.tracom.ufs.entities.UfsUserWorkgroup[ usrGroupId=" + usrGroupId + " ]";
    }

    public String getIntrash() {
        return intrash;
    }

    public void setIntrash(String intrash) {
        this.intrash = intrash;
    }

}
