/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author ekangethe
 */
@Entity
@Table(name = "TRANSACTION_TYPES")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TransactionTypes.findAll", query = "SELECT t FROM TransactionTypes t")
    , @NamedQuery(name = "TransactionTypes.findById", query = "SELECT t FROM TransactionTypes t WHERE t.id = :id")
    , @NamedQuery(name = "TransactionTypes.findByTxnMti", query = "SELECT t FROM TransactionTypes t WHERE t.txnMti = :txnMti")
    , @NamedQuery(name = "TransactionTypes.findByTxnProcode", query = "SELECT t FROM TransactionTypes t WHERE t.txnProcode = :txnProcode")
    , @NamedQuery(name = "TransactionTypes.findByTxnName", query = "SELECT t FROM TransactionTypes t WHERE t.txnName = :txnName")
    , @NamedQuery(name = "TransactionTypes.findByTxnDescription", query = "SELECT t FROM TransactionTypes t WHERE t.txnDescription = :txnDescription")
    , @NamedQuery(name = "TransactionTypes.findByCreationDate", query = "SELECT t FROM TransactionTypes t WHERE t.creationDate = :creationDate")})
public class TransactionTypes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Long id;
    @Size(max = 15)
    @Column(name = "TXN_MTI")
    private String txnMti;
    @Size(max = 30)
    @Column(name = "TXN_PROCODE")
    private String txnProcode;
    @Size(max = 30)
    @Column(name = "TXN_NAME")
    private String txnName;
    @Size(max = 200)
    @Column(name = "TXN_DESCRIPTION")
    private String txnDescription;
    @Column(name = "CREATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    public TransactionTypes() {
    }

    public TransactionTypes(Long id) {
        this.id = id;
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
        return "ke.tra.ufs.posiris.entities.TransactionTypes[ id=" + id + " ]";
    }
    
}
