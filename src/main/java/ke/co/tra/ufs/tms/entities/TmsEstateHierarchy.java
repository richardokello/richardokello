/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * @author tracom9
 */
@Entity
@Table(name = "TMS_ESTATE_HIERACHY")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "TmsEstateHierarchy.findAll", query = "SELECT t FROM TmsEstateHierarchy t")
        , @NamedQuery(name = "TmsEstateHierarchy.findByUnitId", query = "SELECT t FROM TmsEstateHierarchy t WHERE t.unitId = :unitId")
        , @NamedQuery(name = "TmsEstateHierarchy.findByProductId", query = "SELECT t FROM TmsEstateHierarchy t WHERE t.productId = :productId")
        , @NamedQuery(name = "TmsEstateHierarchy.findByLevelNo", query = "SELECT t FROM TmsEstateHierarchy t WHERE t.levelNo = :levelNo")
        , @NamedQuery(name = "TmsEstateHierarchy.findByUnitName", query = "SELECT t FROM TmsEstateHierarchy t WHERE t.unitName = :unitName")
        , @NamedQuery(name = "TmsEstateHierarchy.findByStatus", query = "SELECT t FROM TmsEstateHierarchy t WHERE t.status = :status")
        , @NamedQuery(name = "TmsEstateHierarchy.findByCreationDate", query = "SELECT t FROM TmsEstateHierarchy t WHERE t.creationDate = :creationDate")
        , @NamedQuery(name = "TmsEstateHierarchy.findByAction", query = "SELECT t FROM TmsEstateHierarchy t WHERE t.action = :action")
        , @NamedQuery(name = "TmsEstateHierarchy.findByActionStatus", query = "SELECT t FROM TmsEstateHierarchy t WHERE t.actionStatus = :actionStatus")
        , @NamedQuery(name = "TmsEstateHierarchy.findByIntrash", query = "SELECT t FROM TmsEstateHierarchy t WHERE t.intrash = :intrash")})
public class TmsEstateHierarchy implements Serializable {

    @JoinColumn(name = "PRODUCT_ID", referencedColumnName = "PRODUCT_ID")
    @ManyToOne(optional = false)
    private UfsProduct productId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "unitId", fetch = FetchType.LAZY)
    private List<TmsEstateItem> tmsEstateItemList;

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @GenericGenerator(
            name = "TMS_BUSINESS_UNIT_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "TMS_BUSINESS_UNIT_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "0"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )

    @GeneratedValue(generator = "TMS_BUSINESS_UNIT_SEQ")
    @Basic(optional = false)
    @Column(name = "UNIT_ID")//
    private BigDecimal unitId;
    @Basic(optional = false)
    @Column(name = "LEVEL_NO")
    private BigInteger levelNo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "UNIT_NAME")
    private String unitName;
    @Basic(optional = false)
    @Size(min = 1, max = 10)
    @Column(name = "STATUS")
    private String status;
    @Column(name = "CREATION_DATE", insertable = false, updatable = false)
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

    public TmsEstateHierarchy() {
    }

    public TmsEstateHierarchy(BigDecimal unitId) {
        this.unitId = unitId;
    }

    public TmsEstateHierarchy(BigDecimal unitId, UfsProduct productId, BigInteger levelNo, String unitName, String status) {
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
        if (!(object instanceof TmsEstateHierarchy)) {
            return false;
        }
        TmsEstateHierarchy other = (TmsEstateHierarchy) object;
        if ((this.unitId == null && other.unitId != null) || (this.unitId != null && !this.unitId.equals(other.unitId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ke.co.tra.ufs.tms.entities.TmsEstateHierarchy[ unitId=" + unitId + " ]";
    }

    public UfsProduct getProductId() {
        return productId;
    }

    public void setProductId(UfsProduct productId) {
        this.productId = productId;
    }

    @XmlTransient
    @JsonIgnore
    public List<TmsEstateItem> getTmsEstateItemList() {
        return tmsEstateItemList;
    }

    public void setTmsEstateItemList(List<TmsEstateItem> tmsEstateItemList) {
        this.tmsEstateItemList = tmsEstateItemList;
    }

}
