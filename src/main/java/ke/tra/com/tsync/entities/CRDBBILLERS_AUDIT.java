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
    private String amount; // in future sput amount to original Amount and fromPOSamount

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

    @Column(name="SWITCH_AUTH_CODE")
    String switchAgentCode;

    @Column(name="STATUS")
    String status;

    @Column(name="STATUSDESC")
    String statusDesc;

    @Column(name="RETRIESCOUNT")
    Integer retriesCount;


    @Column(name="HTTPCODE")
    Integer httpcode;

    @Column(name="REQUESTSENDDESCRPTION")
    String requestSendDescription;

    @Column(name="POSSTAN")
    String posStan;

    @Column(name="ISORGINALREQUEST")
    Integer isOrginalRequest;


    public CRDBBILLERS_AUDIT(
            GepgControlNumberRequest gepgControlNumberRequest,
            String tid,String mid,
            String posref,Integer retriesCount,
    Integer httpcode,String requestSendDescription
    ) {
        this.tid=tid;
        this.mid=mid;
        this.posref=posref;
        this.requestName = "GETCONTROL";
        this.code = gepgControlNumberRequest.code(); // IF WE HAVE A REQUEST WITHOUT A CODE
        this.requestID = gepgControlNumberRequest.requestID();
        this.paymentReference = gepgControlNumberRequest.paymentReference();
        this.requestDirection = "REQUEST";
        this.retriesCount=retriesCount;
        this.httpcode = httpcode;
        this.requestSendDescription=requestSendDescription;
    }


    public CRDBBILLERS_AUDIT(GetControlNumberDetailsResponse getControlNumberDetailsResponse
    ,String tid,String mid, String posref, String status,String statusDesc) {
        this.tid=tid;
        this.mid=mid;
        this.posref=posref;
        this.amount = getControlNumberDetailsResponse.amount();
        this.requestName = "GETCONTROL";
        this.code="INQUIRE"; //  MOVE THIS TO INPUT TO ALLOW FOR MULTIPLE CODES
        this.requestID = getControlNumberDetailsResponse.requestID();
        this.paymentReference = getControlNumberDetailsResponse.paymentReference();
        this.requestDirection = "RESPONSE";
        this.owner = getControlNumberDetailsResponse.owner();
        this. customerEmail = getControlNumberDetailsResponse.customerEmail();
        this.customerMobile = getControlNumberDetailsResponse.customerMobile();
        this.serviceName=getControlNumberDetailsResponse.serviceName();
        this.paymentGfsCode = getControlNumberDetailsResponse.paymentGfsCode();
        this.currency = getControlNumberDetailsResponse.currency();
        this. message=getControlNumberDetailsResponse.message();
        this.statusDesc=statusDesc;
        this.status=status;
    }


    public CRDBBILLERS_AUDIT(PostGePGControlNumberPaymentRequest postGePGControlNumberPaymentRequest,
                             String tid,String mid, String posref,
                             String switchauthcode,
                             Integer retriesCount,
                             Integer httpcode,
                             String httpCodeDesc
    ) {


        this.tid=tid;
        this.mid=mid;
        this.posref=posref;
        this.requestName = "POSTCONTROL";
        this.code = postGePGControlNumberPaymentRequest.code(); // IF WE HAVE A REQUEST WITHOUT A CODE
        this.requestID = postGePGControlNumberPaymentRequest.requestID();
        this.paymentReference = postGePGControlNumberPaymentRequest.paymentReference();
        this.requestDirection = "REQUEST";
        //callbackurl=postGePGControlNumberPaymentRequest.callbackurl();
        this.paymentType = postGePGControlNumberPaymentRequest.paymentType();
        this.owner = postGePGControlNumberPaymentRequest.owner();
        this.customerEmail = postGePGControlNumberPaymentRequest.customerEmail();
        this.customerMobile = postGePGControlNumberPaymentRequest.customerMobile();
        this.serviceName = postGePGControlNumberPaymentRequest.serviceName();
        this.customerMobile = postGePGControlNumberPaymentRequest.customerMobile();
        this.paymentGfsCode = postGePGControlNumberPaymentRequest.paymentGfsCode();
        this.currency = postGePGControlNumberPaymentRequest.currency();
        this.paymentDesc = postGePGControlNumberPaymentRequest.paymentDesc();
        this.paymentExpiry = postGePGControlNumberPaymentRequest.paymentExpiry();
        this.paymentOption = postGePGControlNumberPaymentRequest.paymentOption();
        this.amount = postGePGControlNumberPaymentRequest.amount();
        this.switchAgentCode=switchauthcode;
        this.retriesCount=retriesCount;
        this.httpcode = httpcode;
        this.requestSendDescription=httpCodeDesc;
    }
