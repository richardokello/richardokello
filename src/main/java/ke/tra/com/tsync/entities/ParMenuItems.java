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
@Table(name = "PAR_MENU_ITEMS")
@NamedQueries({
    @NamedQuery(name = "ParMenuItems.findAll", query = "SELECT p FROM ParMenuItems p")})
public class ParMenuItems implements Serializable {
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
    @Column(name = "DESCRIPTION")
    private String description;
    @Basic(optional = false)
    @Column(name = "IS_PARENT")
    private short isParent;
    @Column(name = "DATE_CREATED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;
    @Basic(optional = false)
    @Column(name = "ACTION")
    private String action;
    @Basic(optional = false)
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Basic(optional = false)
    @Column(name = "INTRASH")
    private String intrash;
    @Basic(optional = false)
    @Column(name = "MENU_LEVEL")
    private short menuLevel;
    @JoinColumn(name = "TAG_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private ParTags tagId;
    @OneToMany(mappedBy = "parentId")
    private Collection<ParMenuItems> parMenuItemsCollection;
    @JoinColumn(name = "PARENT_ID", referencedColumnName = "ID")
    @ManyToOne
    private ParMenuItems parentId;
    @JoinColumn(name = "GROUP_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private ParMenuGroup groupId;

    public ParMenuItems() {
    }

    public ParMenuItems(BigDecimal id) {
        this.id = id;
    }

    public ParMenuItems(BigDecimal id, String name, String description, short isParent, String action, String actionStatus, String intrash, short menuLevel) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isParent = isParent;
        this.action = action;
        this.actionStatus = actionStatus;
        this.intrash = intrash;
        this.menuLevel = menuLevel;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public short getIsParent() {
        return isParent;
    }

    public void setIsParent(short isParent) {
        this.isParent = isParent;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
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

    public short getMenuLevel() {
        return menuLevel;
    }

    public void setMenuLevel(short menuLevel) {
        this.menuLevel = menuLevel;
    }

    public ParTags getTagId() {
        return tagId;
    }

    public void setTagId(ParTags tagId) {
        this.tagId = tagId;
    }

    public Collection<ParMenuItems> getParMenuItemsCollection() {
        return parMenuItemsCollection;
    }

    public void setParMenuItemsCollection(Collection<ParMenuItems> parMenuItemsCollection) {
        this.parMenuItemsCollection = parMenuItemsCollection;
    }

    public ParMenuItems getParentId() {
        return parentId;
    }

    public void setParentId(ParMenuItems parentId) {
        this.parentId = parentId;
    }

    public ParMenuGroup getGroupId() {
        return groupId;
    }

    public void setGroupId(ParMenuGroup groupId) {
        this.groupId = groupId;
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
        if (!(object instanceof ParMenuItems)) {
            return false;
        }
        ParMenuItems other = (ParMenuItems) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.oracleufs.ParMenuItems[ id=" + id + " ]";
    }
    
}
