/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.tra.com.tsync.entities;

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
@Table(name = "PAR_RECEIPT_HEADER")
@NamedQueries({
    @NamedQuery(name = "ParReceiptHeader.findAll", query = "SELECT p FROM ParReceiptHeader p")})
public class ParReceiptHeader implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private BigDecimal id;
    @Column(name = "BOLD")
    private Short bold;
    @Column(name = "ITALICS")
    private Short italics;
    @Column(name = "FONT")
    private Long font;
    @Column(name = "CREATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column(name = "ACTION")
    private String action;
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Column(name = "INTRASH")
    private String intrash;
    @JoinColumn(name = "TAG_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private ParTags tagId;
    @JoinColumn(name = "RECEIPT_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private ParReceipt receiptId;

    public ParReceiptHeader() {
    }

    public ParReceiptHeader(BigDecimal id) {
        this.id = id;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public Short getBold() {
        return bold;
    }

    public void setBold(Short bold) {
        this.bold = bold;
    }

    public Short getItalics() {
        return italics;
    }

    public void setItalics(Short italics) {
        this.italics = italics;
    }

    public Long getFont() {
        return font;
    }

    public void setFont(Long font) {
        this.font = font;
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

    public ParTags getTagId() {
        return tagId;
    }

    public void setTagId(ParTags tagId) {
        this.tagId = tagId;
    }

    public ParReceipt getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(ParReceipt receiptId) {
        this.receiptId = receiptId;
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
        if (!(object instanceof ParReceiptHeader)) {
            return false;
        }
        ParReceiptHeader other = (ParReceiptHeader) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.oracleufs.ParReceiptHeader[ id=" + id + " ]";
    }
    
}
