/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.tra.com.tsync.entities;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
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

/**
 *
 * @author Mwagiru Kamoni
 */
@Entity
@Table(name = "UFS_BANKS")
@NamedQueries({
    @NamedQuery(name = "UfsBanks.findAll", query = "SELECT u FROM UfsBanks u")})
public class UfsBanks implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @Column(name = "BANK_NAME")
    private String bankName;
    @Column(name = "BANK_IDENTIFIER")
    private String bankIdentifier;
    @Column(name = "SETTLEMENT_ACCOUNT")
    private String settlementAccount;
    @Basic(optional = false)
    @Column(name = "BANK_TYPE")
    private String bankType;
    @Column(name = "COMMISION_DEFINITION")
    private String commisionDefinition;
    @Column(name = "BANK_GUARANTEE")
    private BigInteger bankGuarantee;
    @Column(name = "INTERCHANGE_FEE")
    private BigInteger interchangeFee;
    @Column(name = "SETUP_FEE")
    private BigInteger setupFee;
    @Column(name = "CREATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column(name = "ACTION")
    private String action;
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Column(name = "INTRASH")
    private String intrash;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bankId")
    private Collection<UfsBankBranches> ufsBankBranchesCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bankId")
    private Collection<UfsBankRegion> ufsBankRegionCollection;
    @JoinColumn(name = "TENANT_ID", referencedColumnName = "U_UID")
    @ManyToOne(optional = false)
    private UfsOrganizationUnits tenantId;
    @JoinColumn(name = "SETTLEMENT_CURRENCY", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne
    private UfsCurrency settlementCurrency;
    @JoinColumn(name = "COUNTRY", referencedColumnName = "ID")
    @ManyToOne
    private UfsCountries country;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bankId")
    private Collection<UfsBankBins> ufsBankBinsCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bankId")
    private Collection<UfsGls> ufsGlsCollection;

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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
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

    public Collection<UfsBankBranches> getUfsBankBranchesCollection() {
        return ufsBankBranchesCollection;
    }

    public void setUfsBankBranchesCollection(Collection<UfsBankBranches> ufsBankBranchesCollection) {
        this.ufsBankBranchesCollection = ufsBankBranchesCollection;
    }

    public Collection<UfsBankRegion> getUfsBankRegionCollection() {
        return ufsBankRegionCollection;
    }

    public void setUfsBankRegionCollection(Collection<UfsBankRegion> ufsBankRegionCollection) {
        this.ufsBankRegionCollection = ufsBankRegionCollection;
    }

    public UfsOrganizationUnits getTenantId() {
        return tenantId;
    }

    public void setTenantId(UfsOrganizationUnits tenantId) {
        this.tenantId = tenantId;
    }

    public UfsCurrency getSettlementCurrency() {
        return settlementCurrency;
    }

    public void setSettlementCurrency(UfsCurrency settlementCurrency) {
        this.settlementCurrency = settlementCurrency;
    }

    public UfsCountries getCountry() {
        return country;
    }

    public void setCountry(UfsCountries country) {
        this.country = country;
    }

    public Collection<UfsBankBins> getUfsBankBinsCollection() {
        return ufsBankBinsCollection;
    }

    public void setUfsBankBinsCollection(Collection<UfsBankBins> ufsBankBinsCollection) {
        this.ufsBankBinsCollection = ufsBankBinsCollection;
    }

    public Collection<UfsGls> getUfsGlsCollection() {
        return ufsGlsCollection;
    }

    public void setUfsGlsCollection(Collection<UfsGls> ufsGlsCollection) {
        this.ufsGlsCollection = ufsGlsCollection;
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
        return "com.mycompany.oracleufs.UfsBanks[ id=" + id + " ]";
    }
    
}
