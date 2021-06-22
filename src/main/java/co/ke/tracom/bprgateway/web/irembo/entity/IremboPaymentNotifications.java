package co.ke.tracom.bprgateway.web.irembo.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "IREMBOPAYMENTNOTIFICATIONS")
public class IremboPaymentNotifications implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(nullable = false, precision = 19)
    private BigDecimal ipnid;
    private String billid;
    private BigDecimal amount;
    private String paymenttype;
    private String payername;
    private String payerphone;
    private String paymentreference;
    private String accountnumber;
    private String description;
    private String paymentdatetime;
    private String paymentstatus;
    private String paymentchannel;
    private int trials;
    private String resend;

}


