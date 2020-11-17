/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;

import java.io.Serializable;
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
@Table(name = "TRANSACTION_TYPES")
@NamedQueries({
    @NamedQuery(name = "TransactionTypes.findAll", query = "SELECT t FROM TransactionTypes t"),
    @NamedQuery(name = "TransactionTypes.findById", query = "SELECT t FROM TransactionTypes t WHERE t.id = :id"),
    @NamedQuery(name = "TransactionTypes.findByTxnMti", query = "SELECT t FROM TransactionTypes t WHERE t.txnMti = :txnMti"),
    @NamedQuery(name = "TransactionTypes.findByTxnProcode", query = "SELECT t FROM TransactionTypes t WHERE t.txnProcode = :txnProcode"),
    @NamedQuery(name = "TransactionTypes.findByTxnName", query = "SELECT t FROM TransactionTypes t WHERE t.txnName = :txnName"),
    @NamedQuery(name = "TransactionTypes.findByTxnDescription", query = "SELECT t FROM TransactionTypes t WHERE t.txnDescription = :txnDescription"),
    @NamedQuery(name = "TransactionTypes.findByCreationDate", query = "SELECT t FROM TransactionTypes t WHERE t.creationDate = :creationDate"),
    @NamedQuery(name = "TransactionTypes.findByAction", query = "SELECT t FROM TransactionTypes t WHERE t.action = :action"),
    @NamedQuery(name = "TransactionTypes.findByActionStatus", query = "SELECT t FROM TransactionTypes t WHERE t.actionStatus = :actionStatus"),
    @NamedQuery(name = "TransactionTypes.findByIntrash", query = "SELECT t FROM TransactionTypes t WHERE t.intrash = :intrash")})
public class TransactionTypes implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @Column(name = "TXN_MTI")
    private String txnMti;
    @Column(name = "TXN_PROCODE")
    private String txnProcode;
    @Column(name = "TXN_NAME")
    private String txnName;
    @Column(name = "TXN_DESCRIPTION")
    private String txnDescription;
    @Column(name = "CREATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Basic(optional = false)
    @Column(name = "ACTION")
    private String action;
    @Basic(optional = false)
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Column(name = "INTRASH")
    private String intrash;

    public TransactionTypes() {
    }

    public TransactionTypes(Long id) {
        this.id = id;
    }

    public TransactionTypes(Long id, String action, String actionStatus) {
        this.id = id;
        this.action = action;
        this.actionStatus = actionStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTxnMti() {
        return txnMti;
    }

    public void setTxnMti(String txnMti) {
        this.txnMti = txnMti;
    }

    public String getTxnProcode() {
        return txnProcode;
    }

    public void setTxnProcode(String txnProcode) {
        this.txnProcode = txnProcode;
    }

    public String getTxnName() {
        return txnName;
    }

    public void setTxnName(String txnName) {
        this.txnName = txnName;
    }

    public String getTxnDescription() {
        return txnDescription;
    }

    public void setTxnDescription(String txnDescription) {
        this.txnDescription = txnDescription;
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
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TransactionTypes)) {
            return false;
        }
        TransactionTypes other = (TransactionTypes) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.oracleufs.TransactionTypes[ id=" + id + " ]";
    }
    
}
