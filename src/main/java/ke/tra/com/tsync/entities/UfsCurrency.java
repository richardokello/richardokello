/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.tra.com.tsync.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
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

/**
 *
 * @author Mwagiru Kamoni
 */
@Entity
@Table(name = "UFS_CURRENCY")
@NamedQueries({
    @NamedQuery(name = "UfsCurrency.findAll", query = "SELECT u FROM UfsCurrency u")})
public class UfsCurrency implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
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
    @JoinColumn(name = "TENANT_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private UfsOrganizationUnits tenantId;
    @OneToMany(mappedBy = "settlementCurrency")
    private Collection<UfsBanks> ufsBanksCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "currencyId")
    private Collection<TmsDeviceCurrency> tmsDeviceCurrencyCollection;

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

    public Collection<UfsBanks> getUfsBanksCollection() {
        return ufsBanksCollection;
    }

    public void setUfsBanksCollection(Collection<UfsBanks> ufsBanksCollection) {
        this.ufsBanksCollection = ufsBanksCollection;
    }

    public Collection<TmsDeviceCurrency> getTmsDeviceCurrencyCollection() {
        return tmsDeviceCurrencyCollection;
    }

    public void setTmsDeviceCurrencyCollection(Collection<TmsDeviceCurrency> tmsDeviceCurrencyCollection) {
        this.tmsDeviceCurrencyCollection = tmsDeviceCurrencyCollection;
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
        return "com.mycompany.oracleufs.UfsCurrency[ id=" + id + " ]";
    }
    
}
