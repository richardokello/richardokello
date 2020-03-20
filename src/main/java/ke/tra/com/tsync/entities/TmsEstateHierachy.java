/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.tra.com.tsync.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "TMS_ESTATE_HIERACHY")
@NamedQueries({
    @NamedQuery(name = "TmsEstateHierachy.findAll", query = "SELECT t FROM TmsEstateHierachy t")})
public class TmsEstateHierachy implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "UNIT_ID")
    private BigDecimal unitId;
    @Basic(optional = false)
    @Column(name = "PRODUCT_ID")
    private BigInteger productId;
    @Basic(optional = false)
    @Column(name = "LEVEL_NO")
    private BigInteger levelNo;
    @Basic(optional = false)
    @Column(name = "UNIT_NAME")
    private String unitName;
    @Basic(optional = false)
    @Column(name = "STATUS")
    private String status;
    @Column(name = "CREATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Column(name = "ACTION")
    private String action;
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Column(name = "INTRASH")
    private String intrash;

    public TmsEstateHierachy() {
    }

    public TmsEstateHierachy(BigDecimal unitId) {
        this.unitId = unitId;
    }

    public TmsEstateHierachy(BigDecimal unitId, BigInteger productId, BigInteger levelNo, String unitName, String status) {
        this.unitId = unitId;
        this.productId = productId;
        this.levelNo = levelNo;
        this.unitName = unitName;
        this.status = status;
    }

    public BigDecimal getUnitId() {
        return unitId;
    }

    public void setUnitId(BigDecimal unitId) {
        this.unitId = unitId;
    }

    public BigInteger getProductId() {
        return productId;
    }

    public void setProductId(BigInteger productId) {
        this.productId = productId;
    }

    public BigInteger getLevelNo() {
        return levelNo;
    }

    public void setLevelNo(BigInteger levelNo) {
        this.levelNo = levelNo;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
        hash += (unitId != null ? unitId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TmsEstateHierachy)) {
            return false;
        }
        TmsEstateHierachy other = (TmsEstateHierachy) object;
        if ((this.unitId == null && other.unitId != null) || (this.unitId != null && !this.unitId.equals(other.unitId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.oracleufs.TmsEstateHierachy[ unitId=" + unitId + " ]";
    }
    
}
