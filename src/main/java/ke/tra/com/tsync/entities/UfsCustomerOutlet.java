/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.tra.com.tsync.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Collection;
import javax.xml.bind.annotation.XmlTransient;
import ke.axle.chassis.annotations.Filter;
import ke.axle.chassis.annotations.ModifiableField;
import ke.axle.chassis.annotations.Searchable;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author Tracom
 */

@Entity
@Table(name = "UFS_CUSTOMER_OUTLET")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "UfsCustomerOutlet.findAll", query = "SELECT u FROM UfsCustomerOutlet u"),
        @NamedQuery(name = "UfsCustomerOutlet.findById", query = "SELECT u FROM UfsCustomerOutlet u WHERE u.id = :id"),
        @NamedQuery(name = "UfsCustomerOutlet.findByOutletName", query = "SELECT u FROM UfsCustomerOutlet u WHERE u.outletName = :outletName"),
        @NamedQuery(name = "UfsCustomerOutlet.findByOutletCode", query = "SELECT u FROM UfsCustomerOutlet u WHERE u.outletCode = :outletCode"),
        @NamedQuery(name = "UfsCustomerOutlet.findByCreatedAt", query = "SELECT u FROM UfsCustomerOutlet u WHERE u.createdAt = :createdAt"),
        @NamedQuery(name = "UfsCustomerOutlet.findByAction", query = "SELECT u FROM UfsCustomerOutlet u WHERE u.action = :action"),
        @NamedQuery(name = "UfsCustomerOutlet.findByActionStatus", query = "SELECT u FROM UfsCustomerOutlet u WHERE u.actionStatus = :actionStatus"),
        @NamedQuery(name = "UfsCustomerOutlet.findByIntrash", query = "SELECT u FROM UfsCustomerOutlet u WHERE u.intrash = :intrash"),
        @NamedQuery(name = "UfsCustomerOutlet.findByLongitude", query = "SELECT u FROM UfsCustomerOutlet u WHERE u.longitude = :longitude"),
        @NamedQuery(name = "UfsCustomerOutlet.findByOperatingHours", query = "SELECT u FROM UfsCustomerOutlet u WHERE u.operatingHours = :operatingHours"),
        @NamedQuery(name = "UfsCustomerOutlet.findByLatitude", query = "SELECT u FROM UfsCustomerOutlet u WHERE u.latitude = :latitude")})
public class UfsCustomerOutlet implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GenericGenerator(
            name = "CUSTOMER_OUTLET_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "CUSTOMER_OUTLET_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "0"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @GeneratedValue(generator = "CUSTOMER_OUTLET_SEQ")
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "OUTLET_NAME")
    private String outletName;
    @Size(max = 15)
    @Column(name = "OUTLET_CODE")
    private String outletCode;
    @Column(name = "CREATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Size(max = 15)
    @Column(name = "ACTION")
    private String action;
    @Size(max = 15)
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Size(max = 3)
    @Column(name = "INTRASH")
    private String intrash;
    @Size(max = 30)
    @Column(name = "LONGITUDE")
    private String longitude;
    @Size(max = 4000)
    @Column(name = "OPERATING_HOURS")
    private String operatingHours;
    @Size(max = 30)
    @Column(name = "LATITUDE")
    private String latitude;
    @JoinColumn(name = "BANK_BRANCH_ID", referencedColumnName = "ID")
    @ManyToOne
    private UfsBankBranches bankBranchId;
    @JoinColumn(name = "CUSTOMER_ID", referencedColumnName = "ID")
    @ManyToOne
    private UfsCustomer customerId;
    @JoinColumn(name = "GEOGRAPHICAL_REGION_ID", referencedColumnName = "ID")
    @ManyToOne
    private UfsGeographicalRegion geographicalRegionId;
    @OneToMany(mappedBy = "outletId")
    private Collection<TmsDevice> tmsDeviceCollection;
    @OneToMany(mappedBy = "outletId")
    private Collection<UfsContactPerson> ufsContactPersonCollection;

    public UfsCustomerOutlet() {
    }

    public UfsCustomerOutlet(Long id) {
        this.id = id;
    }

    public UfsCustomerOutlet(Long id, String outletName) {
        this.id = id;
        this.outletName = outletName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOutletName() {
        return outletName;
    }

    public void setOutletName(String outletName) {
        this.outletName = outletName;
    }

    public String getOutletCode() {
        return outletCode;
    }

    public void setOutletCode(String outletCode) {
        this.outletCode = outletCode;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
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

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getOperatingHours() {
        return operatingHours;
    }

    public void setOperatingHours(String operatingHours) {
        this.operatingHours = operatingHours;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public UfsBankBranches getBankBranchId() {
        return bankBranchId;
    }

    public void setBankBranchId(UfsBankBranches bankBranchId) {
        this.bankBranchId = bankBranchId;
    }

    public UfsCustomer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UfsCustomer customerId) {
        this.customerId = customerId;
    }

    public UfsGeographicalRegion getGeographicalRegionId() {
        return geographicalRegionId;
    }

    public void setGeographicalRegionId(UfsGeographicalRegion geographicalRegionId) {
        this.geographicalRegionId = geographicalRegionId;
    }

    @XmlTransient
    @JsonIgnore
    public Collection<TmsDevice> getTmsDeviceCollection() {
        return tmsDeviceCollection;
    }

    public void setTmsDeviceCollection(Collection<TmsDevice> tmsDeviceCollection) {
        this.tmsDeviceCollection = tmsDeviceCollection;
    }

    @XmlTransient
    @JsonIgnore
    public Collection<UfsContactPerson> getUfsContactPersonCollection() {
        return ufsContactPersonCollection;
    }

    public void setUfsContactPersonCollection(Collection<UfsContactPerson> ufsContactPersonCollection) {
        this.ufsContactPersonCollection = ufsContactPersonCollection;
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
        if (!(object instanceof UfsCustomerOutlet)) {
            return false;
        }
        UfsCustomerOutlet other = (UfsCustomerOutlet) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ke.tra.boa.ufs.entities.UfsCustomerOutlet[ id=" + id + " ]";
    }

}


