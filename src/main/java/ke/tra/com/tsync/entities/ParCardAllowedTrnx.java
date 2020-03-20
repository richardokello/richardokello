/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.tra.com.tsync.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Mwagiru Kamoni
 */
@Entity
@Table(name = "PAR_CARD_ALLOWED_TRNX")
@NamedQueries({
    @NamedQuery(name = "ParCardAllowedTrnx.findAll", query = "SELECT p FROM ParCardAllowedTrnx p")})
public class ParCardAllowedTrnx implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private BigDecimal id;
    @Column(name = "INTRASH")
    private String intrash;
    @JoinColumn(name = "TRANSACTION_TYPE", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private PosirisTransactionType transactionType;
    @JoinColumn(name = "CARD_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private ParCards cardId;

    public ParCardAllowedTrnx() {
    }

    public ParCardAllowedTrnx(BigDecimal id) {
        this.id = id;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getIntrash() {
        return intrash;
    }

    public void setIntrash(String intrash) {
        this.intrash = intrash;
    }

    public PosirisTransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(PosirisTransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public ParCards getCardId() {
        return cardId;
    }

    public void setCardId(ParCards cardId) {
        this.cardId = cardId;
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
        if (!(object instanceof ParCardAllowedTrnx)) {
            return false;
        }
        ParCardAllowedTrnx other = (ParCardAllowedTrnx) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.oracleufs.ParCardAllowedTrnx[ id=" + id + " ]";
    }
    
}
