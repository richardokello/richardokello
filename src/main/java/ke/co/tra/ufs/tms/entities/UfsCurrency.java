/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author tracom9
 */
@Entity
@Table(name = "UFS_CURRENCY")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "UfsCurrency.findAll", query = "SELECT u FROM UfsCurrency u")
        , @NamedQuery(name = "UfsCurrency.findById", query = "SELECT u FROM UfsCurrency u WHERE u.id = :id")
        , @NamedQuery(name = "UfsCurrency.findByCurrencyId", query = "SELECT u FROM UfsCurrency u WHERE u.id = :id")
        , @NamedQuery(name = "UfsCurrency.findByName", query = "SELECT u FROM UfsCurrency u WHERE u.name = :name")
        , @NamedQuery(name = "UfsCurrency.findByCode", query = "SELECT u FROM UfsCurrency u WHERE u.code = :code")
        , @NamedQuery(name = "UfsCurrency.findBySymbol", query = "SELECT u FROM UfsCurrency u WHERE u.symbol = :symbol")
        , @NamedQuery(name = "UfsCurrency.findByCodeName", query = "SELECT u FROM UfsCurrency u WHERE u.codeName = :codeName")
        , @NamedQuery(name = "UfsCurrency.findByAction", query = "SELECT u FROM UfsCurrency u WHERE u.action = :action")
        , @NamedQuery(name = "UfsCurrency.findByActionStatus", query = "SELECT u FROM UfsCurrency u WHERE u.actionStatus = :actionStatus")
        , @NamedQuery(name = "UfsCurrency.findByIntrash", query = "SELECT u FROM UfsCurrency u WHERE u.intrash = :intrash")
        , @NamedQuery(name = "UfsCurrency.findByDecimalValue", query = "SELECT u FROM UfsCurrency u WHERE u.decimalValue = :decimalValue")
        , @NamedQuery(name = "UfsCurrency.findByNumericValue", query = "SELECT u FROM UfsCurrency u WHERE u.numericValue = :numericValue")})
public class UfsCurrency implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @SequenceGenerator(name = "UFS_CURRENCY_SEQ", sequenceName = "UFS_CURRENCY_SEQ")
    @GeneratedValue(generator = "UFS_CURRENCY_SEQ")
    @Basic(optional = false)
    @Column(name = "ID")
    private BigDecimal id;
    @Basic(optional = false)
    @Column(name = "NAME")
    private String name;
    @Basic(optional = false)
    @Column(name = "CODE")
    private String code;
    @Basic(optional = false)
    @Column(name = "SYMBOL")
    private String symbol;
    @Basic(optional = false)
    @Column(name = "CODE_NAME")
    private String codeName;
    @Column(name = "ACTION")
    private String action;
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Column(name = "INTRASH")
    private String intrash;
    @Basic(optional = false)
    @Column(name = "DECIMAL_VALUE")
    private int decimalValue;
    @Basic(optional = false)
    @Column(name = "NUMERIC_VALUE")
    private int numericValue;
    @JoinColumn(name = "TENANT_ID", referencedColumnName = "U_UID")
    @ManyToOne(optional = true)
    @JsonIgnore
    private UfsOrganizationUnits tenantId;
    @OneToMany(mappedBy = "settlementCurrency")
    @JsonIgnore
    private Set<UfsBanks> ufsBanksCollection;

    public UfsCurrency() {
    }

    public UfsCurrency(BigDecimal id) {
        this.id = id;
    }

    public UfsCurrency(BigDecimal id, String name, String code, String symbol, String codeName, int decimalValue, int numericValue) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.symbol = symbol;
        this.codeName = codeName;
        this.decimalValue = decimalValue;
        this.numericValue = numericValue;
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

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
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

    public int getDecimalValue() {
        return decimalValue;
    }

    public void setDecimalValue(int decimalValue) {
        this.decimalValue = decimalValue;
    }

    public int getNumericValue() {
        return numericValue;
    }

    public void setNumericValue(int numericValue) {
        this.numericValue = numericValue;
    }

    public UfsOrganizationUnits getTenantId() {
        return tenantId;
    }

    public void setTenantId(UfsOrganizationUnits tenantId) {
        this.tenantId = tenantId;
    }

    @XmlTransient
    public Set<UfsBanks> getUfsBanksCollection() {
        return ufsBanksCollection;
    }

    public void setUfsBanksCollection(Set<UfsBanks> ufsBanksCollection) {
        this.ufsBanksCollection = ufsBanksCollection;
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
        return "masdemo.entities.UfsCurrency[ id=" + id + " ]";
    }

}
