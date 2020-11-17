/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Mwagiru Kamoni
 */
@Entity
@Table(name = "PAR_BIN_CONFIG")
@NamedQueries({
    @NamedQuery(name = "ParBinConfig.findAll", query = "SELECT p FROM ParBinConfig p")})
public class ParBinConfig implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private BigDecimal id;
    @Basic(optional = false)
    @Column(name = "BIN_ISSUER")
    private String binIssuer;
    @Basic(optional = false)
    @Column(name = "BIN_LOW")
    private String binLow;
    @Basic(optional = false)
    @Column(name = "BIN_HIGH")
    private String binHigh;
    @Column(name = "CREATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column(name = "ACTION")
    private String action;
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Column(name = "INTRASH")
    private String intrash;
    @Basic(optional = false)
    @Column(name = "HAS_ROUTING")
    private short hasRouting;
    @Column(name = "IP")
    private String ip;
    @Column(name = "PORT")
    private String port;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "binId")
    private Collection<ParBinAllowedTrnx> parBinAllowedTrnxCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "binId")
    private Collection<ParBinDisallowedTrnx> parBinDisallowedTrnxCollection;

    public ParBinConfig() {
    }

    public ParBinConfig(BigDecimal id) {
        this.id = id;
    }

    public ParBinConfig(BigDecimal id, String binIssuer, String binLow, String binHigh, short hasRouting) {
        this.id = id;
        this.binIssuer = binIssuer;
        this.binLow = binLow;
        this.binHigh = binHigh;
        this.hasRouting = hasRouting;
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

    public short getHasRouting() {
        return hasRouting;
    }

    public void setHasRouting(short hasRouting) {
        this.hasRouting = hasRouting;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public Collection<ParBinAllowedTrnx> getParBinAllowedTrnxCollection() {
        return parBinAllowedTrnxCollection;
    }

    public void setParBinAllowedTrnxCollection(Collection<ParBinAllowedTrnx> parBinAllowedTrnxCollection) {
        this.parBinAllowedTrnxCollection = parBinAllowedTrnxCollection;
    }

    public Collection<ParBinDisallowedTrnx> getParBinDisallowedTrnxCollection() {
        return parBinDisallowedTrnxCollection;
    }

    public void setParBinDisallowedTrnxCollection(Collection<ParBinDisallowedTrnx> parBinDisallowedTrnxCollection) {
        this.parBinDisallowedTrnxCollection = parBinDisallowedTrnxCollection;
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
        return "com.mycompany.oracleufs.ParBinConfig[ id=" + id + " ]";
    }
    
}
