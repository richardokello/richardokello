/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Kenny
 */
@Entity
@Table(name = "VW_ALLTXNS_SUCCESSFAILED")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VwAlltxnsSuccessfailed.findAll", query = "SELECT v FROM VwAlltxnsSuccessfailed v"),
    @NamedQuery(name = "VwAlltxnsSuccessfailed.findByMti", query = "SELECT v FROM VwAlltxnsSuccessfailed v WHERE v.mti = :mti"),
    @NamedQuery(name = "VwAlltxnsSuccessfailed.findByProcode", query = "SELECT v FROM VwAlltxnsSuccessfailed v WHERE v.procode = :procode"),
    @NamedQuery(name = "VwAlltxnsSuccessfailed.findByTransactiontype", query = "SELECT v FROM VwAlltxnsSuccessfailed v WHERE v.transactiontype = :transactiontype"),
    @NamedQuery(name = "VwAlltxnsSuccessfailed.findByAmount", query = "SELECT v FROM VwAlltxnsSuccessfailed v WHERE v.amount = :amount"),
    @NamedQuery(name = "VwAlltxnsSuccessfailed.findByStan", query = "SELECT v FROM VwAlltxnsSuccessfailed v WHERE v.stan = :stan"),
    @NamedQuery(name = "VwAlltxnsSuccessfailed.findByTid", query = "SELECT v FROM VwAlltxnsSuccessfailed v WHERE v.tid = :tid"),
    @NamedQuery(name = "VwAlltxnsSuccessfailed.findByMid", query = "SELECT v FROM VwAlltxnsSuccessfailed v WHERE v.mid = :mid"),
    @NamedQuery(name = "VwAlltxnsSuccessfailed.findByReferenceNo", query = "SELECT v FROM VwAlltxnsSuccessfailed v WHERE v.referenceNo = :referenceNo"),
    @NamedQuery(name = "VwAlltxnsSuccessfailed.findByResponsecode", query = "SELECT v FROM VwAlltxnsSuccessfailed v WHERE v.responsecode = :responsecode"),
    @NamedQuery(name = "VwAlltxnsSuccessfailed.findByCurrencycode", query = "SELECT v FROM VwAlltxnsSuccessfailed v WHERE v.currencycode = :currencycode"),
    @NamedQuery(name = "VwAlltxnsSuccessfailed.findByTransactiondate", query = "SELECT v FROM VwAlltxnsSuccessfailed v WHERE v.transactiondate = :transactiondate")})
public class VwAlltxnsSuccessfailed implements Serializable {

    private static final long serialVersionUID = 1L;
    @Size(max = 255)
    @Column(name = "MTI")
    private String mti;
    @Size(max = 255)
    @Column(name = "PROCODE")
    private String procode;
    @Size(max = 43)
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

    public VwAlltxnsSuccessfailed() {
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
