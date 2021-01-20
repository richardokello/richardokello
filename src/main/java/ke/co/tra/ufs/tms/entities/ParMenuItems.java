/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ke.axle.chassis.annotations.Filter;
import ke.axle.chassis.annotations.ModifiableField;
import ke.axle.chassis.annotations.Searchable;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author ASUS
 */
@Entity
@Table(name = "PAR_MENU_ITEMS")
@XmlRootElement
@ToString
@NamedQueries({
        @NamedQuery(name = "ParMenuItems.findAll", query = "SELECT p FROM ParMenuItems p")
        , @NamedQuery(name = "ParMenuItems.findById", query = "SELECT p FROM ParMenuItems p WHERE p.id = :id")
        , @NamedQuery(name = "ParMenuItems.findByName", query = "SELECT p FROM ParMenuItems p WHERE p.name = :name")
        , @NamedQuery(name = "ParMenuItems.findByDescription", query = "SELECT p FROM ParMenuItems p WHERE p.description = :description")
        , @NamedQuery(name = "ParMenuItems.findByIsParent", query = "SELECT p FROM ParMenuItems p WHERE p.isParent = :isParent")
        , @NamedQuery(name = "ParMenuItems.findByDateCreated", query = "SELECT p FROM ParMenuItems p WHERE p.dateCreated = :dateCreated")
        , @NamedQuery(name = "ParMenuItems.findByAction", query = "SELECT p FROM ParMenuItems p WHERE p.action = :action")
        , @NamedQuery(name = "ParMenuItems.findByActionStatus", query = "SELECT p FROM ParMenuItems p WHERE p.actionStatus = :actionStatus")
        , @NamedQuery(name = "ParMenuItems.findByIntrash", query = "SELECT p FROM ParMenuItems p WHERE p.intrash = :intrash")})
public class ParMenuItems implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id//
    @Basic(optional = false)
    @GenericGenerator(
            name = "PAR_MENU_ITEMS_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "PAR_MENU_ITEMS_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increase_size", value = "1")
            }
    )
    @GeneratedValue(generator = "PAR_MENU_ITEMS_SEQ")
    @Column(name = "ID")
    private BigDecimal id;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "NAME")
    @Searchable
    @ModifiableField
    private String name;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "DESCRIPTION")
    @ModifiableField
    private String description;

    @Basic(optional = false)
    @Filter
    @Column(name = "IS_PARENT")
    @ModifiableField
    private short isParent;

    @Column(name = "DATE_CREATED", updatable = false, insertable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;

    @Basic(optional = false)
    @Size(min = 1, max = 15)
    @Column(name = "ACTION")
    @Filter
    private String action;

    @Basic(optional = false)
    @Size(min = 1, max = 15)
    @Filter
    @Column(name = "ACTION_STATUS")
    private String actionStatus;

    @Basic(optional = false)
    @Size(min = 1, max = 3)
    @Column(name = "INTRASH")
    private String intrash;

    @OneToMany(mappedBy = "parentId")
    @JsonIgnore
    private List<ParMenuItems> parMenuItemsList;

    @JoinColumn(name = "PARENT_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne
    @JsonIgnore
    private ParMenuItems parentId;

    @Column(name = "PARENT_ID")
    private BigDecimal parentIds;

    @Transient
    private List<ParMenuItems> children;

    @Transient
    private String text;

    @Basic(optional = false)
    @NotNull
    @Filter
    @Column(name = "MENU_LEVEL")
    @ModifiableField
    private short menuLevel;


    //    @Filter
    @NotNull
    @ModifiableField
    @Column(name = "CUSTOMER_TYPE")
    private BigDecimal customerTypeId;


    @Filter
    @Column(name = "CUSTOMER_TYPE", updatable = false, insertable = false)
    private String type;

    @JoinColumn(name = "CUSTOMER_TYPE", referencedColumnName = "ID", updatable = false, insertable = false)
    @ManyToOne
    private UfsCustomerType customerType;

    public ParMenuItems() {
    }

    public ParMenuItems(BigDecimal id) {
        this.id = id;
    }

    public ParMenuItems(BigDecimal id, String name, String description, short isParent, String action, String actionStatus, String intrash) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isParent = isParent;
        this.action = action;
        this.actionStatus = actionStatus;
        this.intrash = intrash;
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

    public List<ParMenuItems> getChildren() {
        return parMenuItemsList;
    }

    public void setChildren(List<ParMenuItems> children) {
        this.children = children;
    }

    public String getText() {
        return name;
    }

    public void setText(String text) {
        this.text = text;
    }

    @XmlTransient
    @JsonIgnore
    public List<ParMenuItems> getParMenuItemsList() {
        return parMenuItemsList;
    }

    public void setParMenuItemsList(List<ParMenuItems> parMenuItemsList) {
        this.parMenuItemsList = parMenuItemsList;
    }

    public ParMenuItems getParentId() {
        return parentId;
    }

    public void setParentId(ParMenuItems parentId) {
        this.parentId = parentId;
    }


    public BigDecimal getParentIds() {
        return parentIds;
    }

    public void setParentIds(BigDecimal parentIds) {
        this.parentIds = parentIds;
    }

    public short getMenuLevel() {
        return menuLevel;
    }

    public void setMenuLevel(short menuLevel) {
        this.menuLevel = menuLevel;
    }

    public BigDecimal getCustomerTypeId() {
        return customerTypeId;
    }

    public void setCustomerTypeId(BigDecimal customerType) {
        this.customerTypeId = customerType;
    }

    public UfsCustomerType getCustomerType() {
        return customerType;
    }

    public void setCustomerType(UfsCustomerType customerType) {
        this.customerType = customerType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
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
        return "ke.co.tra.ufs.tms.entities.ParMenuItems[ id=" + id + " ]";
    }

}
