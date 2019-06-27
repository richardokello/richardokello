/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.entities;

import com.cm.projects.spring.resource.chasis.annotations.Filter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author ojuma
 */
@Entity
@Table(name = "UFS_BANKS", catalog = "", schema = "UFS_SMART_SUITE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UfsBanks.findAll", query = "SELECT u FROM UfsBanks u")
    , @NamedQuery(name = "UfsBanks.findById", query = "SELECT u FROM UfsBanks u WHERE u.id = :id")
    , @NamedQuery(name = "UfsBanks.findByBankName", query = "SELECT u FROM UfsBanks u WHERE u.bankName = :bankName")
    , @NamedQuery(name = "UfsBanks.findByBankIdentifier", query = "SELECT u FROM UfsBanks u WHERE u.bankIdentifier = :bankIdentifier")
    , @NamedQuery(name = "UfsBanks.findBySettlementAccount", query = "SELECT u FROM UfsBanks u WHERE u.settlementAccount = :settlementAccount")
    , @NamedQuery(name = "UfsBanks.findByBankType", query = "SELECT u FROM UfsBanks u WHERE u.bankType = :bankType")
    , @NamedQuery(name = "UfsBanks.findByCommisionDefinition", query = "SELECT u FROM UfsBanks u WHERE u.commisionDefinition = :commisionDefinition")
    , @NamedQuery(name = "UfsBanks.findByBankGuarantee", query = "SELECT u FROM UfsBanks u WHERE u.bankGuarantee = :bankGuarantee")
    , @NamedQuery(name = "UfsBanks.findByInterchangeFee", query = "SELECT u FROM UfsBanks u WHERE u.interchangeFee = :interchangeFee")
    , @NamedQuery(name = "UfsBanks.findBySetupFee", query = "SELECT u FROM UfsBanks u WHERE u.setupFee = :setupFee")
    , @NamedQuery(name = "UfsBanks.findBySettlementCurrency", query = "SELECT u FROM UfsBanks u WHERE u.settlementCurrency = :settlementCurrency")
    , @NamedQuery(name = "UfsBanks.findByCountry", query = "SELECT u FROM UfsBanks u WHERE u.country = :country")
    , @NamedQuery(name = "UfsBanks.findByCreatedAt", query = "SELECT u FROM UfsBanks u WHERE u.createdAt = :createdAt")
    , @NamedQuery(name = "UfsBanks.findByAction", query = "SELECT u FROM UfsBanks u WHERE u.action = :action")
    , @NamedQuery(name = "UfsBanks.findByActionStatus", query = "SELECT u FROM UfsBanks u WHERE u.actionStatus = :actionStatus")
    , @NamedQuery(name = "UfsBanks.findByIntrash", query = "SELECT u FROM UfsBanks u WHERE u.intrash = :intrash")})
