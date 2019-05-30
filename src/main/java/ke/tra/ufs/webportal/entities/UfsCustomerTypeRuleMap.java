/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.entities;

import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
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
    @GenericGenerator(
            name = "UFS_CUSTOMER_TYPE_RULE_MAP_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "UFS_CUSTOMER_TYPE_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "0"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @GeneratedValue(generator = "UFS_CUSTOMER_TYPE_RULE_MAP_SEQ")
    @Column(name = "ID")
    private Long id;
    @JoinColumn(name = "TYPE_ID", referencedColumnName = "ID",insertable = false, updatable = false)
    @ManyToOne(optional = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private UfsCustomerType typeId;

    @Column(name = "TYPE_ID")
    private BigDecimal typeIds;

    @JoinColumn(name = "RULE_ID", referencedColumnName = "ID",insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private UfsCustomerTypeRules ruleId;

    @Column(name = "RULE_ID")
    private BigDecimal ruleIds;

    @Size(max = 3)
    @Column(name = "INTRASH", insertable = false)
    private String intrash;

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

    public BigDecimal getTypeIds() {
        return typeIds;
    }

    public void setTypeIds(BigDecimal typeIds) {
        this.typeIds = typeIds;
    }

    public BigDecimal getRuleIds() {
        return ruleIds;
    }

    public void setRuleIds(BigDecimal ruleIds) {
        this.ruleIds = ruleIds;
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

    public String getIntrash() {
        return intrash;
    }

    public void setIntrash(String intrash) {
        this.intrash = intrash;
    }
    
}
