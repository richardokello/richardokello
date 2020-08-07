/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.entities;

import ke.axle.chassis.annotations.Filter;
import ke.axle.chassis.annotations.ModifiableField;
import ke.axle.chassis.annotations.Unique;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author Tracom
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
    @GenericGenerator(
            name = "UFS_CUSTOMER_CLASS_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "UFS_CUSTOMER_CLASS_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "0"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @GeneratedValue(generator = "UFS_CUSTOMER_CLASS_SEQ")
    @Column(name = "ID")
    private Long id;

    @Basic(optional = false)
    @NotNull
    @ModifiableField
    @Unique
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
    @Filter
    private String action;

    @Size(max = 20)
    @Filter
    @Column(name = "ACTION_STATUS")
    private String actionStatus;

    @Size(max = 3)
    @Column(name = "INTRASH")
    private String intrash;

    @Column(name = "CREATION_DATE", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

//    @JoinColumn(name = "PARENT_ID", referencedColumnName = "ID", insertable = false, updatable = false)
//    @ManyToOne
//    @JsonIgnore
//    private UfsCustomerClass parentId;
//    @Column(name = "PARENT_ID")
//    @TreeRoot
//    private BigDecimal parentIds;

    @JoinColumn(name = "TYPE_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private UfsCustomerType typeId;

    @Column(name = "TYPE_ID")
    private BigDecimal typeIds;

    @Transient
    private List<UfsCustomerClass> children;

    @Transient
    private String text;

//    @OneToMany(mappedBy = "parentId")
//    @com.fasterxml.jackson.annotation.JsonIgnore
//    private List<UfsCustomerClass> customerClassesList;

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

    public String getClassValues() {
        return classValues;
    }

    public void setClassValues(String classValues) {
        this.classValues = classValues;
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


    public UfsCustomerType getTypeId() {
        return typeId;
    }

    public void setTypeId(UfsCustomerType typeId) {
        this.typeId = typeId;
    }

    public BigDecimal getTypeIds() {
        return typeIds;
    }

    public void setTypeIds(BigDecimal typeIds) {
        this.typeIds = typeIds;
    }

    public void setChildren(List<UfsCustomerClass> children) {
        this.children = children;
    }

    public String getText() {
        return this.getName();
    }

    public void setText(String text) {
        this.text = text;
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
        return "ke.tra.ufs.webportal.entities.UfsCustomerClass[ id=" + id + " ]";
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


    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getIntrash() {
        return intrash;
    }

    public void setIntrash(String intrash) {
        this.intrash = intrash;
    }


}
