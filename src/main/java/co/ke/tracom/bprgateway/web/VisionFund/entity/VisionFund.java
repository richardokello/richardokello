package co.ke.tracom.bprgateway.web.VisionFund.entity;

import co.ke.tracom.bprgateway.web.VisionFund.data.TransactionType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "BPR_VISION_FUND_TXN_LOGS")
@Data
@NoArgsConstructor
public class VisionFund {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    private Integer Id;
    @Column(name="ACCOUNT_NUMBER")
    private String accountNumber;
    @Column(name="CURRENCY_CODE")
    private String currencyCode = "RWF";
    @Column(name="REFERENCE_NUMBER")
    private String referenceNumber;
    @Column(name="MOBILE_NUMBER")
    private String mobileNumber;
    @Column(name="TRANSACTION_DESCRIPTION")
    private String tranDesc;
    @Column(name="AMOUNT")
    private Long amount;
    @Column(name="LEGAL_ID")
    private String nationalID;

    @Column(name="TOKEN_NO")
    private String token;
    @Column(name="ACCOUNT_NAME")
    private String accountName;

    @Column(name="TRANSACTION_TYPE")
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

}

