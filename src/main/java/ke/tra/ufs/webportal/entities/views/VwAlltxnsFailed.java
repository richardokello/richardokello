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
import java.util.Date;

/**
 *
 * @author Kenny
 */
@Entity
@Table(name = "VW_ALLTXNS_FAILED")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VwAlltxnsFailed.findAll", query = "SELECT v FROM VwAlltxnsFailed v"),
    @NamedQuery(name = "VwAlltxnsFailed.findById", query = "SELECT v FROM VwAlltxnsFailed v WHERE v.id = :id"),
    @NamedQuery(name = "VwAlltxnsFailed.findByMti", query = "SELECT v FROM VwAlltxnsFailed v WHERE v.mti = :mti"),
    @NamedQuery(name = "VwAlltxnsFailed.findByProcode", query = "SELECT v FROM VwAlltxnsFailed v WHERE v.procode = :procode"),
    @NamedQuery(name = "VwAlltxnsFailed.findByTransactiontype", query = "SELECT v FROM VwAlltxnsFailed v WHERE v.transactiontype = :transactiontype"),
    @NamedQuery(name = "VwAlltxnsFailed.findByAmount", query = "SELECT v FROM VwAlltxnsFailed v WHERE v.amount = :amount"),
    @NamedQuery(name = "VwAlltxnsFailed.findByStan", query = "SELECT v FROM VwAlltxnsFailed v WHERE v.stan = :stan"),
    @NamedQuery(name = "VwAlltxnsFailed.findByTid", query = "SELECT v FROM VwAlltxnsFailed v WHERE v.tid = :tid"),
    @NamedQuery(name = "VwAlltxnsFailed.findByMid", query = "SELECT v FROM VwAlltxnsFailed v WHERE v.mid = :mid"),
    @NamedQuery(name = "VwAlltxnsFailed.findByReferenceNo", query = "SELECT v FROM VwAlltxnsFailed v WHERE v.referenceNo = :referenceNo"),
    @NamedQuery(name = "VwAlltxnsFailed.findByResponsecode", query = "SELECT v FROM VwAlltxnsFailed v WHERE v.responsecode = :responsecode"),
    @NamedQuery(name = "VwAlltxnsFailed.findByCurrencycode", query = "SELECT v FROM VwAlltxnsFailed v WHERE v.currencycode = :currencycode"),
    @NamedQuery(name = "VwAlltxnsFailed.findByTransactiondate", query = "SELECT v FROM VwAlltxnsFailed v WHERE v.transactiondate = :transactiondate")})
public class VwAlltxnsFailed implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Id
    @Column(name = "ID")
    private BigInteger id;
    @Size(max = 255)
    @Column(name = "MTI")
    private String mti;
    @Size(max = 255)
    @Column(name = "PROCODE")
    private String procode;
    @Size(max = 30)
    @Column(name = "TRANSACTIONTYPE")
    private String transactiontype;
    @Size(max = 255)
    @Column(name = "AMOUNT")
    private String amount;
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
    @Size(max = 255)
    @Column(name = "CURRENCYCODE")
    private String currencycode;
    @Column(name = "TRANSACTIONDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date transactiondate;

    public VwAlltxnsFailed() {
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
    
}
