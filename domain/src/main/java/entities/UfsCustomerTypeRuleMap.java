/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;

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

/**
 *
 * @author Mwagiru Kamoni
 */
@Entity
@Table(name = "UFS_CUSTOMER_TYPE_RULE_MAP")
@NamedQueries({
    @NamedQuery(name = "UfsCustomerTypeRuleMap.findAll", query = "SELECT u FROM UfsCustomerTypeRuleMap u")})
public class UfsCustomerTypeRuleMap implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @Column(name = "INTRASH")
    private String intrash;
    @JoinColumn(name = "RULE_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private UfsCustomerTypeRules ruleId;
    @JoinColumn(name = "TYPE_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private UfsCustomerType typeId;

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

    public UfsCustomerTypeRules getRuleId() {
        return ruleId;
    }

    public void setRuleId(UfsCustomerTypeRules ruleId) {
        this.ruleId = ruleId;
    }

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
        return "com.mycompany.oracleufs.UfsCustomerTypeRuleMap[ id=" + id + " ]";
    }
    
}
