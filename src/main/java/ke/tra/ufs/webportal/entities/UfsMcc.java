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

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ekangethe
 */
@Entity
@Table(name = "UFS_MCC")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UfsMcc.findAll", query = "SELECT u FROM UfsMcc u")
    , @NamedQuery(name = "UfsMcc.findById", query = "SELECT u FROM UfsMcc u WHERE u.id = :id")
    , @NamedQuery(name = "UfsMcc.findByName", query = "SELECT u FROM UfsMcc u WHERE u.name = :name")
    , @NamedQuery(name = "UfsMcc.findByDescription", query = "SELECT u FROM UfsMcc u WHERE u.description = :description")
    , @NamedQuery(name = "UfsMcc.findByValue", query = "SELECT u FROM UfsMcc u WHERE u.value = :value")
    , @NamedQuery(name = "UfsMcc.findByAction", query = "SELECT u FROM UfsMcc u WHERE u.action = :action")
    , @NamedQuery(name = "UfsMcc.findByActionStatus", query = "SELECT u FROM UfsMcc u WHERE u.actionStatus = :actionStatus")
    , @NamedQuery(name = "UfsMcc.findByIntrash", query = "SELECT u FROM UfsMcc u WHERE u.intrash = :intrash")
    , @NamedQuery(name = "UfsMcc.findByCreationDate", query = "SELECT u FROM UfsMcc u WHERE u.creationDate = :creationDate")})
public class UfsMcc implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @GenericGenerator(
            name = "MCC_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "MCC_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "0"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @GeneratedValue(generator = "MCC_SEQ")
    @Column(name = "ID")
    private BigDecimal id;
    @Size(max = 30)
    @Searchable
    @ModifiableField
    @Column(name = "NAME")
    private String name;
    @Size(max = 60)
    @Searchable
    @ModifiableField
    @Column(name = "DESCRIPTION")
    private String description;
    @Size(max = 20)
    @ModifiableField
    @Column(name = "VALUE")
    private String value;
    @Size(max = 20)
    @ModifiableField
    @Column(name = "ACTION",insertable = false)
    private String action;
    @Size(max = 20)
    @Filter
    @ModifiableField
    @Column(name = "ACTION_STATUS",insertable = false)
    private String actionStatus;
    @Size(max = 3)
    @Filter
    @ModifiableField
    @Column(name = "INTRASH",insertable = false)
    private String intrash;
    @Filter
    @Column(name = "CREATION_DATE",insertable = false,updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    public UfsMcc() {
    }

    public UfsMcc(BigDecimal id) {
        this.id = id;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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
        if (!(object instanceof UfsMcc)) {
            return false;
        }
        UfsMcc other = (UfsMcc) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ke.tra.ufs.webportal.entities.UfsMcc[ id=" + id + " ]";
    }
    
}
