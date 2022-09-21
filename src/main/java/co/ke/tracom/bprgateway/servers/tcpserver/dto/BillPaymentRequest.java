package co.ke.tracom.bprgateway.servers.tcpserver.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class BillPaymentRequest {
  @JsonProperty("bill")
  private String bill;

  @JsonProperty("billSubCategory")
  private String billSubCategory;

  @JsonProperty("tnxType")
  private String tnxType;

  @JsonProperty("data")
  private List<TransactionData> data = null;
/*
  @JsonProperty("credentials")
  private Credentials credentials;*/

//  private PaymentDetails payment;

  private String debitAccount;
  private String creditAccount;
  private String senderName;
  private String mobileNumber;
  private long amount;
  private int schoolId;

  private String schoolName;
  private String svcCode;
  private String studentName;
  private String billNumber;
  private String source;
  private String field;
  private String value;
  private Credentials credentials;



}
