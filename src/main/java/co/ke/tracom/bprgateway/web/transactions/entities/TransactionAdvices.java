package co.ke.tracom.bprgateway.web.transactions.entities;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "TRANSACTIONADVICES")
public class TransactionAdvices {
    @Id
    @Basic(optional = false)
    @Column(nullable = false, precision = 19)
    private long adviceid;
    private String tid;
    private String mid;
    private String amount;
    private String transtype;
    private String reqtype;
    private String rspcode;
    private String walletid;
    private String transref;
    private String pan;
    private String xmldatarequest;
    private String xmldataresponse;
    private String otherdata;
    private int trials;
    private String advised;

    @Column(name = "ref_number")
    private String refNumber;
    private String message_id;
    private String t24RefNo;
    private Timestamp inserttime;
    private String sendtime;

}
