/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;

import java.io.Serializable;
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
@Table(name = "POSIRIS_TRANSACTION_TYPE")
@NamedQueries({
    @NamedQuery(name = "PosirisTransactionType.findAll", query = "SELECT p FROM PosirisTransactionType p")})
public class PosirisTransactionType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @Column(name = "NAME")
    private String name;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "ACTION")
    private String action;
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Column(name = "CREATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column(name = "INTRASH")
    private String intrash;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "transactionType")
    private Collection<PosirisTransactionTypeCode> posirisTransactionTypeCodeCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "transactionType")
    private Collection<ParBinAllowedTrnx> parBinAllowedTrnxCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "transactionType")
    private Collection<ParBinDisallowedTrnx> parBinDisallowedTrnxCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "transactionType")
    private Collection<ParCardAllowedTrnx> parCardAllowedTrnxCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "transactionType")
    private Collection<ParCardDisallowedTrnx> parCardDisallowedTrnxCollection;

    public PosirisTransactionType() {
    }

    public PosirisTransactionType(Long id) {
        this.id = id;
    }

    public PosirisTransactionType(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getIntrash() {
        return intrash;
    }

    public void setIntrash(String intrash) {
        this.intrash = intrash;
    }

    public Collection<PosirisTransactionTypeCode> getPosirisTransactionTypeCodeCollection() {
        return posirisTransactionTypeCodeCollection;
    }

    public void setPosirisTransactionTypeCodeCollection(Collection<PosirisTransactionTypeCode> posirisTransactionTypeCodeCollection) {
        this.posirisTransactionTypeCodeCollection = posirisTransactionTypeCodeCollection;
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

    public Collection<ParCardAllowedTrnx> getParCardAllowedTrnxCollection() {
        return parCardAllowedTrnxCollection;
    }

    public void setParCardAllowedTrnxCollection(Collection<ParCardAllowedTrnx> parCardAllowedTrnxCollection) {
        this.parCardAllowedTrnxCollection = parCardAllowedTrnxCollection;
    }

    public Collection<ParCardDisallowedTrnx> getParCardDisallowedTrnxCollection() {
        return parCardDisallowedTrnxCollection;
    }

    public void setParCardDisallowedTrnxCollection(Collection<ParCardDisallowedTrnx> parCardDisallowedTrnxCollection) {
        this.parCardDisallowedTrnxCollection = parCardDisallowedTrnxCollection;
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
        if (!(object instanceof PosirisTransactionType)) {
            return false;
        }
        PosirisTransactionType other = (PosirisTransactionType) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.oracleufs.PosirisTransactionType[ id=" + id + " ]";
    }
    
}
