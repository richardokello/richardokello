package ke.tra.ufs.webportal.entities;

import ke.tra.ufs.webportal.utils.annotations.Filter;
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

    @Filter
    @Column(name = "MERCHANT")
    private String merchantId;

    @Filter
    @Column(name = "INITIATION_MODE")
    private String initiationMode;

    @Filter
    @Column(name = "TERMINAL")
    private String terminalId;

    @Filter
    @Column(name = "POS_USER")
    private String posUserId;

    @Column(name = "TOTAL_AMOUNT")
    private String totalAmount;

    @Column(name = "TOTAL_TRANSACTIONS")
    private String totalTrx;

    @Filter
    @Column(name = "STATUS")
    private String status;

    @Filter
    @Column(name = "INSERTTIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date settlementTime;
}
