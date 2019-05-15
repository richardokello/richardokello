/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ojuma
 */
@Entity
@Table(name = "UFS_CUSTOMER_TYPE_RULE_MAP")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UfsCustomerTypeRuleMap.findAll", query = "SELECT u FROM UfsCustomerTypeRuleMap u")
    , @NamedQuery(name = "UfsCustomerTypeRuleMap.findById", query = "SELECT u FROM UfsCustomerTypeRuleMap u WHERE u.id = :id")
    , @NamedQuery(name = "UfsCustomerTypeRuleMap.findByIntrash", query = "SELECT u FROM UfsCustomerTypeRuleMap u WHERE u.intrash = :intrash")})
public class UfsCustomerTypeRuleMap implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Long id;
    @Size(max = 3)
    @Column(name = "INTRASH")
    private String intrash;
    @JoinColumn(name = "TYPE_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private UfsCustomerType typeId;
    @JoinColumn(name = "RULE_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private UfsCustomerTypeRules ruleId;

    public UfsCustomerTypeRuleMap() {
    }

    public UfsCustomerTypeRuleMap(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIntrash() {
        return intrash;
    }

    public void setIntrash(String intrash) {
        this.intrash = intrash;
    }

    public UfsCustomerType getTypeId() {
        return typeId;
    }

    public void setTypeId(UfsCustomerType typeId) {
        this.typeId = typeId;
    }

    public UfsCustomerTypeRules getRuleId() {
        return ruleId;
    }

    public void setRuleId(UfsCustomerTypeRules ruleId) {
        this.ruleId = ruleId;
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
        if (!(object instanceof UfsCustomerTypeRuleMap)) {
            return false;
        }
        UfsCustomerTypeRuleMap other = (UfsCustomerTypeRuleMap) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ke.tra.ufs.webportal.entities.UfsCustomerTypeRuleMap[ id=" + id + " ]";
    }
    
}
