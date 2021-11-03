/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.entities;

import ke.axle.chassis.annotations.Filter;
import ke.axle.chassis.annotations.ModifiableField;
import ke.axle.chassis.annotations.Searchable;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author ASUS
 */
@Entity
@Table(name = "PAR_MENU_GROUP")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ParMenuGroup.findAll", query = "SELECT p FROM ParMenuGroup p")
    , @NamedQuery(name = "ParMenuGroup.findById", query = "SELECT p FROM ParMenuGroup p WHERE p.id = :id")
    , @NamedQuery(name = "ParMenuGroup.findByName", query = "SELECT p FROM ParMenuGroup p WHERE p.name = :name")
    , @NamedQuery(name = "ParMenuGroup.findByDescription", query = "SELECT p FROM ParMenuGroup p WHERE p.description = :description")
    , @NamedQuery(name = "ParMenuGroup.findByDateCreated", query = "SELECT p FROM ParMenuGroup p WHERE p.dateCreated = :dateCreated")
    , @NamedQuery(name = "ParMenuGroup.findByAction", query = "SELECT p FROM ParMenuGroup p WHERE p.action = :action")
    , @NamedQuery(name = "ParMenuGroup.findByActionStatus", query = "SELECT p FROM ParMenuGroup p WHERE p.actionStatus = :actionStatus")
    , @NamedQuery(name = "ParMenuGroup.findByIntrash", query = "SELECT p FROM ParMenuGroup p WHERE p.intrash = :intrash")})
public class ParMenuGroup implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id//
    @Basic(optional = false)
    @GenericGenerator(
            name = "PAR_MENU_GROUP_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "PAR_MENU_GROUP_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increase_size", value = "1")
            }
    )
    @GeneratedValue(generator = "PAR_MENU_GROUP_SEQ")
    @Column(name = "ID")
    private BigDecimal id;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "NAME")
    @Searchable
    @ModifiableField
    @Filter
    private String name;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @ModifiableField
    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "DATE_CREATED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;

    @Basic(optional = false)
    @Size(min = 1, max = 15)
    @Column(name = "ACTION")
    @Searchable
    @Filter
    private String action;

    @Basic(optional = false)
    @Size(min = 1, max = 15)
    @Searchable
    @Filter
    @Column(name = "ACTION_STATUS")
    private String actionStatus;

    @Basic(optional = false)
    @Size(min = 1, max = 3)
    @Column(name = "INTRASH")
    private String intrash;

    public ParMenuGroup() {
    }

    public ParMenuGroup(BigDecimal id) {
        this.id = id;
    }

    public ParMenuGroup(BigDecimal id, String name, String description, String action, String actionStatus, String intrash) {
        this.id = id;
        this.name = name;
        this.description = description;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ParMenuGroup)) {
            return false;
        }
        ParMenuGroup other = (ParMenuGroup) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ke.co.tra.ufs.tms.entities.ParMenuGroup[ id=" + id + " ]";
    }
    
}
