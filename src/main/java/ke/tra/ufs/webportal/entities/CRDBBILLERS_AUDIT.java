package ke.tra.ufs.webportal.entities;


import ke.axle.chassis.annotations.Filter;
import ke.axle.chassis.annotations.Searchable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Mwagiru Kamoni
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "CRDBBILLERS_GEPG_LOGS")
public class CRDBBILLERS_AUDIT implements Serializable {

    @Id
    @Column(name = "ID")
    private BigDecimal crdb_billers_id;

    @Column(name = "REQUEST_NAME")
    @Filter
    @Searchable
    private String requestName; // GETCONTROL, GETCONTROLRESPONSE

    @Column(name = "CODE")
    @Filter
    @Searchable
    private String code;

    @Column(name = "REQUEST_ID")
    @Filter
    @Searchable
    private String requestID;

    @Column(name = "PAYMENT_REFERENCE")
    @Filter
    @Searchable
    private String paymentReference;

    @Column(name = "CALLBACK_URL")
    private String callbackurl;

    @Column(name = "PAYMENT_TYPE")
    @Filter
    @Searchable
    private String paymentType;

    @Column(name = "OWNER")
    @Filter
    @Searchable
    private String owner;

    @Column(name = "CUSTOMER_EMAIL")
    @Filter
    @Searchable
    private String customerEmail;

    @Column(name = "CUSTOMER_MOBILE")
    @Filter
    @Searchable
    private String customerMobile;

    @Column(name = "SERVICE_NAME")
    @Filter
    @Searchable
    private String serviceName;

    @Column(name = "PAYMENT_GFS_CODE")
    @Filter
    @Searchable
    private String paymentGfsCode;

    @Column(name = "CURRENCY")
    @Filter
    @Searchable
    private String currency;

    @Column(name = "PAYMENT_DESC")
    @Filter
    @Searchable
    private String paymentDesc;

    @Column(name = "PAYMENT_EXPIRY")
    @Filter
    @Searchable
    private String paymentExpiry;

    @Column(name = "PAYMENT_OPTION")
    @Filter
    @Searchable
    private String paymentOption;

    @Column(name = "AMOUNT")
    private String amount;

    @Column(name = "MESSAGE")
    private String message = "";

    @Column(name = "REQUEST_DIRECTION")
    @Filter
    @Searchable
    private String requestDirection;

    @Column(name = "GEPG_RECEIPT")
    @Filter
    @Searchable
    private String gepgReceipt;

    @Column(name = "TID")
    @Filter
    @Searchable
    String tid;
    @Column(name = "MID")
    @Filter
    @Searchable
    String mid;
    @Column(name = "POS_REF")
    @Filter
    @Searchable
    String posref;
    //SWITCH_AUTH_CODE
    @Column(name = "SWITCH_AUTH_CODE")
    @Filter
    @Searchable
    String switchAuthCode;

    @Column(name = "INSERTTIME")
    @Filter(isDateRange = true)
    Date insertTime;

    @Column(name = "STATUS")
    @Filter
    @Searchable
    String status;

    @Column(name = "STATUSDESC")
    @Filter
    @Searchable
    String statusDesc;

    @Column(name = "RETRIESCOUNT")
    Integer retriesCount;
}
