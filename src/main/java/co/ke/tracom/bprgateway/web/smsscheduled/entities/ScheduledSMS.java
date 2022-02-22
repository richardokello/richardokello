package co.ke.tracom.bprgateway.web.smsscheduled.entities;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "BPRSMSSCHEDULED")
public class ScheduledSMS implements java.io.Serializable {
    private static final long serialVersionUID = 5547062376313951258L;

    @Id
    @Basic(optional = false)
    @Column(nullable = false, precision = 19)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long smsschedid;
    @Column(name = "SENDMONEYTOKENSTARTTIME")
    private Long sendmoneytokenstarttime;
    @Column(name = "RECEIVERPHONE")
    private String receiverphone;
    @Column(name = "MONEYSENDID")
    private Long sendMoneyId;
    @Column(name = "MESSAGE")
    private String message;
    @Column(name = "TXNREF")
    private String txnref;
    @Column(name = "SENTSTATUS")
    private int sentstatus;// let sent = 1 after sending
    @Column(name = "ATTEMPTS")
    private int attempts;  // default 0
    @Column(name = "RESPONSECODE")
    private String responsecode;
    @Column(name = "RESPONSEDESC")
    private String responsecdesc;

    public ScheduledSMS() {

    }
}
