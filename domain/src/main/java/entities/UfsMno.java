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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Mwagiru Kamoni
 */
@Entity
@Table(name = "UFS_MNO")
@NamedQueries({
    @NamedQuery(name = "UfsMno.findAll", query = "SELECT u FROM UfsMno u")})
public class UfsMno implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "MNO_ID")
    private BigDecimal mnoId;
    @Basic(optional = false)
    @Column(name = "MNO_NAME")
    private String mnoName;
    @Basic(optional = false)
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "ACTION")
    private String action;
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Column(name = "INTRASH")
    private String intrash;

    public UfsMno() {
    }

    public UfsMno(BigDecimal mnoId) {
        this.mnoId = mnoId;
    }

    public UfsMno(BigDecimal mnoId, String mnoName, String description) {
        this.mnoId = mnoId;
        this.mnoName = mnoName;
        this.description = description;
    }

    public BigDecimal getMnoId() {
        return mnoId;
    }

    public void setMnoId(BigDecimal mnoId) {
        this.mnoId = mnoId;
    }

    public String getMnoName() {
        return mnoName;
    }

    public void setMnoName(String mnoName) {
        this.mnoName = mnoName;
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
        hash += (mnoId != null ? mnoId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UfsMno)) {
            return false;
        }
        UfsMno other = (UfsMno) object;
        if ((this.mnoId == null && other.mnoId != null) || (this.mnoId != null && !this.mnoId.equals(other.mnoId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.oracleufs.UfsMno[ mnoId=" + mnoId + " ]";
    }
    
}