//CREATE A FACTORY TO INITIALIZE ALL THESE CLASS INITIALIZERS

    public CRDBBILLERS_AUDIT(PostGepgControlNumberResponse postGepgControlNumberResponse,
                             String tid,String mid, String posref,String status,String statusDesc) {
        //partnerID not being saved
        this.requestName = "POSTCONTROL";
        this.code="PURCHASE"; // MOVE THIS TO INPUT TO ALLOW FOR MULTIPLE CODES
        this.requestID = postGepgControlNumberResponse.requestID();
        this.paymentReference = postGepgControlNumberResponse.paymentReference();
        this.requestDirection = "RESPONSE";
        this.owner = postGepgControlNumberResponse.owner();
        this.amount = postGepgControlNumberResponse.amount();
        this.gepgReceipt = postGepgControlNumberResponse.gepgReceipt();
        this.tid=tid;
        this.mid=mid;
        this.posref=posref;
        this.statusDesc=statusDesc;
        this.status=status;
    }

    public CRDBBILLERS_AUDIT(
            PostGePGControlNumberPaymentRequest postGePGControlNumberPaymentRequest,
            String tid, String mid, String posref, String switchauthcode,
            Integer retriesCount, Integer httpcode,
            String requestSendDesc, Integer isOriginalRequest) {

        this.tid=tid;
        this.mid=mid;
        this.posref=posref;
        this.requestName = "POSTCONTROL";
        this.code = postGePGControlNumberPaymentRequest.code(); // IF WE HAVE A REQUEST WITHOUT A CODE
        this.requestID = postGePGControlNumberPaymentRequest.requestID();
        this.paymentReference = postGePGControlNumberPaymentRequest.paymentReference();
        this.requestDirection = "REQUEST";
        //callbackurl=postGePGControlNumberPaymentRequest.callbackurl();
        this.paymentType = postGePGControlNumberPaymentRequest.paymentType();
        this.owner = postGePGControlNumberPaymentRequest.owner();
        this.customerEmail = postGePGControlNumberPaymentRequest.customerEmail();
        this.customerMobile = postGePGControlNumberPaymentRequest.customerMobile();
        this.serviceName = postGePGControlNumberPaymentRequest.serviceName();
        this.customerMobile = postGePGControlNumberPaymentRequest.customerMobile();
        this.paymentGfsCode = postGePGControlNumberPaymentRequest.paymentGfsCode();
        this.currency = postGePGControlNumberPaymentRequest.currency();
        this.paymentDesc = postGePGControlNumberPaymentRequest.paymentDesc();
        this.paymentExpiry = postGePGControlNumberPaymentRequest.paymentExpiry();
        this.paymentOption = postGePGControlNumberPaymentRequest.paymentOption();
        this.amount = postGePGControlNumberPaymentRequest.amount();
        this.switchAgentCode=switchauthcode;
        this.retriesCount=retriesCount;
        this.httpcode = httpcode;
        this.requestSendDescription=requestSendDesc;
        this.isOrginalRequest=isOriginalRequest;

    }
}
