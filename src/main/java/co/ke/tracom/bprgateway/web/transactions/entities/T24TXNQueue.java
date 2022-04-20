package co.ke.tracom.bprgateway.web.transactions.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "T24TXNSQUEUE")
public class T24TXNQueue implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name="T24TXNQUEUEID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long t24TXNQueueid;
    @Column(length = 20)
    private Long starttime;
    @Column(length = 20)
    private Long endtime;
    @Column(length = 20)
    private String txnchannel;
    @Column(length = 20)
    private String gatewayref;
    @Column(length = 20)
    private String t24reference;
    @Column(length = 2000)
    private String requestleg;
    @Column(length = 3000)
    private String responseleg;
    @Column(length = 20)
    private String t24responsecode;
    @Column(length = 400)
    private String postedstatus;

    @Column(name = "INSERTTIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date inserttime;
    @Column(length = 50)
    private String cradvicereqdyn;
    @Column(length = 50)
    private String chargecode;
    @Column(length = 50)
    private String debitamount;
    @Column(length = 50)
    private String cocode;
    @Column(length = 50)
    private String creditcustomer;
    @Column(length = 50)
    private String returntodept;
    @Column(length = 50)
    private String creditvaluedate;
    @Column(length = 50)
    private String creditcompcode;
    @Column(length = 50)
    private String roundtype;
    @Column(length = 50)
    private String debitcurrency;
    @Column(length = 50)
    private String inputter;
    @Column(length = 50)
    private String positiontype;
    @Column(length = 50)
    private String totsndchgcrccy;
    @Column(length = 50)
    private String totreccommlcl;
    @Column(length = 50)
    private String locamtcredited;
    @Column(length = 50)
    private String ratefixing;
    @Column(length = 50)
    private String commissioncode;
    @Column(length = 50)
    private String amountdebited;
    @Column(length = 50)
    private String totrecchg;
    @Column(length = 50)
    private String currencymktcr;
    @Column(length = 50)
    private String chargecomdisplay;
    @Column(length = 50)
    private String transactiontype;
    @Column(length = 50)
    private String debitcompcode;
    @Column(length = 50)
    private String chargedcustomer;
    @Column(length = 50)
    private String processingdate;
    @Column(length = 50)
    private String creditacctno;
    @Column(length = 50)
    private String totrecchglcl;
    @Column(length = 50)
    private String locamtdebited;
    @Column(length = 50)
    private String datetime;
    @Column(length = 50)
    private String totreccomm;
    @Column(length = 50)
    private String profitcentrecust;
    @Column(length = 50)
    private String stmtnos;
    @Column(length = 50)
    private String debitvaluedate;
    @Column(length = 50)
    private String recordstatus;
    @Column(length = 50)
    private String dradvicereqdyn;
    @Column(length = 50)
    private String currencymktdr;
    @Column(length = 50)
    private String amountcredited;
    @Column(length = 50)
    private String fedfunds;
    @Column(length = 50)
    private String creditcurrency;
    @Column(length = 50)
    private String debitacctno;
    @Column(length = 50)
    private String totrecchgcrccy;
    @Column(length = 50)
    private String deptcode;
    @Column(length = 50)
    private String debitcustomer;
    @Column(length = 50)
    private String custgrouplevel;
    @Column(length = 50)
    private String currno;
    @Column(length = 20)
    private int attempts;
    @Column(length = 20)
    private String baladvise;
    @Column(length = 50)
    private String t24failnarration;
    @Column(length = 10)
    private String procode;
    private String accountname;
    private String tid;
    private String currency;
    private String accounttitle;
    private String custid;
    private String workingbal;
    private String onlinebal;
    private String legalid;
    private String towncountry;
    private String phone;
    private String dateofbirth;
    private String custemail;
    private String businessloc;
    private String sector;
    private String txnmti;
    private String tot24debitaccount;
    private String tot24creditaccount;
    private String chargesacctno;
    private String totalchargeamt;
    private String meterno;
    private String customerName;
    private String tokenNo;
    private String unitsKw;
    private String meterLocation;
    @Column(name = "IDENTIFICATION")
    private String identification;

}
