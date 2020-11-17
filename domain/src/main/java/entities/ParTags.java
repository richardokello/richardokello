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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "PAR_TAGS")
@NamedQueries({
    @NamedQuery(name = "ParTags.findAll", query = "SELECT p FROM ParTags p")})
public class ParTags implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @Column(name = "NAME")
    private String name;
    @Basic(optional = false)
    @Column(name = "KEY")
    private String key;
    @Basic(optional = false)
    @Column(name = "DESCRIPTION")
    private String description;
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
    @Column(name = "VALUE_TYPE")
    private String valueType;
    @Basic(optional = false)
    @Column(name = "VALUE_LENGTH")
    private short valueLength;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tagId")
    private Collection<ParReceiptFooter> parReceiptFooterCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tagId")
    private Collection<ParMenuItems> parMenuItemsCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tagId")
    private Collection<ParReceiptHeader> parReceiptHeaderCollection;
    @JoinColumn(name = "TYPE", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private ParTagsTypes type;

    public ParTags() {
    }

    public ParTags(Long id) {
        this.id = id;
    }

    public ParTags(Long id, String name, String key, String description, String valueType, short valueLength) {
        this.id = id;
        this.name = name;
        this.key = key;
        this.description = description;
        this.valueType = valueType;
        this.valueLength = valueLength;
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public short getValueLength() {
        return valueLength;
    }

    public void setValueLength(short valueLength) {
        this.valueLength = valueLength;
    }

    public Collection<ParReceiptFooter> getParReceiptFooterCollection() {
        return parReceiptFooterCollection;
    }

    public void setParReceiptFooterCollection(Collection<ParReceiptFooter> parReceiptFooterCollection) {
        this.parReceiptFooterCollection = parReceiptFooterCollection;
    }

    public Collection<ParMenuItems> getParMenuItemsCollection() {
        return parMenuItemsCollection;
    }

    public void setParMenuItemsCollection(Collection<ParMenuItems> parMenuItemsCollection) {
        this.parMenuItemsCollection = parMenuItemsCollection;
    }

    public Collection<ParReceiptHeader> getParReceiptHeaderCollection() {
        return parReceiptHeaderCollection;
    }

    public void setParReceiptHeaderCollection(Collection<ParReceiptHeader> parReceiptHeaderCollection) {
        this.parReceiptHeaderCollection = parReceiptHeaderCollection;
    }

    public ParTagsTypes getType() {
        return type;
    }

    public void setType(ParTagsTypes type) {
        this.type = type;
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
        if (!(object instanceof ParTags)) {
            return false;
        }
        ParTags other = (ParTags) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.oracleufs.ParTags[ id=" + id + " ]";
    }
    
}
