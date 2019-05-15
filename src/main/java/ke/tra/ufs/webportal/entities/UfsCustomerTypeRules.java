/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author ojuma
 */
@Entity
@Table(name = "UFS_CUSTOMER_TYPE_RULES")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UfsCustomerTypeRules.findAll", query = "SELECT u FROM UfsCustomerTypeRules u")
    , @NamedQuery(name = "UfsCustomerTypeRules.findById", query = "SELECT u FROM UfsCustomerTypeRules u WHERE u.id = :id")
    , @NamedQuery(name = "UfsCustomerTypeRules.findByName", query = "SELECT u FROM UfsCustomerTypeRules u WHERE u.name = :name")
    , @NamedQuery(name = "UfsCustomerTypeRules.findByDescription", query = "SELECT u FROM UfsCustomerTypeRules u WHERE u.description = :description")
    , @NamedQuery(name = "UfsCustomerTypeRules.findByTextType", query = "SELECT u FROM UfsCustomerTypeRules u WHERE u.textType = :textType")
    , @NamedQuery(name = "UfsCustomerTypeRules.findByAction", query = "SELECT u FROM UfsCustomerTypeRules u WHERE u.action = :action")
    , @NamedQuery(name = "UfsCustomerTypeRules.findByActionStatus", query = "SELECT u FROM UfsCustomerTypeRules u WHERE u.actionStatus = :actionStatus")
    , @NamedQuery(name = "UfsCustomerTypeRules.findByCreationDate", query = "SELECT u FROM UfsCustomerTypeRules u WHERE u.creationDate = :creationDate")
    , @NamedQuery(name = "UfsCustomerTypeRules.findByIntrash", query = "SELECT u FROM UfsCustomerTypeRules u WHERE u.intrash = :intrash")})
public class UfsCustomerTypeRules implements Serializable {

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
    @Size(min = 1, max = 20)
    @Column(name = "TEXT_TYPE")
    private String textType;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ruleId")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private List<UfsCustomerTypeRuleMap> ufsCustomerTypeRuleMapList;
    @JoinColumn(name = "TENANT_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private UfsOrganizationUnits tenantId;

    public UfsCustomerTypeRules() {
    }

    public UfsCustomerTypeRules(Long id) {
        this.id = id;
    }

    public UfsCustomerTypeRules(Long id, String name, String textType) {
        this.id = id;
        this.name = name;
        this.textType = textType;
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

    public String getTextType() {
        return textType;
    }

    public void setTextType(String textType) {
        this.textType = textType;
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

    @XmlTransient
    @JsonIgnore
    public List<UfsCustomerTypeRuleMap> getUfsCustomerTypeRuleMapList() {
        return ufsCustomerTypeRuleMapList;
    }

    public void setUfsCustomerTypeRuleMapList(List<UfsCustomerTypeRuleMap> ufsCustomerTypeRuleMapList) {
        this.ufsCustomerTypeRuleMapList = ufsCustomerTypeRuleMapList;
    }

    public UfsOrganizationUnits getTenantId() {
        return tenantId;
    }

    public void setTenantId(UfsOrganizationUnits tenantId) {
        this.tenantId = tenantId;
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
        if (!(object instanceof UfsCustomerTypeRules)) {
            return false;
        }
        UfsCustomerTypeRules other = (UfsCustomerTypeRules) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ke.tra.ufs.webportal.entities.UfsCustomerTypeRules[ id=" + id + " ]";
    }
    
}
