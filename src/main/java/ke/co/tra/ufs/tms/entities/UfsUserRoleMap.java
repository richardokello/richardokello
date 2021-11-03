/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Owori Juma
 */
@Entity
@Table(name = "UFS_USER_ROLE_MAP")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UfsUserRoleMap.findAll", query = "SELECT u FROM UfsUserRoleMap u")
    , @NamedQuery(name = "UfsUserRoleMap.findByUserRoleMapId", query = "SELECT u FROM UfsUserRoleMap u WHERE u.userRoleMapId = :userRoleMapId")
    , @NamedQuery(name = "UfsUserRoleMap.findByUserId", query = "SELECT u FROM UfsUserRoleMap u WHERE u.userId = :userId")
    , @NamedQuery(name = "UfsUserRoleMap.findByRoleId", query = "SELECT u FROM UfsUserRoleMap u WHERE u.roleId = :roleId")
    , @NamedQuery(name = "UfsUserRoleMap.findByIntrash", query = "SELECT u FROM UfsUserRoleMap u WHERE u.intrash = :intrash")})
public class UfsUserRoleMap implements Serializable {

    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JsonIgnore    
    @org.codehaus.jackson.annotate.JsonIgnore
    private UfsUser user;
    @JoinColumn(name = "ROLE_ID", referencedColumnName = "ROLE_ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)    
    @org.codehaus.jackson.annotate.JsonIgnore
    private UfsUserRole role;

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @SequenceGenerator(name = "UFS_USER_ROLE_MAP_SEQ", sequenceName = "UFS_USER_ROLE_MAP_SEQ")
    @GeneratedValue(generator = "UFS_USER_ROLE_MAP_SEQ")
    @Basic(optional = false)
    @NotNull
    @Column(name = "USER_ROLE_MAP_ID")
    private BigDecimal userRoleMapId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "USER_ID")
    private BigInteger userId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ROLE_ID")
    private BigInteger roleId;
    @Size(max = 5)
    @Column(name = "INTRASH")
    private String intrash;

    public UfsUserRoleMap() {
    }

    public UfsUserRoleMap(BigDecimal userRoleMapId) {
        this.userRoleMapId = userRoleMapId;
    }

    public UfsUserRoleMap(BigInteger userId, BigInteger roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }

    public UfsUserRoleMap(String intrash,BigInteger userId, BigDecimal userRoleMapId) {
        this.userRoleMapId = userRoleMapId;
        this.userId = userId;
        this.intrash = intrash;
    }    
    
    public UfsUserRoleMap(BigDecimal userRoleMapId, BigInteger userId, BigInteger roleId) {
        this.userRoleMapId = userRoleMapId;
        this.userId = userId;
        this.roleId = roleId;
    }

    public BigDecimal getUserRoleMapId() {
        return userRoleMapId;
    }

    public void setUserRoleMapId(BigDecimal userRoleMapId) {
        this.userRoleMapId = userRoleMapId;
    }

    public BigInteger getUserId() {
        return userId;
    }

    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }

    public BigInteger getRoleId() {
        return roleId;
    }

    public void setRoleId(BigInteger roleId) {
        this.roleId = roleId;
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
        hash += (userRoleMapId != null ? userRoleMapId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UfsUserRoleMap)) {
            return false;
        }
        UfsUserRoleMap other = (UfsUserRoleMap) object;
        if ((this.userRoleMapId == null && other.userRoleMapId != null) || (this.userRoleMapId != null && !this.userRoleMapId.equals(other.userRoleMapId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ke.co.tra.ufs.tms.entities.UfsUserRoleMap[ userRoleMapId=" + userRoleMapId + " ]";
    }

    public UfsUser getUser() {
        return user;
    }

    public void setUser(UfsUser user) {
        this.user = user;
    }

    public UfsUserRole getRole() {
        return role;
    }

    public void setRole(UfsUserRole role) {
        this.role = role;
    }
    
}
