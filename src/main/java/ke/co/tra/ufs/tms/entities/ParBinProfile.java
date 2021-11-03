/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.entities;


import ke.axle.chassis.annotations.Filter;
import ke.axle.chassis.annotations.ModifiableField;
import ke.axle.chassis.annotations.Searchable;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ASUS
 */
@Entity
@Table(name = "PAR_BIN_PROFILE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ParBinProfile.findAll", query = "SELECT p FROM ParBinProfile p")
    , @NamedQuery(name = "ParBinProfile.findById", query = "SELECT p FROM ParBinProfile p WHERE p.id = :id")
    , @NamedQuery(name = "ParBinProfile.findByName", query = "SELECT p FROM ParBinProfile p WHERE p.name = :name")
    , @NamedQuery(name = "ParBinProfile.findByDescription", query = "SELECT p FROM ParBinProfile p WHERE p.description = :description")
    , @NamedQuery(name = "ParBinProfile.findByValue", query = "SELECT p FROM ParBinProfile p WHERE p.value = :value")
    , @NamedQuery(name = "ParBinProfile.findByCreatedAt", query = "SELECT p FROM ParBinProfile p WHERE p.createdAt = :createdAt")
    , @NamedQuery(name = "ParBinProfile.findByAction", query = "SELECT p FROM ParBinProfile p WHERE p.action = :action")
    , @NamedQuery(name = "ParBinProfile.findByActionStatus", query = "SELECT p FROM ParBinProfile p WHERE p.actionStatus = :actionStatus")
    , @NamedQuery(name = "ParBinProfile.findByIntrash", query = "SELECT p FROM ParBinProfile p WHERE p.intrash = :intrash")})
public class ParBinProfile implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id//
    @Basic(optional = false)
    @SequenceGenerator(name = "PAR_BIN_PROFILE_SEQ", sequenceName = "PAR_BIN_PROFILE_SEQ")
    @GeneratedValue(generator = "PAR_BIN_PROFILE_SEQ")
    @Column(name = "ID")
    private BigDecimal id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
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
    @Size(min = 1, max = 2000)
    @Column(name = "VALUE")
    @ModifiableField
    private String value;
    @Column(name = "CREATED_AT", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Size(max = 15)
    @Column(name = "ACTION", insertable = false)
    @ModifiableField
    @Searchable
    @Filter
    private String action;
    @Size(max = 15)
    @ModifiableField
    @Searchable
    @Filter
    @Column(name = "ACTION_STATUS", insertable = false)
    private String actionStatus;
    @Size(max = 3)
    @ModifiableField
    @Column(name = "INTRASH", insertable = false)
    private String intrash;

    public ParBinProfile() {
    }

    public ParBinProfile(BigDecimal id) {
        this.id = id;
    }

    public ParBinProfile(BigDecimal id, String name, String description, String value) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.value = value;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ParBinProfile)) {
            return false;
        }
        ParBinProfile other = (ParBinProfile) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ke.co.tra.ufs.tms.entities.ParBinProfile[ id=" + id + " ]";
    }
    
}
