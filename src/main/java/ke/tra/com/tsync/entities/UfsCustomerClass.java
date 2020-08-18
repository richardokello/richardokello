/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.tra.com.tsync.entities;

import java.io.Serializable;

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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Mwagiru Kamoni
 */
@Entity
@Table(name = "UFS_CUSTOMER_CLASS")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "UfsCustomerClass.findAll", query = "SELECT u FROM UfsCustomerClass u"),
        @NamedQuery(name = "UfsCustomerClass.findById", query = "SELECT u FROM UfsCustomerClass u WHERE u.id = :id"),
        @NamedQuery(name = "UfsCustomerClass.findByName", query = "SELECT u FROM UfsCustomerClass u WHERE u.name = :name"),
        @NamedQuery(name = "UfsCustomerClass.findByDescription", query = "SELECT u FROM UfsCustomerClass u WHERE u.description = :description"),
        @NamedQuery(name = "UfsCustomerClass.findByClassValues", query = "SELECT u FROM UfsCustomerClass u WHERE u.classValues = :classValues"),
        @NamedQuery(name = "UfsCustomerClass.findByAction", query = "SELECT u FROM UfsCustomerClass u WHERE u.action = :action"),
        @NamedQuery(name = "UfsCustomerClass.findByActionStatus", query = "SELECT u FROM UfsCustomerClass u WHERE u.actionStatus = :actionStatus"),
        @NamedQuery(name = "UfsCustomerClass.findByCreationDate", query = "SELECT u FROM UfsCustomerClass u WHERE u.creationDate = :creationDate"),
        @NamedQuery(name = "UfsCustomerClass.findByIntrash", query = "SELECT u FROM UfsCustomerClass u WHERE u.intrash = :intrash")})
public class UfsCustomerClass implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "NAME")
    private String name;
    @Size(max = 100)
    @Column(name = "DESCRIPTION")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4000)
    @Column(name = "CLASS_VALUES")
    private String classValues;
    @Size(max = 20)
    @Column(name = "ACTION")
    private String action;
    @Size(max = 20)
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Column(name = "CREATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Size(max = 3)
    @Column(name = "INTRASH")
    private String intrash;
//    @OneToMany(mappedBy = "classTypeId")
//    private Collection<UfsCustomer> ufsCustomerCollection;
//    @OneToMany(mappedBy = "parentId")
//    private Collection<UfsCustomerClass> ufsCustomerClassCollection;

    @JoinColumn(name = "TYPE_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private UfsCustomerType typeId;

    public UfsCustomerClass() {
    }

    public UfsCustomerClass(Long id) {
        this.id = id;
    }

    public UfsCustomerClass(Long id, String name, String classValues) {
        this.id = id;
        this.name = name;
        this.classValues = classValues;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getClassValues() {
        return classValues;
    }

    public void setClassValues(String classValues) {
        this.classValues = classValues;
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

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getIntrash() {
        return intrash;
    }

    public void setIntrash(String intrash) {
        this.intrash = intrash;
    }

//    @XmlTransient
//    @JsonIgnore
//    public Collection<UfsCustomer> getUfsCustomerCollection() {
//        return ufsCustomerCollection;
//    }
//
//    public void setUfsCustomerCollection(Collection<UfsCustomer> ufsCustomerCollection) {
//        this.ufsCustomerCollection = ufsCustomerCollection;
//    }

//    @XmlTransient
//    @JsonIgnore
//    public Collection<UfsCustomerClass> getUfsCustomerClassCollection() {
//        return ufsCustomerClassCollection;
//    }
//
//    public void setUfsCustomerClassCollection(Collection<UfsCustomerClass> ufsCustomerClassCollection) {
//        this.ufsCustomerClassCollection = ufsCustomerClassCollection;
//    }

    public UfsCustomerType getTypeId() {
        return typeId;
    }

    public void setTypeId(UfsCustomerType typeId) {
        this.typeId = typeId;
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
        if (!(object instanceof UfsCustomerClass)) {
            return false;
        }
        UfsCustomerClass other = (UfsCustomerClass) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "UfsCustomerClass[ id=" + id + " ]";
    }

}

