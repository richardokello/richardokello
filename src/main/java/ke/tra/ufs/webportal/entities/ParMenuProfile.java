/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ke.axle.chassis.annotations.Filter;
import ke.axle.chassis.annotations.ModifiableField;
import ke.axle.chassis.annotations.Searchable;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author ASUS
 */
@Entity
@Table(name = "PAR_MENU_PROFILE")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "ParMenuProfile.findAll", query = "SELECT p FROM ParMenuProfile p")
        , @NamedQuery(name = "ParMenuProfile.findById", query = "SELECT p FROM ParMenuProfile p WHERE p.id = :id")
        , @NamedQuery(name = "ParMenuProfile.findByName", query = "SELECT p FROM ParMenuProfile p WHERE p.name = :name")
        , @NamedQuery(name = "ParMenuProfile.findByDescription", query = "SELECT p FROM ParMenuProfile p WHERE p.description = :description")
        , @NamedQuery(name = "ParMenuProfile.findByMenuValue", query = "SELECT p FROM ParMenuProfile p WHERE p.menuValue = :menuValue")
        , @NamedQuery(name = "ParMenuProfile.findByDateCreated", query = "SELECT p FROM ParMenuProfile p WHERE p.dateCreated = :dateCreated")
        , @NamedQuery(name = "ParMenuProfile.findByAction", query = "SELECT p FROM ParMenuProfile p WHERE p.action = :action")
        , @NamedQuery(name = "ParMenuProfile.findByActionStatus", query = "SELECT p FROM ParMenuProfile p WHERE p.actionStatus = :actionStatus")
        , @NamedQuery(name = "ParMenuProfile.findByIntrash", query = "SELECT p FROM ParMenuProfile p WHERE p.intrash = :intrash")})
public class ParMenuProfile implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id//
    @Basic(optional = false)
    @SequenceGenerator(name = "PAR_MENU_PROFILE_SEQ", sequenceName = "PAR_MENU_PROFILE_SEQ")
    @GeneratedValue(generator = "PAR_MENU_PROFILE_SEQ")
    @Column(name = "ID")
    private BigDecimal id;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "NAME")
    @ModifiableField
    @Searchable
    @Filter
    private String name;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "DESCRIPTION")
    @ModifiableField
    private String description;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4000)
    @Column(name = "MENU_VALUE")
    @ModifiableField
    private String menuValue;

    @NotNull
    @Column(name = "CUSTOMER_TYPE")
    private BigDecimal customerTypeId;

    @JoinColumn(name = "CUSTOMER_TYPE", referencedColumnName = "ID", updatable = false, insertable = false)
    @ManyToOne
    private UfsCustomerType customerType;

    @Column(name = "DATE_CREATED", insertable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;
    @Size(max = 15)
    @Searchable
    @Filter
    @Column(name = "ACTION", insertable = false)
    private String action;
    @Size(max = 15)
    @Searchable
    @Filter
    @Column(name = "ACTION_STATUS", insertable = false)
    private String actionStatus;
    @Size(max = 3)
    @Column(name = "INTRASH", insertable = false)
    private String intrash;

    @JsonIgnore
    @OneToMany(mappedBy = "menuProfile")
    private List<ParGlobalMasterProfile> masterProfile;

    public ParMenuProfile() {
    }

    public ParMenuProfile(BigDecimal id) {
        this.id = id;
    }

    public ParMenuProfile(BigDecimal id, String name, String description, String menuValue) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.menuValue = menuValue;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMenuValue() {
        return menuValue;
    }

    public void setMenuValue(String menuValue) {
        this.menuValue = menuValue;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
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

    public BigDecimal getCustomerTypeId() {
        return customerTypeId;
    }

    public void setCustomerTypeId(BigDecimal customerTypeId) {
        this.customerTypeId = customerTypeId;
    }

    public UfsCustomerType getCustomerType() {
        return customerType;
    }

    public void setCustomerType(UfsCustomerType customerType) {
        this.customerType = customerType;
    }

    public List<ParGlobalMasterProfile> getMasterProfile() {
        return masterProfile;
    }

    public void setMasterProfile(List<ParGlobalMasterProfile> masterProfile) {
        this.masterProfile = masterProfile;
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
        if (!(object instanceof ParMenuProfile)) {
            return false;
        }
        ParMenuProfile other = (ParMenuProfile) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ke.co.tra.ufs.tms.entities.ParMenuProfile[ id=" + id + " ]";
    }

}
