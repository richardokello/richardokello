/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.entities;

import com.cm.projects.spring.resource.chasis.annotations.Filter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author ASUS
 */
@Entity
@Table(name = "UFS_CURRENCY")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UfsCurrency.findAll", query = "SELECT u FROM UfsCurrency u")
    , @NamedQuery(name = "UfsCurrency.findById", query = "SELECT u FROM UfsCurrency u WHERE u.id = :id")
    , @NamedQuery(name = "UfsCurrency.findByName", query = "SELECT u FROM UfsCurrency u WHERE u.name = :name")
    , @NamedQuery(name = "UfsCurrency.findByCode", query = "SELECT u FROM UfsCurrency u WHERE u.code = :code")
    , @NamedQuery(name = "UfsCurrency.findBySymbol", query = "SELECT u FROM UfsCurrency u WHERE u.symbol = :symbol")
    , @NamedQuery(name = "UfsCurrency.findByDecimalValue", query = "SELECT u FROM UfsCurrency u WHERE u.decimalValue = :decimalValue")
    , @NamedQuery(name = "UfsCurrency.findByNumericValue", query = "SELECT u FROM UfsCurrency u WHERE u.numericValue = :numericValue")
    , @NamedQuery(name = "UfsCurrency.findByAction", query = "SELECT u FROM UfsCurrency u WHERE u.action = :action")
    , @NamedQuery(name = "UfsCurrency.findByActionStatus", query = "SELECT u FROM UfsCurrency u WHERE u.actionStatus = :actionStatus")
    , @NamedQuery(name = "UfsCurrency.findByIntrash", query = "SELECT u FROM UfsCurrency u WHERE u.intrash = :intrash")})
public class UfsCurrency implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "NAME")
    private String name;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull()
    @Size(min = 1, max = 10)
    @Column(
            name = "CODE")

    private String code;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "SYMBOL")
    private String symbol;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DECIMAL_VALUE")
    private short decimalValue;
    @Basic(optional = false)
    @NotNull
    @Column(name = "NUMERIC_VALUE")
    private short numericValue;
    @Size(max = 15)
    @Column(name = "ACTION")
    private String action;
    @Size(max = 15)
    @Filter
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Size(max = 3)
    @Column(name = "INTRASH")
    private String intrash;
    @OneToMany(mappedBy = "settlementCurrency")
    @JsonIgnore
    private Set<UfsBanks> ufsBanksSet;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @GenericGenerator(
            name = "UFS_CURRENCY_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                @org.hibernate.annotations.Parameter(name = "sequence_name", value = "UFS_CURRENCY_SEQ"),
                @org.hibernate.annotations.Parameter(name = "initial_value", value = "0"),
                @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    
    @GeneratedValue(generator = "UFS_CURRENCY_SEQ")
    private BigDecimal id;
    @JoinColumn(name = "TENANT_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne
    @JsonIgnore
    private UfsOrganizationUnits tenantId;

    @Column(name = "TENANT_ID")
    private BigDecimal tenantIds;

    public UfsCurrency() {
    }

    public UfsCurrency(BigDecimal id) {
        this.id = id;
    }
    

    public UfsCurrency(BigDecimal id, String name, String code, String symbol, short decimalValue, short numericValue) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.symbol = symbol;
        this.decimalValue = decimalValue;
        this.numericValue = numericValue;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public UfsOrganizationUnits getTenantId() {
        return tenantId;
    }

    public void setTenantId(UfsOrganizationUnits tenantId) {
        this.tenantId = tenantId;
    }

    public BigDecimal getTenantIds() {
        return tenantIds;
    }

    public void setTenantIds(BigDecimal tenantIds) {
        this.tenantIds = tenantIds;
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
        if (!(object instanceof UfsCurrency)) {
            return false;
        }
        UfsCurrency other = (UfsCurrency) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ke.tracom.ufs.entities.UfsCurrency[ id=" + id + " ]";
    }





    @XmlTransient
    @org.codehaus.jackson.annotate.JsonIgnore
    public Set<UfsBanks> getUfsBanksSet() {
        return ufsBanksSet;
    }

    public void setUfsBanksSet(Set<UfsBanks> ufsBanksSet) {
        this.ufsBanksSet = ufsBanksSet;
    }


    public short getDecimalValue() {
        return decimalValue;
    }

    public void setDecimalValue(short decimalValue) {
        this.decimalValue = decimalValue;
    }

    public short getNumericValue() {
        return numericValue;
    }

    public void setNumericValue(short numericValue) {
        this.numericValue = numericValue;
    }


    public String getActionStatus() {
        return actionStatus;
    }

    public void setActionStatus(String actionStatus) {
        this.actionStatus = actionStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
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
