/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "POSIRIS_TRANSACTION_TYPE_CODE")
@NamedQueries({
    @NamedQuery(name = "PosirisTransactionTypeCode.findAll", query = "SELECT p FROM PosirisTransactionTypeCode p")})
public class PosirisTransactionTypeCode implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private BigDecimal id;
    @Basic(optional = false)
    @Column(name = "CODE")
    private String code;
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
    @JoinColumn(name = "TRANSACTION_TYPE", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private PosirisTransactionType transactionType;

    public PosirisTransactionTypeCode() {
    }

    public PosirisTransactionTypeCode(BigDecimal id) {
        this.id = id;
    }

    public PosirisTransactionTypeCode(BigDecimal id, String code) {
        this.id = id;
        this.code = code;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public PosirisTransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(PosirisTransactionType transactionType) {
        this.transactionType = transactionType;
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
        if (!(object instanceof PosirisTransactionTypeCode)) {
            return false;
        }
        PosirisTransactionTypeCode other = (PosirisTransactionTypeCode) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.oracleufs.PosirisTransactionTypeCode[ id=" + id + " ]";
    }
    
}
