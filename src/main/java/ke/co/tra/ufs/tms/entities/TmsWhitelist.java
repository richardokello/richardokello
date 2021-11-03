/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import ke.axle.chassis.annotations.Filter;
import ke.axle.chassis.annotations.ModifiableField;
import ke.axle.chassis.annotations.Searchable;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author tracom9
 */
@Entity
@Table(name = "TMS_WHITELIST")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "TmsWhitelist.findAll", query = "SELECT t FROM TmsWhitelist t")
        , @NamedQuery(name = "TmsWhitelist.findById", query = "SELECT t FROM TmsWhitelist t WHERE t.id = :id")
        , @NamedQuery(name = "TmsWhitelist.findBySerialNo", query = "SELECT t FROM TmsWhitelist t WHERE t.serialNo = :serialNo")
        , @NamedQuery(name = "TmsWhitelist.findByModelId", query = "SELECT t FROM TmsWhitelist t WHERE t.modelId = :modelId")
        , @NamedQuery(name = "TmsWhitelist.findByCreationDate", query = "SELECT t FROM TmsWhitelist t WHERE t.creationDate = :creationDate")
        , @NamedQuery(name = "TmsWhitelist.findByAction", query = "SELECT t FROM TmsWhitelist t WHERE t.action = :action")
        , @NamedQuery(name = "TmsWhitelist.findByActionStatus", query = "SELECT t FROM TmsWhitelist t WHERE t.actionStatus = :actionStatus")
        , @NamedQuery(name = "TmsWhitelist.findByIntrash", query = "SELECT t FROM TmsWhitelist t WHERE t.intrash = :intrash")})
public class TmsWhitelist implements Serializable {

    @JoinColumn(name = "MODEL_ID", referencedColumnName = "MODEL_ID", updatable = false, insertable = false)
    @ManyToOne(optional = false)
    private UfsDeviceModel modelId;

    @Filter
    @Column(name = "ASSIGNED", insertable = false)
    private Short assigned;

    @Filter
    @Column(name = "ASSIGNED", updatable = false, insertable = false)
    private String assignStr;

    @ModifiableField
    @Column(name = "MODEL_ID")
    private BigDecimal modelIds;

    @JoinColumn(name = "BATCH_ID", referencedColumnName = "BATCH_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private TmsWhitelistBatch batch;

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @GenericGenerator(
            name = "TMS_WHITELIST_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "TMS_WHITELIST_SEQ")
                    ,
                    @Parameter(name = "initial_value", value = "1")
                    ,
                    @Parameter(name = "increment_size", value = "1")
            }
    )
    @GeneratedValue(generator = "TMS_WHITELIST_SEQ")
    @Column(name = "ID")
    private BigDecimal id;
    @Basic(optional = false)
    @NotNull
    @Filter
    @Size(min = 1, max = 50)
    @Column(name = "SERIAL_NO")
    private String serialNo;
    @Column(name = "CREATION_DATE", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Size(max = 15)
    @ModifiableField
    @Column(name = "ACTION")
    private String action;
    @Size(max = 15)
    @ModifiableField
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Size(max = 5)
    @ModifiableField
    @Column(name = "INTRASH")
    private String intrash;
    @Column(name = "PURCHASE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date purchaseDate;
    @ModifiableField
    @Column(name = "PRODUCT_NO")
    private String productNo;
    @ModifiableField
    @Column(name = "LOCATION")
    private String location;
    @ModifiableField
    @Column(name = "DELIVERED_BY")
    private String deliveredBy;
    @ModifiableField
    @Column(name = "RECEIVED_BY")
    private String receivedBy;
    @JoinColumn(name = "BANK_ID", referencedColumnName = "ID", updatable = false, insertable = false)
    @ManyToOne(optional = false)
    private UfsBanks ufsBanks;
    @ModifiableField
    @Filter
    @Column(name = "BANK_ID")
    private Long ufsBankId;


    public TmsWhitelist() {
    }

    public TmsWhitelist(BigDecimal id) {
        this.id = id;
    }

    public TmsWhitelist(BigDecimal id, String serialNo, UfsDeviceModel modelId) {
        this.id = id;
        this.serialNo = serialNo;
        this.modelId = modelId;
    }

    public TmsWhitelist(String serialNo, BigDecimal modelId, String action, String actionStatus, String intrash, Long ufsBankId) {
        this.serialNo = serialNo;
        this.modelIds = modelId;
        this.action = action;
        this.actionStatus = actionStatus;
        this.intrash = intrash;
        this.ufsBankId = ufsBankId;
    }

    public TmsWhitelist(String serialNo, BigDecimal modelId, String action, String actionStatus, String intrash,
                        Date purchaseDate, String productNo, String location, String deliveredBy, String receivedBy, Long ufsBankId) {
        this.serialNo = serialNo;
        this.modelIds = modelId;
        this.action = action;
        this.actionStatus = actionStatus;
        this.intrash = intrash;
        this.purchaseDate = purchaseDate;
        this.productNo = productNo;
        this.location = location;
        this.deliveredBy = deliveredBy;
        this.receivedBy = receivedBy;
        this.ufsBankId = ufsBankId;

    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public BigDecimal getModelIds() {
        return modelIds;
    }

    public void setModelIds(BigDecimal modelIds) {
        this.modelIds = modelIds;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
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

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getProductNo() {
        return productNo;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDeliveredBy() {
        return deliveredBy;
    }

    public void setDeliveredBy(String deliveredBy) {
        this.deliveredBy = deliveredBy;
    }

    public String getReceivedBy() {
        return receivedBy;
    }

    public void setReceivedBy(String receivedBy) {
        this.receivedBy = receivedBy;
    }

   public Short getAssigned() {
        return assigned;
    }

    public void setAssigned(Short assigned) {
        this.assigned = assigned;
    }

    public String getAssignStr() {
        return assignStr;
    }

    public void setAssignStr(String assignStr) {
        this.assignStr = assignStr;
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
        if (!(object instanceof TmsWhitelist)) {
            return false;
        }
        TmsWhitelist other = (TmsWhitelist) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ke.co.tra.ufs.tms.entities.TmsWhitelist[ id=" + id + " ]";
    }

    public UfsDeviceModel getModelId() {
        return modelId;
    }

    public void setModelId(UfsDeviceModel modelId) {
        this.modelId = modelId;
    }

    public TmsWhitelistBatch getBatch() {
        return batch;
    }

    public void setBatch(TmsWhitelistBatch batch) {
        this.batch = batch;
    }

    public UfsBanks getUfsBanks() {
        return ufsBanks;
    }

    public void setUfsBanks(UfsBanks ufsBanks) {
        this.ufsBanks = ufsBanks;
    }

    public Long getUfsBankId() {
        return ufsBankId;
    }

    public void setUfsBankId(Long ufsBankId) {
        this.ufsBankId = ufsBankId;
    }
}
