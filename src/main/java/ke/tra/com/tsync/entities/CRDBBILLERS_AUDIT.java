package ke.tra.com.tsync.entities;

import ke.tra.com.tsync.wrappers.crdb.GepgControlNumberRequest;
import ke.tra.com.tsync.wrappers.crdb.GetControlNumberDetailsResponse;
import ke.tra.com.tsync.wrappers.crdb.PostGePGControlNumberPaymentRequest;
import ke.tra.com.tsync.wrappers.crdb.PostGepgControlNumberResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Mwagiru Kamoni
 */

/*
enum RequestType {
    GetControlNumber,
    GetControlNumberResponse,
    PostControlNumber,
    PostControlNumberResponse
}
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "CRDBBILLERS_GEPG_LOGS")
public class CRDBBILLERS_AUDIT implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CRDBBILLERS_GEPG_LOGS_SEQ")
    @SequenceGenerator(sequenceName = "crdbbillers_gepg_logs_seq", allocationSize = 1, name = "CRDBBILLERS_GEPG_LOGS_SEQ")
    @Column(name = "ID")
    private BigDecimal crdb_billers_id;

    @Column(name = "REQUEST_NAME")
    private String requestName; // GETCONTROL, GETCONTROLRESPONSE

    @Column(name = "CODE")
    private String code;
    //private String sessionToken;//not needed
    //private String partnerID;
    // private String checksum;//not needed

    @Column(name = "REQUEST_ID")
    private String requestID;

    @Column(name = "PAYMENT_REFERENCE")
    private String paymentReference;

    @Column(name = "CALLBACK_URL")
    private String callbackurl;

    @Column(name = "PAYMENT_TYPE")
    private String paymentType;

    @Column(name = "OWNER")
    private String owner;

    @Column(name = "CUSTOMER_EMAIL")
    private String customerEmail;

    @Column(name = "CUSTOMER_MOBILE")
    private String customerMobile;

    @Column(name = "SERVICE_NAME")
    private String serviceName;

    @Column(name = "PAYMENT_GFS_CODE")
    private String paymentGfsCode;

    @Column(name = "CURRENCY")
    private String currency;

    @Column(name = "PAYMENT_DESC")
    private String paymentDesc;

    @Column(name = "PAYMENT_EXPIRY")
    private String paymentExpiry;

    @Column(name = "PAYMENT_OPTION")
    private String paymentOption;

    @Column(name = "AMOUNT")
    private String amount;

    @Column(name = "MESSAGE")
    private String message="";

    @Column(name = "REQUEST_DIRECTION")
    private String requestDirection;

    @Column(name = "GEPG_RECEIPT")
    private String gepgReceipt;

    @Column(name = "TID")
    String tid;
    @Column(name = "MID")
    String mid;
    @Column(name = "POS_REF")
    String posref;

    public CRDBBILLERS_AUDIT(
            GepgControlNumberRequest gepgControlNumberRequest,
            String tid,String mid, String posref) {
        this.tid=tid;
        this.mid=mid;
        this.posref=posref;
        requestName = "GETCONTROL";
        code = gepgControlNumberRequest.code(); // IF WE HAVE A REQUEST WITHOUT A CODE
        requestID = gepgControlNumberRequest.requestID();
        paymentReference = gepgControlNumberRequest.paymentReference();
        requestDirection = "REQUEST";
    }


    public CRDBBILLERS_AUDIT(GetControlNumberDetailsResponse getControlNumberDetailsResponse
    ,String tid,String mid, String posref) {
        this.tid=tid;
        this.mid=mid;
        this.posref=posref;
        amount = getControlNumberDetailsResponse.amount();
        requestName = "GETCONTROL";
        code="INQUIRE"; //  MOVE THIS TO INPUT TO ALLOW FOR MULTIPLE CODES
        requestID = getControlNumberDetailsResponse.requestID();
        paymentReference = getControlNumberDetailsResponse.paymentReference();
        requestDirection = "RESPONSE";
        owner = getControlNumberDetailsResponse.owner();
        customerEmail = getControlNumberDetailsResponse.customerEmail();
        customerMobile = getControlNumberDetailsResponse.customerMobile();
        serviceName=getControlNumberDetailsResponse.serviceName();
        paymentGfsCode = getControlNumberDetailsResponse.paymentGfsCode();
        currency = getControlNumberDetailsResponse.currency();
        message=getControlNumberDetailsResponse.message();
    }


    public CRDBBILLERS_AUDIT(PostGePGControlNumberPaymentRequest postGePGControlNumberPaymentRequest,
                             String tid,String mid, String posref) {
        this.tid=tid;
        this.mid=mid;
        this.posref=posref;
        requestName = "POSTCONTROL";
        code = postGePGControlNumberPaymentRequest.code(); // IF WE HAVE A REQUEST WITHOUT A CODE
        requestID = postGePGControlNumberPaymentRequest.requestID();
        paymentReference = postGePGControlNumberPaymentRequest.paymentReference();
        requestDirection = "REQUEST";
        //callbackurl=postGePGControlNumberPaymentRequest.callbackurl();
        paymentType = postGePGControlNumberPaymentRequest.paymentType();
        owner = postGePGControlNumberPaymentRequest.owner();
        customerEmail = postGePGControlNumberPaymentRequest.customerEmail();
        customerMobile = postGePGControlNumberPaymentRequest.customerMobile();
        serviceName = postGePGControlNumberPaymentRequest.serviceName();
        customerMobile = postGePGControlNumberPaymentRequest.customerMobile();
        paymentGfsCode = postGePGControlNumberPaymentRequest.paymentGfsCode();
        currency = postGePGControlNumberPaymentRequest.currency();
        paymentDesc = postGePGControlNumberPaymentRequest.paymentDesc();
        paymentExpiry = postGePGControlNumberPaymentRequest.paymentExpiry();
        paymentOption = postGePGControlNumberPaymentRequest.paymentOption();
        amount = postGePGControlNumberPaymentRequest.amount();
    }
//CREATE A FACTORY TO INITIALIZE ALL THESE CLASS INITIALIZERS

    public CRDBBILLERS_AUDIT(PostGepgControlNumberResponse postGepgControlNumberResponse,
                             String tid,String mid, String posref) {
        //partnerID not being saved
        requestName = "POSTCONTROL";
        code="PURCHASE"; // MOVE THIS TO INPUT TO ALLOW FOR MULTIPLE CODES
        requestID = postGepgControlNumberResponse.requestID();
        paymentReference = postGepgControlNumberResponse.paymentReference();
        requestDirection = "RESPONSE";
        owner = postGepgControlNumberResponse.owner();
        amount = postGepgControlNumberResponse.amount();
        
        gepgReceipt = postGepgControlNumberResponse.gepgReceipt();
        this.tid=tid;
        this.mid=mid;
        this.posref=posref;

    }


}
