package ke.tra.ufs.webportal.entities.views;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

@Data
@Entity
@Table(name = "VW_MERCHANTS")
public class VwMerchants implements Serializable {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Id
    @Column(name = "ID")
    private BigInteger id;

    @Column(name = "MERCHANT_NAME")
    private String merchantName;

    @Column(name = "MID")
    private String merchantId;

    @Column(name = "TID")
    private String terminalId;

    @Column(name = "AMOUNT")
    private String purchaseAmount;

    @Column(name = "LOCATION")
    private String location;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "ONBOARDING_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date onBoardingDate;
}
