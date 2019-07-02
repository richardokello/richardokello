/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
import ke.axle.chassis.annotations.Filter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author ojuma
 */
@Entity
@Table(name = "UFS_COUNTRIES")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UfsCountries.findAll", query = "SELECT u FROM UfsCountries u")
    , @NamedQuery(name = "UfsCountries.findById", query = "SELECT u FROM UfsCountries u WHERE u.id = :id")
    , @NamedQuery(name = "UfsCountries.findByName", query = "SELECT u FROM UfsCountries u WHERE u.name = :name")
    , @NamedQuery(name = "UfsCountries.findByCode", query = "SELECT u FROM UfsCountries u WHERE u.code = :code")
    , @NamedQuery(name = "UfsCountries.findByAction", query = "SELECT u FROM UfsCountries u WHERE u.action = :action")
    , @NamedQuery(name = "UfsCountries.findByActionStatus", query = "SELECT u FROM UfsCountries u WHERE u.actionStatus = :actionStatus")
    , @NamedQuery(name = "UfsCountries.findByStatus", query = "SELECT u FROM UfsCountries u WHERE u.status = :status")
    , @NamedQuery(name = "UfsCountries.findByIntrash", query = "SELECT u FROM UfsCountries u WHERE u.intrash = :intrash")
    , @NamedQuery(name = "UfsCountries.findByCreatedAt", query = "SELECT u FROM UfsCountries u WHERE u.createdAt = :createdAt")
    , @NamedQuery(name = "UfsCountries.findByUpdatedAt", query = "SELECT u FROM UfsCountries u WHERE u.updatedAt = :updatedAt")})
public class UfsCountries implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "NAME")
    private String name;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull()
    @Size(min = 1, max = 40)
    @Column(name = "CODE")
    private String code;
    @Basic(optional = false)
    @Size(min = 1, max = 15)
    @Column(name = "ACTION")
    private String action;
    @Basic(optional = false)
    @Size(min = 1, max = 15)
    @Filter
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Size(max = 10)
    @Column(name = "STATUS")
    private String status;
    @Basic(optional = false)
    @Size(min = 1, max = 3)
    @Column(name = "INTRASH")
    private String intrash;
    @Basic(optional = false)
    @Column(name = "CREATED_AT",insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Basic(optional = false)
    @Column(name = "UPDATED_AT",insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
        @GenericGenerator(
            name = "UFS_COUNTRIES_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "UFS_COUNTRIES_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "0"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )

    @GeneratedValue(generator = "UFS_COUNTRIES_SEQ")
    @Column(name = "ID")
    private BigDecimal id;
    @OneToMany(mappedBy = "country")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Set<UfsBanks> ufsBanksSet;

    public UfsCountries() {
    }

    public UfsCountries(BigDecimal id) {
        this.id = id;
    }

    public UfsCountries(BigDecimal id, String name, String code, String action, String actionStatus, String intrash, Date createdAt, Date updatedAt) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.action = action;
        this.actionStatus = actionStatus;
        this.intrash = intrash;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }


    public String getActionStatus() {
        return actionStatus;
    }

    public void setActionStatus(String actionStatus) {
        this.actionStatus = actionStatus;
    }


    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @XmlTransient
    @JsonIgnore
    public Set<UfsBanks> getUfsBanksSet() {
        return ufsBanksSet;
    }

    public void setUfsBanksSet(Set<UfsBanks> ufsBanksSet) {
        this.ufsBanksSet = ufsBanksSet;
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
        if (!(object instanceof UfsCountries)) {
            return false;
        }
        UfsCountries other = (UfsCountries) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ke.tra.ufs.webportal.entities.UfsCountries[ id=" + id + " ]";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIntrash() {
        return intrash;
    }

    public void setIntrash(String intrash) {
        this.intrash = intrash;
    }
    
}
