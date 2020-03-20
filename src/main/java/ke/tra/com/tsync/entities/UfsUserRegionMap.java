/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.tra.com.tsync.entities;

import java.io.Serializable;
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
@Table(name = "UFS_USER_REGION_MAP")
@NamedQueries({
    @NamedQuery(name = "UfsUserRegionMap.findAll", query = "SELECT u FROM UfsUserRegionMap u")})
public class UfsUserRegionMap implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")
    @ManyToOne(optional = false)
    private UfsUser userId;
    @JoinColumn(name = "REGION_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private UfsBankRegion regionId;

    public UfsUserRegionMap() {
    }

    public UfsUserRegionMap(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UfsUser getUserId() {
        return userId;
    }

    public void setUserId(UfsUser userId) {
        this.userId = userId;
    }

    public UfsBankRegion getRegionId() {
        return regionId;
    }

    public void setRegionId(UfsBankRegion regionId) {
        this.regionId = regionId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UfsUserRegionMap)) {
            return false;
        }
        UfsUserRegionMap other = (UfsUserRegionMap) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.oracleufs.UfsUserRegionMap[ id=" + id + " ]";
    }
    
}
