/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.entities.views;

import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author ekangethe
 */
@Entity
@Table(name = "VW_UFS_ALLTXNS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VwUfsAlltxns.findAll", query = "SELECT v FROM VwUfsAlltxns v")
    , @NamedQuery(name = "VwUfsAlltxns.findById", query = "SELECT v FROM VwUfsAlltxns v WHERE v.id = :id")
    , @NamedQuery(name = "VwUfsAlltxns.findByMti", query = "SELECT v FROM VwUfsAlltxns v WHERE v.mti = :mti")
    , @NamedQuery(name = "VwUfsAlltxns.findByProcode", query = "SELECT v FROM VwUfsAlltxns v WHERE v.procode = :procode")
    , @NamedQuery(name = "VwUfsAlltxns.findByTransactiontype", query = "SELECT v FROM VwUfsAlltxns v WHERE v.transactiontype = :transactiontype")
    , @NamedQuery(name = "VwUfsAlltxns.findByAmount", query = "SELECT v FROM VwUfsAlltxns v WHERE v.amount = :amount")
    , @NamedQuery(name = "VwUfsAlltxns.findByStan", query = "SELECT v FROM VwUfsAlltxns v WHERE v.stan = :stan")
    , @NamedQuery(name = "VwUfsAlltxns.findByTid", query = "SELECT v FROM VwUfsAlltxns v WHERE v.tid = :tid")
    , @NamedQuery(name = "VwUfsAlltxns.findByMid", query = "SELECT v FROM VwUfsAlltxns v WHERE v.mid = :mid")
    , @NamedQuery(name = "VwUfsAlltxns.findByReferenceNo", query = "SELECT v FROM VwUfsAlltxns v WHERE v.referenceNo = :referenceNo")
    , @NamedQuery(name = "VwUfsAlltxns.findByResponsecode", query = "SELECT v FROM VwUfsAlltxns v WHERE v.responsecode = :responsecode")
    , @NamedQuery(name = "VwUfsAlltxns.findByActionStatus", query = "SELECT v FROM VwUfsAlltxns v WHERE v.actionStatus = :actionStatus")
    , @NamedQuery(name = "VwUfsAlltxns.findByCurrencycode", query = "SELECT v FROM VwUfsAlltxns v WHERE v.currencycode = :currencycode")
    , @NamedQuery(name = "VwUfsAlltxns.findByTransactiondate", query = "SELECT v FROM VwUfsAlltxns v WHERE v.transactiondate = :transactiondate")})
public class VwUfsAlltxns implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "ID")
    private BigInteger id;
    @Size(max = 255)
    @Column(name = "MTI")
    private String mti;
    @Size(max = 255)
    @Column(name = "PROCODE")
    private String procode;
    @Size(max = 255)
    @Column(name = "AUTHCODE")
    private String authcode;
    @Size(max = 30)
    @Column(name = "TRANSACTIONTYPE")
    private String transactiontype;
    @Size(max = 255)
    @Column(name = "AMOUNT")
    private String amount;
    @Size(max = 255)
    @Column(name = "AMOUNT", insertable = false, updatable = false)
    private Double trxAmount;
    @Size(max = 255)
    @Column(name = "STAN")
    private String stan;
    @Size(max = 255)
    @Column(name = "TID")
    private String tid;
    @Size(max = 255)
    @Column(name = "MID")
    private String mid;
    @Size(max = 255)
    @Column(name = "REFERENCE_NO")
    private String referenceNo;
    @Size(max = 255)
    @Column(name = "RESPONSECODE")
    private String responsecode;
    @Size(max = 20)
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Size(max = 255)
    @Column(name = "CURRENCYCODE")
    private String currencycode;
    @Column(name = "TRANSACTIONDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date transactiondate;

    public VwUfsAlltxns() {
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getMti() {
        return mti;
    }

    public void setMti(String mti) {
        this.mti = mti;
    }

    public String getProcode() {
        return procode;
    }

    public void setProcode(String procode) {
        this.procode = procode;
    }

    public String getTransactiontype() {
        return transactiontype;
    }

    public void setTransactiontype(String transactiontype) {
        this.transactiontype = transactiontype;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStan() {
        return stan;
    }

    public void setStan(String stan) {
        this.stan = stan;
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

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public String getResponsecode() {
        return responsecode;
    }

    public void setResponsecode(String responsecode) {
        this.responsecode = responsecode;
    }

    public String getActionStatus() {
        return actionStatus;
    }

    public void setActionStatus(String actionStatus) {
        this.actionStatus = actionStatus;
    }

    public String getCurrencycode() {
        return currencycode;
    }

    public void setCurrencycode(String currencycode) {
        this.currencycode = currencycode;
    }

    public Date getTransactiondate() {
        return transactiondate;
    }

    public void setTransactiondate(Date transactiondate) {
        this.transactiondate = transactiondate;
    }
    public Double getTrxAmount() {
        if (Objects.nonNull(trxAmount)) {
            DecimalFormat df = new DecimalFormat("0.00");  // Set your desired format here.
            String amnt = df.format((trxAmount / 100));
            Double dAmnt = Double.valueOf(amnt);
            return dAmnt;
        }
        return 0.0;
    }

    public void setTrxAmount(Double trxAmount) {
        this.trxAmount = trxAmount;
    }

    public String getAuthcode() {
        return authcode;
    }

    public void setAuthcode(String authcode) {
        this.authcode = authcode;
    }
}
