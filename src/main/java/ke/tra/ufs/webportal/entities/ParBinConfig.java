/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.entities;



import ke.axle.chassis.annotations.Filter;
import ke.axle.chassis.annotations.ModifiableField;
import ke.axle.chassis.annotations.Searchable;
import ke.axle.chassis.annotations.Unique;

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
@Table(name = "PAR_BIN_CONFIG")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ParBinConfig.findAll", query = "SELECT p FROM ParBinConfig p")
    , @NamedQuery(name = "ParBinConfig.findById", query = "SELECT p FROM ParBinConfig p WHERE p.id = :id")
    , @NamedQuery(name = "ParBinConfig.findByBinIssuer", query = "SELECT p FROM ParBinConfig p WHERE p.binIssuer = :binIssuer")
    , @NamedQuery(name = "ParBinConfig.findByBinLow", query = "SELECT p FROM ParBinConfig p WHERE p.binLow = :binLow")
    , @NamedQuery(name = "ParBinConfig.findByBinHigh", query = "SELECT p FROM ParBinConfig p WHERE p.binHigh = :binHigh")
    , @NamedQuery(name = "ParBinConfig.findByCreatedAt", query = "SELECT p FROM ParBinConfig p WHERE p.createdAt = :createdAt")
    , @NamedQuery(name = "ParBinConfig.findByAction", query = "SELECT p FROM ParBinConfig p WHERE p.action = :action")
    , @NamedQuery(name = "ParBinConfig.findByActionStatus", query = "SELECT p FROM ParBinConfig p WHERE p.actionStatus = :actionStatus")
    , @NamedQuery(name = "ParBinConfig.findByIntrash", query = "SELECT p FROM ParBinConfig p WHERE p.intrash = :intrash")})
public class ParBinConfig implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id//
    @Basic(optional = false)
    @SequenceGenerator(name = "PAR_BIN_CONFIG_SEQ", sequenceName = "PAR_BIN_CONFIG_SEQ")
    @GeneratedValue(generator = "PAR_BIN_CONFIG_SEQ")
    @Column(name = "ID")
    private BigDecimal id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "BIN_ISSUER")
    @Searchable
    @Filter
    @ModifiableField
    @Unique
    private String binIssuer;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @ModifiableField
    @Column(name = "BIN_LOW")
    private String binLow;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @ModifiableField
    @Column(name = "BIN_HIGH")
    private String binHigh;
    @Column(name = "CREATED_AT", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Size(max = 15)
    @Searchable
    @ModifiableField
    @Filter
    @Column(name = "ACTION", insertable = false)
    private String action;
    @Size(max = 15)
    @Searchable
    @Filter
    @ModifiableField
    @Column(name = "ACTION_STATUS", insertable = false)
    private String actionStatus;
    @Size(max = 3)
    @ModifiableField
    @Column(name = "INTRASH", insertable = false)
    private String intrash;

    public ParBinConfig() {
    }

    public ParBinConfig(BigDecimal id) {
        this.id = id;
    }

    public ParBinConfig(BigDecimal id, String binIssuer, String binLow, String binHigh) {
        this.id = id;
        this.binIssuer = binIssuer;
        this.binLow = binLow;
        this.binHigh = binHigh;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getBinIssuer() {
        return binIssuer;
    }

    public void setBinIssuer(String binIssuer) {
        this.binIssuer = binIssuer;
    }

    public String getBinLow() {
        return binLow;
    }

    public void setBinLow(String binLow) {
        this.binLow = binLow;
    }

    public String getBinHigh() {
        return binHigh;
    }

    public void setBinHigh(String binHigh) {
        this.binHigh = binHigh;
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
        if (!(object instanceof ParBinConfig)) {
            return false;
        }
        ParBinConfig other = (ParBinConfig) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ke.co.tra.ufs.tms.entities.ParBinConfig[ id=" + id + " ]";
    }
    
}