public class UfsBanks implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "BANK_NAME")
    private String bankName;
    @Size(max = 50)
    @Column(name = "BANK_IDENTIFIER")
    private String bankIdentifier;
    @Size(max = 30)
    @Column(name = "SETTLEMENT_ACCOUNT")
    private String settlementAccount;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "BANK_TYPE")
    private String bankType;
    @Size(max = 20)
    @Column(name = "COMMISION_DEFINITION")
    private String commisionDefinition;
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

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bankId")
    private Set<UfsBankBins> ufsBankBinsSet;
    @JoinColumn(name = "COUNTRY", referencedColumnName = "ID",insertable = false, updatable = false)
    @ManyToOne
    private UfsCountries country;
    @JoinColumn(name = "SETTLEMENT_CURRENCY", referencedColumnName = "ID",insertable = false, updatable = false)
    @ManyToOne
    private UfsCurrency settlementCurrency;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GenericGenerator(
            name = "UFS_BANKS_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "UFS_BANKS_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "0"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )

    @GeneratedValue(generator = "UFS_BANKS_SEQ")
    @Column(name = "ID")
    private Long id;
    @Column(name = "BANK_GUARANTEE")
    private BigInteger bankGuarantee;
    @Column(name = "INTERCHANGE_FEE")
    private BigInteger interchangeFee;
    @Column(name = "SETUP_FEE")
    private BigInteger setupFee;
    @Column(name = "SETTLEMENT_CURRENCY")
    private BigInteger settlementCurrencys;
    @Column(name = "COUNTRY")
    private Long countrys;
    @Column(name = "CREATED_AT",insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bankId")
    private Set<UfsBankBranches> ufsBankBranchesSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bankId")
    private Set<UfsBankRegion> ufsBankRegionSet;
    @JoinColumn(name = "TENANT_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private UfsOrganizationUnits tenantId;
    @Column(name = "TENANT_ID")
    private BigDecimal tenantIds;
    public UfsBanks() {
    }

    public UfsBanks(Long id) {
        this.id = id;
    }

    public UfsBanks(Long id, String bankName, String bankType) {
        this.id = id;
        this.bankName = bankName;
        this.bankType = bankType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigInteger getBankGuarantee() {
        return bankGuarantee;
    }

    public void setBankGuarantee(BigInteger bankGuarantee) {
        this.bankGuarantee = bankGuarantee;
    }

    public BigInteger getInterchangeFee() {
        return interchangeFee;
    }

    public void setInterchangeFee(BigInteger interchangeFee) {
        this.interchangeFee = interchangeFee;
    }

    public BigInteger getSetupFee() {
        return setupFee;
    }

    public void setSetupFee(BigInteger setupFee) {
        this.setupFee = setupFee;
    }

    public UfsCountries getCountry() {
        return country;
    }

    public UfsCurrency getSettlementCurrency() {
        return settlementCurrency;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @XmlTransient
    @JsonIgnore
    public Set<UfsBankBranches> getUfsBankBranchesSet() {
        return ufsBankBranchesSet;
    }

    public void setUfsBankBranchesSet(Set<UfsBankBranches> ufsBankBranchesSet) {
        this.ufsBankBranchesSet = ufsBankBranchesSet;
    }

    @XmlTransient
    @JsonIgnore
    public Set<UfsBankRegion> getUfsBankRegionSet() {
        return ufsBankRegionSet;
    }

    public void setUfsBankRegionSet(Set<UfsBankRegion> ufsBankRegionSet) {
        this.ufsBankRegionSet = ufsBankRegionSet;
    }

    public UfsOrganizationUnits getTenantId() {
        return tenantId;
    }

    public void setTenantId(UfsOrganizationUnits tenantId) {
        this.tenantId = tenantId;
    }


    public void setCountry(UfsCountries country) {
        this.country = country;
    }

    public void setSettlementCurrency(UfsCurrency settlementCurrency) {
        this.settlementCurrency = settlementCurrency;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public BigInteger getSettlementCurrencys() {
        return settlementCurrencys;
    }

    public void setSettlementCurrencys(BigInteger settlementCurrencys) {
        this.settlementCurrencys = settlementCurrencys;
    }

    public Long getCountrys() {
        return countrys;
    }

    public void setCountrys(Long countrys) {
        this.countrys = countrys;
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
        if (!(object instanceof UfsBanks)) {
            return false;
        }
        UfsBanks other = (UfsBanks) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ke.tra.ufs.webportal.entities.UfsBanks[ id=" + id + " ]";
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankIdentifier() {
        return bankIdentifier;
    }

    public void setBankIdentifier(String bankIdentifier) {
        this.bankIdentifier = bankIdentifier;
    }

    public String getSettlementAccount() {
        return settlementAccount;
    }

    public void setSettlementAccount(String settlementAccount) {
        this.settlementAccount = settlementAccount;
    }

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    public String getCommisionDefinition() {
        return commisionDefinition;
    }

    public void setCommisionDefinition(String commisionDefinition) {
        this.commisionDefinition = commisionDefinition;
    }


    public String getActionStatus() {
        return actionStatus;
    }

    public void setActionStatus(String actionStatus) {
        this.actionStatus = actionStatus;
    }


    @XmlTransient
    @JsonIgnore
    public Set<UfsBankBins> getUfsBankBinsSet() {
        return ufsBankBinsSet;
    }

    public void setUfsBankBinsSet(Set<UfsBankBins> ufsBankBinsSet) {
        this.ufsBankBinsSet = ufsBankBinsSet;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }


    public void setIntrash(String intrash) {
        this.intrash = intrash;
    }

    
}
