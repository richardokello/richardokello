package co.ke.tracom.bprgateway.web.wasac.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.experimental.FieldNameConstants;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "BPR_WASC_AUDIT_TRAIL")
@Table(name = "BPR_WASC_AUDIT_TRAIL")
public class WascWaterTxnLog {
    @Id
    @SequenceGenerator(name = "BPR_WASC_AUDIT_TRAIL_SEQ", sequenceName = "BPR_WASC_AUDIT_TRAIL_SEQ",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BPR_WASC_AUDIT_TRAIL_SEQ")
    @Column(name = "ID")
    private Long id;
    @Column(name = "TID")
    private String tid;
    @Column(name = "MID")
    private String mid;
    @Column(name = "POS_REF")
    private String posRef;
    @Column(name = "METER_NO")
    private String meterNo;
    @Column(name = "CUSTOMER_NAME")
    private String customerName;
    @Column(name = "BANK_BRANCH")
    private String bankBranch;
    @FieldNameConstants.Exclude
    private String amount;
    @Column(name = "GATEWAY_T24_POSTING_STATUS")
    private String gatewayT24PostingStatus;
    @Column(name = "CREATION_DATE")
    private Date creationDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getPosRef() {
        return posRef;
    }

    public void setPosRef(String posRef) {
        this.posRef = posRef;
    }

    public String getMeterNo() {
        return meterNo;
    }

    public void setMeterNo(String meterNo) {
        this.meterNo = meterNo;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getBankBranch() {
        return bankBranch;
    }

    public void setBankBranch(String bankBranch) {
        this.bankBranch = bankBranch;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getGatewayT24PostingStatus() {
        return gatewayT24PostingStatus;
    }

    public void setGatewayT24PostingStatus(String gatewayT24PostingStatus) {
        this.gatewayT24PostingStatus = gatewayT24PostingStatus;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
