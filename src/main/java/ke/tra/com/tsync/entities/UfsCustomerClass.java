/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.tra.com.tsync.entities;

import java.io.Serializable;
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
@Table(name = "UFS_CUSTOMER_CLASS")
@NamedQueries({
    @NamedQuery(name = "UfsCustomerClass.findAll", query = "SELECT u FROM UfsCustomerClass u")})
public class UfsCustomerClass implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @Column(name = "NAME")
    private String name;
    @Column(name = "DESCRIPTION")
    private String description;
    @Basic(optional = false)
    @Column(name = "CLASS_VALUES")
    private String classValues;
    @Column(name = "ACTION")
    private String action;
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Column(name = "CREATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Column(name = "INTRASH")
    private String intrash;
    @OneToMany(mappedBy = "classTypeId")
    private Collection<UfsCustomer> ufsCustomerCollection;
    @JoinColumn(name = "TYPE_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private UfsCustomerType typeId;
    @OneToMany(mappedBy = "parentId")
    private Collection<UfsCustomerClass> ufsCustomerClassCollection;
    @JoinColumn(name = "PARENT_ID", referencedColumnName = "ID")
    @ManyToOne
    private UfsCustomerClass parentId;

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

    public Collection<UfsCustomer> getUfsCustomerCollection() {
        return ufsCustomerCollection;
    }

    public void setUfsCustomerCollection(Collection<UfsCustomer> ufsCustomerCollection) {
        this.ufsCustomerCollection = ufsCustomerCollection;
    }

    public UfsCustomerType getTypeId() {
        return typeId;
    }

    public void setTypeId(UfsCustomerType typeId) {
        this.typeId = typeId;
    }

    public Collection<UfsCustomerClass> getUfsCustomerClassCollection() {
        return ufsCustomerClassCollection;
    }

    public void setUfsCustomerClassCollection(Collection<UfsCustomerClass> ufsCustomerClassCollection) {
        this.ufsCustomerClassCollection = ufsCustomerClassCollection;
    }

    public UfsCustomerClass getParentId() {
        return parentId;
    }

    public void setParentId(UfsCustomerClass parentId) {
        this.parentId = parentId;
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
        return "com.mycompany.oracleufs.UfsCustomerClass[ id=" + id + " ]";
    }
    
}
