/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.tra.com.tsync.entities;

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
@Table(name = "PAR_RECEIPT")
@NamedQueries({
    @NamedQuery(name = "ParReceipt.findAll", query = "SELECT p FROM ParReceipt p")})
public class ParReceipt implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private BigDecimal id;
    @Basic(optional = false)
    @Column(name = "NAME")
    private String name;
    @Basic(optional = false)
    @Column(name = "NARRATION")
    private String narration;
    @Column(name = "LOGO")
    private String logo;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "receiptId")
    private Collection<ParReceiptFooter> parReceiptFooterCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "receiptId")
    private Collection<ParReceiptHeader> parReceiptHeaderCollection;

    public ParReceipt() {
    }

    public ParReceipt(BigDecimal id) {
        this.id = id;
    }

    public ParReceipt(BigDecimal id, String name, String narration) {
        this.id = id;
        this.name = name;
        this.narration = narration;
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

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
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

    public Collection<ParReceiptFooter> getParReceiptFooterCollection() {
        return parReceiptFooterCollection;
    }

    public void setParReceiptFooterCollection(Collection<ParReceiptFooter> parReceiptFooterCollection) {
        this.parReceiptFooterCollection = parReceiptFooterCollection;
    }

    public Collection<ParReceiptHeader> getParReceiptHeaderCollection() {
        return parReceiptHeaderCollection;
    }

    public void setParReceiptHeaderCollection(Collection<ParReceiptHeader> parReceiptHeaderCollection) {
        this.parReceiptHeaderCollection = parReceiptHeaderCollection;
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
        if (!(object instanceof ParReceipt)) {
            return false;
        }
        ParReceipt other = (ParReceipt) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.oracleufs.ParReceipt[ id=" + id + " ]";
    }
    
}
