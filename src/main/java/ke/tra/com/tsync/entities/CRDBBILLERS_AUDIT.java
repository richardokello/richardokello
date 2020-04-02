package ke.tra.com.tsync.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
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
 *
 * @author Mwagiru Kamoni
 */

enum RequestType {
    GetControlNumber,
    GetControlNumberResponse,
    PostControlNumber,
    PostControlNumberResponse
}


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "CRDBBILLERS_GEPG_LOGS")
public class CRDBBILLERS_AUDIT implements Serializable {
    /**
     * "ID" NUMBER NOT NULL ENABLE,
     * 	"REQUEST_NAME" VARCHAR2(20 BYTE),
     * 	"CODE" VARCHAR2(15 BYTE) NOT NULL ENABLE,
     * 	"REQUEST_ID" VARCHAR2(20 BYTE),
     * 	"PAYMENT_REFERENCE" VARCHAR2(20 BYTE),
     * 	"PAYMENT_TYPE" VARCHAR2(15 BYTE),
     * 	"OWNER" VARCHAR2(80 BYTE),
     * 	"CUSTOMER_EMAIL" VARCHAR2(100 BYTE),
     * 	"CUSTOMER_MOBILE" VARCHAR2(15 BYTE),
     * 	"SERVICE_NAME" VARCHAR2(100 BYTE),
     * 	"PAYMENT_GFS_CODE" VARCHAR2(20 BYTE),
     * 	"CURRENCY" VARCHAR2(10 BYTE),
     * 	"PAYMENT_DESC" VARCHAR2(100 BYTE),
     * 	"PAYMENT_EXPIRY" VARCHAR2(20 BYTE),
     * 	"PAYMENT_OPTION" VARCHAR2(10 BYTE),
     * 	"AMOUNT" VARCHAR2(20 BYTE),
     * 	"MESSAGE" VARCHAR2(400 BYTE),
     * 	"INSERTTIME" TIMESTAMP (6) DEFAULT current_timestamp(2) NOT NULL ENABLE,
     * 	"REQUEST_DIRECTION" VARCHAR2(20 B
     */
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

    @Column(name = "PAYENT_EXPIRY")
    private String paymentExpiry;

    @Column(name = "PAYMENT_OPTION")
    private String paymentOption;

    @Column(name = "AMOUNT")
    private String amount;

    @Column(name = "MESSAGE")
    private String message;


    @Column(name="REQUEST_DIRECTION")
    private  String requestDirection;
    CRDBBILLERS_AUDIT(
            GepgControlNumberRequest gepgControlNumberRequest,
            GetControlNumberDetailsResponse getControlNumberDetailsResponse){

    }
    CRDBBILLERS_AUDIT(PostGePGControlNumberPaymentRequest postGePGControlNumberPaymentRequest,
                      PostGepgControlNumberResponse postGepgControlNumberResponse){
    }


}
