package ke.co.tra.ufs.tms.entities;

import ke.axle.chassis.annotations.Filter;
import ke.axle.chassis.annotations.Searchable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "CRDBBILLERS_GEPG_LOGS")
public class CrdbBillers implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ID")
    private Long id;
    @Searchable
    @Filter
    @Column(name = "REQUEST_NAME")
    private String requestName;
    @Searchable
    @Filter
    @Column(name = "CODE")
    private String code;
    @Searchable
    @Filter
    @Column(name = "REQUEST_ID")
    private String requestId;
    @Searchable
    @Filter
    @Column(name = "PAYMENT_REFERENCE")
    private String paymentReference;
    @Searchable
    @Filter
    @Column(name = "PAYMENT_TYPE")
    private String paymentType;
    @Searchable
    @Filter
    @Column(name = "OWNER")
    private String owner;
    @Searchable
    @Filter
    @Column(name = "CUSTOMER_EMAIL")
    private String customerEmail;
    @Searchable
    @Filter
    @Column(name = "CUSTOMER_MOBILE")
    private String customerMobile;
    @Searchable
    @Filter
    @Column(name = "SERVICE_NAME")
    private String serviceName;
    @Searchable
    @Filter
    @Column(name = "PAYMENT_GFS_CODE")
    private String paymentGfsCode;
    @Searchable
    @Filter
    @Column(name = "CURRENCY")
    private String currency;
    @Searchable
    @Filter
    @Column(name = "PAYMENT_DESC")
    private String paymentDesc;
    @Searchable
    @Filter
    @Column(name = "PAYMENT_EXPIRY")
    private String paymentExpiry;
    @Searchable
    @Filter
    @Column(name = "PAYMENT_OPTION")
    private String paymentOption;
    @Searchable
    @Filter
    @Column(name = "AMOUNT")
    private String amount;
    @Searchable
    @Filter
    @Column(name = "MESSAGE")
    private String message;
    @Filter(isDateRange = true)
    @Column(name = "INSERTTIME")
    @Temporal(TemporalType.DATE)
    private Date insertTime;
    @Searchable
    @Filter
    @Column(name = "REQUEST_DIRECTION")
    private String requestDirection;
    @Searchable
    @Filter
    @Column(name = "GEPG_RECEIPT")
    private String gepgReceipt;
    @Searchable
    @Filter
    @Column(name = "CALLBACK_URL")
    private String callbackUrl;
    @Searchable
    @Filter
    @Column(name = "TID")
    private String tid;
    @Searchable
    @Filter
    @Column(name = "MID")
    private String mid;
    @Searchable
    @Filter
    @Column(name = "POS_REF")
    private String posRef;
    @Searchable
    @Filter
    @Column(name = "SWITCH_AUTH_CODE")
    private String switchAuthCode;
    @Searchable
    @Filter
    @Column(name = "STATUS")
    private String status;
    @Searchable
    @Filter
    @Column(name = "STATUSDESC")
    private String statusDesc;
    @Searchable
    @Filter
    @Column(name = "RETRIESCOUNT")
    private String retriesCount;
    @Searchable
    @Filter
    @Column(name = "HTTPCODE")
    private String httpCode;
    @Searchable
    @Filter
    @Column(name = "REQUESTSENDDESCRPTION")
    private String requestSendDescription;
    @Searchable
    @Filter
    @Column(name = "ISORGINALREQUEST")
    private String isOriginalRequest;
    @Searchable
    @Filter
    @Column(name = "POSSTAN")
    private String posStan;
    @Searchable
    @Filter
    @Column(name = "BILLER")
    private String biller;
}
