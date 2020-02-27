package ke.tra.ufs.webportal.entities;

import lombok.Data;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Date;

@Data
@Entity
@Table(name = "UFS_SETTLEMENT")
public class UfsSettlement {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Id
    @Column(name = "ID")
    private BigInteger id;

    @Column(name = "MERCHANT")
    private String merchantId;

    @Column(name = "INITIATION_MODE")
    private String initiationMode;

    @Column(name = "TERMINAL")
    private String terminalId;

    @Column(name = "POS_USER")
    private String posUserId;

    @Column(name = "TOTAL_AMOUNT")
    private String totalAmount;

    @Column(name = "TOTAL_TRANSACTIONS")
    private String totalTrx;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "INSERTTIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date settlementTime;
}
