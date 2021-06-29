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
    @Column(name = "ADVICEID", nullable = false, precision = 19)
    private long adviceid;

    @Column(name = "TERMINALID")
    private String tid;

    @Column(name = "MERCHANTID")
    private String mid;

    @Column(name = "AMOUNT")
    private String amount;

    @Column(name = "TRANSACTIONTYPE")
    private String transactionType;

    @Column(name = "REQUESTTYPE")
    private String requestType;

    @Column(name = "RESPONSECODE")
    private String responseCode;

    @Column(name = "TRANSACTIONREF")
    private String transactionReference;

    @Column(name = "PAN")
    private String pan;

    @Column(name = "XMLDATAREQUEST")
    private String xmlDataRequest;

    @Column(name = "XMLDATARESPONSE")
    private String xmlDataResponse;

    @Column(name = "OTHERDATA")
    private String otherData;

    @Column(name = "TRIALS")
    private int trials;

    @Column(name = "ADVISED")
    private String advised;

    @Column(name = "REF_NUMBER")
    private String refNumber;

    @Column(name = "MESSAGE_ID")
    private String messageId;

    @Column(name = "T24_REF_NO")
    private String t24RefNo;

    @Column(name = "INSERTTIME")
    private Timestamp insertTime;

    @Column(name = "SENDTIME")
    private String sendTime;

}
