package co.ke.tracom.bprgateway.web.smsscheduled.entities;


import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

@Data
@Builder
@Entity
@Table(name = "BPRMONEYSEND")
public class ScheduledSMS implements java.io.Serializable {
    private static final long serialVersionUID = 5547062376313951258L;

    @Id
    @Basic(optional = false)
    @Column(nullable = false, precision = 19)
    private Long smsschedid;
    private String receiverphone;
    private String message;
    private String txnref;
    private int sentstatus;// let sent = 1 after sending
    private int attempts;  // default 0
    private String responsecode;
    private String responsecdesc;

    public ScheduledSMS() {

    }
}
