/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Mwagiru Kamoni
 */
@Entity
@Table(name = "UFS_USER_ROLE_MAP")
@NamedQueries({
    @NamedQuery(name = "UfsUserRoleMap.findAll", query = "SELECT u FROM UfsUserRoleMap u")})
public class UfsUserRoleMap implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "USER_ROLE_MAP_ID")
    private BigDecimal userRoleMapId;
    @Column(name = "INTRASH")
    private String intrash;
    @JoinColumn(name = "ROLE_ID", referencedColumnName = "ROLE_ID")
    @ManyToOne(optional = false)
    private UfsUserRole roleId;
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")
    @ManyToOne(optional = false)
    private UfsUser userId;

    public UfsUserRoleMap() {
    }

    public UfsUserRoleMap(BigDecimal userRoleMapId) {
        this.userRoleMapId = userRoleMapId;
    }

    public BigDecimal getUserRoleMapId() {
        return userRoleMapId;
    }

    public void setUserRoleMapId(BigDecimal userRoleMapId) {
        this.userRoleMapId = userRoleMapId;
    }

    public String getIntrash() {
        return intrash;
    }

    public void setIntrash(String intrash) {
        this.intrash = intrash;
    }

    public UfsUserRole getRoleId() {
        return roleId;
    }

    public void setRoleId(UfsUserRole roleId) {
        this.roleId = roleId;
    }

    public UfsUser getUserId() {
        return userId;
    }

    public void setUserId(UfsUser userId) {
        this.userId = userId;
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
        return "com.mycompany.oracleufs.UfsUserRoleMap[ userRoleMapId=" + userRoleMapId + " ]";
    }
    
}
