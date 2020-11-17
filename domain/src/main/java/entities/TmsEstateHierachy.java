/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Mwagiru Kamoni
 */
@Entity
@Table(name = "TMS_ESTATE_HIERACHY")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "TmsEstateHierachy.findAll", query = "SELECT t FROM TmsEstateHierachy t"),
        @NamedQuery(name = "TmsEstateHierachy.findByUnitId", query = "SELECT t FROM TmsEstateHierachy t WHERE t.unitId = :unitId"),
        @NamedQuery(name = "TmsEstateHierachy.findByLevelNo", query = "SELECT t FROM TmsEstateHierachy t WHERE t.levelNo = :levelNo"),
        @NamedQuery(name = "TmsEstateHierachy.findByUnitName", query = "SELECT t FROM TmsEstateHierachy t WHERE t.unitName = :unitName"),
        @NamedQuery(name = "TmsEstateHierachy.findByStatus", query = "SELECT t FROM TmsEstateHierachy t WHERE t.status = :status"),
        @NamedQuery(name = "TmsEstateHierachy.findByCreationDate", query = "SELECT t FROM TmsEstateHierachy t WHERE t.creationDate = :creationDate"),
        @NamedQuery(name = "TmsEstateHierachy.findByAction", query = "SELECT t FROM TmsEstateHierachy t WHERE t.action = :action"),
        @NamedQuery(name = "TmsEstateHierachy.findByActionStatus", query = "SELECT t FROM TmsEstateHierachy t WHERE t.actionStatus = :actionStatus"),
        @NamedQuery(name = "TmsEstateHierachy.findByIntrash", query = "SELECT t FROM TmsEstateHierachy t WHERE t.intrash = :intrash")})
public class TmsEstateHierachy implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "UNIT_ID")
    private BigDecimal unitId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "LEVEL_NO")
    private BigInteger levelNo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "UNIT_NAME")
    private String unitName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "STATUS")
    private String status;
    @Column(name = "CREATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Size(max = 10)
    @Column(name = "ACTION")
    private String action;
    @Size(max = 10)
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Size(max = 5)
    @Column(name = "INTRASH")
    private String intrash;
    @JoinColumn(name = "PRODUCT_ID", referencedColumnName = "PRODUCT_ID")
    @ManyToOne(optional = false)
    private UfsProduct productId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "unitId")
    private Collection<TmsEstateItem> tmsEstateItemCollection;

    public TmsEstateHierachy() {
    }

    public TmsEstateHierachy(BigDecimal unitId) {
        this.unitId = unitId;
    }

    public TmsEstateHierachy(BigDecimal unitId, BigInteger levelNo, String unitName, String status) {
        this.unitId = unitId;
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

    public UfsProduct getProductId() {
        return productId;
    }

    public void setProductId(UfsProduct productId) {
        this.productId = productId;
    }

    @XmlTransient
    @JsonIgnore
    public Collection<TmsEstateItem> getTmsEstateItemCollection() {
        return tmsEstateItemCollection;
    }

    public void setTmsEstateItemCollection(Collection<TmsEstateItem> tmsEstateItemCollection) {
        this.tmsEstateItemCollection = tmsEstateItemCollection;
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
        return "TmsEstateHierachy[ unitId=" + unitId + " ]";
    }

}
