package co.ke.tracom.bprgateway.web.transactions.entities;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ALL_TRANSACTIONS")
public class AllTransactions implements java.io.Serializable {

    @Id
    @Column(name = "TRANSACTIONNUMBER")
    private long transactionNumber;
    @Column(name = "T24POSREF")
    private String t24PosRef;
    @Column(name = "T24RESPONSECODE")
    private String t24ResponseCode;
    @Column(name = "T24REFERENCE")
    private String t24Reference;
    @Column(name = "CREDITACCTNO")
    private String creditAccountNo;
    @Column(name = "DEBITACCTNO")
    private String debitAccountNo;
//added this column to help in reporting.. differenciate PC module and POS transacations.
    @Column(name = "TXNTYPE", columnDefinition = "varchar(30) default 'POS'")
    private  String txnType;
    @Column(name = "INSERTTIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date insertTime;

    @Column(name = "field000")
    private String field000;

    @Column(name = "field002")
    private String field002;

    @Column(name = "field003")
    private String field003;

    @Column(name = "field004")
    private String field004;

    @Column(name = "field005")
    private String field005;

    @Column(name = "field037")
    private String field037;

    @Column(name = "field039")
    private String field039;

    @Column(name = "field041")
    private String field041;

    @Column(name = "field042")
    private String field042;

    @Column(name = "field061")
    private String field061;

    @Column(name = "field121")
    private String field121;

    @Column(name = "FIELD123")
    private String field123;

    public AllTransactions() {
    }

    public long getTransactionNumber() {
        return transactionNumber;
    }

    public AllTransactions setTransactionNumber(long transactionNumber) {
        this.transactionNumber = transactionNumber;
        return this;
    }



    public String getTxnType() {
        return txnType;
    }

    public AllTransactions setTxnType(String txnType) {
        this.txnType = txnType;
        return this;
    }

    public String getT24PosRef() {
        return t24PosRef;
    }

    public AllTransactions setT24PosRef(String t24PosRef) {
        this.t24PosRef = t24PosRef;
        return this;
    }

    public String getT24ResponseCode() {
        return t24ResponseCode;
    }

    public AllTransactions setT24ResponseCode(String t24ResponseCode) {
        this.t24ResponseCode = t24ResponseCode;
        return this;
    }

    public String getCreditAccountNo() {
        return creditAccountNo;
    }

    public AllTransactions setCreditAccountNo(String creditAccountNo) {
        this.creditAccountNo = creditAccountNo;
        return this;
    }

    public String getDebitAccountNo() {
        return debitAccountNo;
    }

    public AllTransactions setDebitAccountNo(String debitAccountNo) {
        this.debitAccountNo = debitAccountNo;
        return this;
    }

    public Date getInsertTime() {
        return insertTime;
    }

    public AllTransactions setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
        return this;
    }

    public String getT24Reference() {
        return t24Reference;
    }

    public AllTransactions setT24Reference(String t24Reference) {
        this.t24Reference = t24Reference;
        return this;
    }

    public String getField000() {
        return field000;
    }

    public AllTransactions setField000(String field000) {
        this.field000 = field000;
        return this;
    }

    public String getField002() {
        return field002;
    }

    public AllTransactions setField002(String field002) {
        this.field002 = field002;
        return this;
    }

    public String getField003() {
        return field003;
    }

    public AllTransactions setField003(String field003) {
        this.field003 = field003;
        return this;
    }

    public String getField004() {
        return field004;
    }

    public AllTransactions setField004(String field004) {
        this.field004 = field004;
        return this;
    }

    public String getField005() {
        return field005;
    }

    public AllTransactions setField005(String field005) {
        this.field005 = field005;
        return this;
    }

    public String getField037() {
        return field037;
    }

    public AllTransactions setField037(String field037) {
        this.field037 = field037;
        return this;
    }

    public String getField039() {
        return field039;
    }

    public AllTransactions setField039(String field039) {
        this.field039 = field039;
        return this;
    }

    public String getField041() {
        return field041;
    }

    public AllTransactions setField041(String field041) {
        this.field041 = field041;
        return this;
    }

    public String getField042() {
        return field042;
    }

    public AllTransactions setField042(String field042) {
        this.field042 = field042;
        return this;
    }

    public String getField061() {
        return field061;
    }

    public AllTransactions setField061(String field061) {
        this.field061 = field061;
        return this;
    }

    public String getField121() {
        return field121;
    }

    public AllTransactions setField121(String field121) {
        this.field121 = field121;
        return this;
    }

    public String getField123() {
        return field123;
    }

    public AllTransactions setField123(String field123) {
        this.field123 = field123;
        return this;
    }

    @Override
    public String toString() {
        return "AllTransactions{" +
                "transactionNumber=" + transactionNumber +
                ", t24PosRef='" + t24PosRef + '\'' +
                ", t24ResponseCode='" + t24ResponseCode + '\'' +
                ", t24Reference='" + t24Reference + '\'' +
                ", creditAccountNo='" + creditAccountNo + '\'' +
                ", debitAccountNo='" + debitAccountNo + '\'' +
                ", insertTime=" + insertTime +
                ", field000='" + field000 + '\'' +
                ", field002='" + field002 + '\'' +
                ", field003='" + field003 + '\'' +
                ", field004='" + field004 + '\'' +
                ", field005='" + field005 + '\'' +
                ", field037='" + field037 + '\'' +
                ", field039='" + field039 + '\'' +
                ", field041='" + field041 + '\'' +
                ", field042='" + field042 + '\'' +
                ", field061='" + field061 + '\'' +
                ", field121='" + field121 + '\'' +
                ", field123='" + field123 + '\'' +
                '}';
    }
}