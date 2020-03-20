/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.tra.com.tsync.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Mwagiru Kamoni
 */
@Entity
@Table(name = "PAR_EMVCHIP_CONFIGS")
@NamedQueries({
    @NamedQuery(name = "ParEmvchipConfigs.findAll", query = "SELECT p FROM ParEmvchipConfigs p")})
public class ParEmvchipConfigs implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private BigDecimal id;
    @Column(name = "APP_IDENTIFIER")
    private String appIdentifier;
    @Column(name = "TAC_DEFAULT")
    private String tacDefault;
    @Column(name = "TAC_DENIAL")
    private String tacDenial;
    @Column(name = "TAC_ONLINE")
    private String tacOnline;
    @Column(name = "THRESHOLD_VALUE")
    private Integer thresholdValue;
    @Column(name = "TARGET_PERCENTAGE")
    private Short targetPercentage;
    @Column(name = "MAX_TARGET_PERCENTAGE")
    private Short maxTargetPercentage;
    @Column(name = "DEFAULT_VALUE_DDOL")
    private String defaultValueDdol;
    @Column(name = "APP_VERSION_NO")
    private String appVersionNo;
    @Column(name = "APP_VERSION_NO_TERMINAL")
    private String appVersionNoTerminal;
    @Column(name = "ACQ_IDENTIFIER")
    private String acqIdentifier;
    @Column(name = "COLUMN1")
    private Integer column1;
    @Column(name = "TRANS_CAT_CODE")
    private String transCatCode;
    @Column(name = "EMV_AID_TRANS_TYPE")
    private String emvAidTransType;
    @Column(name = "ACTION")
    private String action;
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Column(name = "CREATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column(name = "INTRASH")
    private String intrash;

    public ParEmvchipConfigs() {
    }

    public ParEmvchipConfigs(BigDecimal id) {
        this.id = id;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getAppIdentifier() {
        return appIdentifier;
    }

    public void setAppIdentifier(String appIdentifier) {
        this.appIdentifier = appIdentifier;
    }

    public String getTacDefault() {
        return tacDefault;
    }

    public void setTacDefault(String tacDefault) {
        this.tacDefault = tacDefault;
    }

    public String getTacDenial() {
        return tacDenial;
    }

    public void setTacDenial(String tacDenial) {
        this.tacDenial = tacDenial;
    }

    public String getTacOnline() {
        return tacOnline;
    }

    public void setTacOnline(String tacOnline) {
        this.tacOnline = tacOnline;
    }

    public Integer getThresholdValue() {
        return thresholdValue;
    }

    public void setThresholdValue(Integer thresholdValue) {
        this.thresholdValue = thresholdValue;
    }

    public Short getTargetPercentage() {
        return targetPercentage;
    }

    public void setTargetPercentage(Short targetPercentage) {
        this.targetPercentage = targetPercentage;
    }

    public Short getMaxTargetPercentage() {
        return maxTargetPercentage;
    }

    public void setMaxTargetPercentage(Short maxTargetPercentage) {
        this.maxTargetPercentage = maxTargetPercentage;
    }

    public String getDefaultValueDdol() {
        return defaultValueDdol;
    }

    public void setDefaultValueDdol(String defaultValueDdol) {
        this.defaultValueDdol = defaultValueDdol;
    }

    public String getAppVersionNo() {
        return appVersionNo;
    }

    public void setAppVersionNo(String appVersionNo) {
        this.appVersionNo = appVersionNo;
    }

    public String getAppVersionNoTerminal() {
        return appVersionNoTerminal;
    }

    public void setAppVersionNoTerminal(String appVersionNoTerminal) {
        this.appVersionNoTerminal = appVersionNoTerminal;
    }

    public String getAcqIdentifier() {
        return acqIdentifier;
    }

    public void setAcqIdentifier(String acqIdentifier) {
        this.acqIdentifier = acqIdentifier;
    }

    public Integer getColumn1() {
        return column1;
    }

    public void setColumn1(Integer column1) {
        this.column1 = column1;
    }

    public String getTransCatCode() {
        return transCatCode;
    }

    public void setTransCatCode(String transCatCode) {
        this.transCatCode = transCatCode;
    }

    public String getEmvAidTransType() {
        return emvAidTransType;
    }

    public void setEmvAidTransType(String emvAidTransType) {
        this.emvAidTransType = emvAidTransType;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getIntrash() {
        return intrash;
    }

    public void setIntrash(String intrash) {
        this.intrash = intrash;
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
        if (!(object instanceof ParEmvchipConfigs)) {
            return false;
        }
        ParEmvchipConfigs other = (ParEmvchipConfigs) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.oracleufs.ParEmvchipConfigs[ id=" + id + " ]";
    }
    
}
