package co.ke.tracom.bprgateway.web.ltss.service;

import co.ke.tracom.bprgateway.core.config.CustomObjectMapper;
import co.ke.tracom.bprgateway.core.tracomchannels.json.RestHTTPService;
import co.ke.tracom.bprgateway.core.util.RRNGenerator;
import co.ke.tracom.bprgateway.web.agenttransactions.dto.response.AuthenticateAgentResponse;
import co.ke.tracom.bprgateway.web.agenttransactions.dto.response.Data;
import co.ke.tracom.bprgateway.web.exceptions.custom.ExternalHTTPRequestException;
import co.ke.tracom.bprgateway.web.ltss.data.LTSSRequest;
import co.ke.tracom.bprgateway.web.ltss.data.checkPayment.CheckPaymentRequest;
import co.ke.tracom.bprgateway.web.ltss.data.checkPayment.CheckPaymentResponse;
import co.ke.tracom.bprgateway.web.ltss.data.exception.StatusCode;
import co.ke.tracom.bprgateway.web.ltss.data.nationalIDValidation.NationalIDValidationRequest;
import co.ke.tracom.bprgateway.web.ltss.data.nationalIDValidation.NationalIDValidationResponse;
import co.ke.tracom.bprgateway.web.ltss.data.newSubscriber.NewSubscriberRequest;
import co.ke.tracom.bprgateway.web.ltss.data.newSubscriber.NewSubscriberResponse;
import co.ke.tracom.bprgateway.web.ltss.data.paymentContribution.LTSSPaymentResponse;
import co.ke.tracom.bprgateway.web.ltss.data.paymentContribution.PaymentContributionResponse;
import co.ke.tracom.bprgateway.web.switchparameters.XSwitchParameterService;
import co.ke.tracom.bprgateway.web.t24communication.services.T24Channel;
import co.ke.tracom.bprgateway.web.transactions.entities.T24TXNQueue;
import co.ke.tracom.bprgateway.web.transactions.services.TransactionService;
import co.ke.tracom.bprgateway.web.util.data.MerchantAuthInfo;
import co.ke.tracom.bprgateway.web.util.services.BaseServiceProcessor;
import co.ke.tracom.bprgateway.web.util.services.UtilityService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Formatter;

@Service
@Slf4j
@RequiredArgsConstructor
public class LtssService {

  private final CustomObjectMapper mapper = new CustomObjectMapper();
private static final String ltlssServiceResponse="LTSS SERVICE RESPONSE: {}";

  private final RestHTTPService restHttpService;
  private final BaseServiceProcessor baseServiceProcessor;
  private final TransactionService transactionService;
  private final XSwitchParameterService xSwitchParameterService;
  private final T24Channel t24Channel;
  private final UtilityService utilityService;

  @Value("${ltss.base.url}")
  private String ltssBaseUrl;

  @Value("${ltss.nationalId.validation.url}")
  private String nationalIdValidationUrl;

  @Value("${ltss.payment.contribution.url}")
  private String paymentContributionUrl;

  @Value("${ltss.payment.check.url}")
  private String checkPaymentByRefNoUrl;

  @Value("${ltss.register.subscriber.url}")
  private String registerNewSubscriberURL;

  /**
   * Validates National ID
   *
   * @param validationRequest validation request object
   * @return validation response
   */
  public NationalIDValidationResponse validateNationalID(
      NationalIDValidationRequest validationRequest) {
    NationalIDValidationResponse validationResponse;

    try {
      String requestURL = ltssBaseUrl + nationalIdValidationUrl;
      //String requestURL = "http://197.243.10.68:8080/ltss-integration-service/pservice/ltssservice/validateSubscriber";
      ResponseEntity<String> response = restHttpService.postLTSSRequest(validationRequest, requestURL);
      log.info(ltlssServiceResponse, response);
      System.out.println("response = " + response);

      validationResponse = mapper.readValue(response.getBody(), NationalIDValidationResponse.class);


    } catch (Exception ex) {
      ex.printStackTrace();
      logError(ex);
      throw new ExternalHTTPRequestException("Error validation National ID from LTSS API");
    }
    return validationResponse;
  }

  /**
   * Sends a payment contribution
   *
   * @param paymentContributionRequest payment request object
   * @return payment contribution response
   */
  public PaymentContributionResponse sendPaymentContribution(
          LTSSRequest paymentContributionRequest) {
    PaymentContributionResponse paymentContributionResponse;

    try {
      String requestURL = ltssBaseUrl + paymentContributionUrl;
      ResponseEntity<String> response =
          restHttpService.postLTSSRequest(paymentContributionRequest, requestURL);
      log.info(ltlssServiceResponse, response);
      paymentContributionResponse =
          mapper.readValue(response.getBody(), PaymentContributionResponse.class);
    } catch (Exception ex) {
      ex.printStackTrace();
      logError(ex);
      throw new ExternalHTTPRequestException("Error sending payment contribution to LTSS API");

    }
    return paymentContributionResponse;



  }

  private String getDefaultBranch() {
    String default_eucl_branch = xSwitchParameterService.fetchXSwitchParamValue("DEFAULT_EUCL_BRANCH");
    if (default_eucl_branch.isEmpty()) {
      return "RW0010461";
    }
    return default_eucl_branch;
  }
  private String getT24Ip() {
    return xSwitchParameterService.fetchXSwitchParamValue("T24_IP");
  }

  private String getT24Port() {
    return xSwitchParameterService.fetchXSwitchParamValue("T24_PORT");
  }
  private String getUsername(){
    String t24USER = xSwitchParameterService.fetchXSwitchParamValue("T24USER");

    if (t24USER.isEmpty()) {
      log.info("****************************** Missing T24 User in the database. ******************************");
      return "TRUSER999";
    }
    return utilityService.decryptSensitiveData(t24USER);
  }
  private String getPassword(){
    String t24PASS = xSwitchParameterService.fetchXSwitchParamValue("T24PASS");

    if (t24PASS.isEmpty()) {
      log.info("****************************** Missing T24 Pass in the database. ******************************");
      return "123456999";
    }
    return utilityService.decryptSensitiveData(t24PASS);
  }
  @SneakyThrows
public LTSSPaymentResponse makeContributionPayment(LTSSRequest ltssRequest)  {

//      System.out.println("ltssRequest = ========" + ltssRequest);
//      System.out.println("ltssRequest = ========" + ltssRequest.getCredentials());
//      System.out.println("ltssRequest = ========" + ltssRequest.getCredentials().getPassword());
//      System.out.println("ltssRequest.getAmount() = " + ltssRequest.getAmount());
      LTSSPaymentResponse paymentResponse=new LTSSPaymentResponse();
   //   System.out.println("paymentResponse = " + paymentResponse);
//    AuthenticateAgentResponse authenticateAgentResponse=baseServiceProcessor.authenticateAgentUsernamePassword(
//            new MerchantAuthInfo(paymentContributionRequest.getCredentials().getUsername(),paymentContributionRequest.getCredentials().getPassword())
//    );

      AuthenticateAgentResponse authenticateAgentResponse = baseServiceProcessor.authenticateAgentUsernamePassword(
              new MerchantAuthInfo(ltssRequest.getCredentials().getUsername(),
                      ltssRequest.getCredentials().getPassword()));
  //  AuthenticateAgentResponse authenticateAgentResponse= new AuthenticateAgentResponse();
//    try{
//      authenticateAgentResponse=baseServiceProcessor.authenticateAgentUsernamePassword(new MerchantAuthInfo(ltssRequest.getCredentials().getUsername(),
//              ltssRequest.getCredentials().getPassword()));
//    }catch (InvalidAgentCredentialsException e){
//     transactionService.saveFailedUserPasswordTransactions("Failed Logins PC module transactions","Agent logins",ltssRequest.getCredentials().getUsername(),
//             "AgentValidation","FAILED","ipAddress");

//    }

      if(authenticateAgentResponse.getCode()!=200)
      {

paymentResponse.setStatus("489");
paymentResponse.setMessage("failed authentication");
      }

      Data agentAuthData = authenticateAgentResponse.getData();

    T24TXNQueue tot24 = new T24TXNQueue();
      NationalIDValidationResponse validationResponse= new NationalIDValidationResponse(); //.builder().build();


      String channel = "AGB";
      //String tid = "PC";
      String identification = ltssRequest.getIdentification();

      String mid= authenticateAgentResponse.getData().getMid();
      String tid=authenticateAgentResponse.getData().getTid();
      Long amount= Long.valueOf(ltssRequest.getAmount());
      String amounts=ltssRequest.getAmount();
      String name = authenticateAgentResponse.getData().getNames();
      String t24UserName = getUsername();
      String t24Password = getPassword();
      String euclBankBranch = getDefaultBranch();

//formattjng to get the month name
    Formatter fmt = new Formatter();
    Calendar cal = Calendar.getInstance();
    fmt = new Formatter();
    fmt.format("%tB ", cal);

 String senderName= validationResponse.getName();
      String debitAcc="403456719410123";
      String curreCode="RWF";
      String payment1="EJOHEZA FOR "+fmt;
      String payment2= tid+mid;
      String payment3="LTSS PAYMENT AT AGENT";

      String bareOFS="0000AFUNDS.TRANSFER,BPR.LTS.PAY.AGB/I/PROCESS"
              +
              ","
              +

              t24UserName+
              "/"
              +t24Password+
              "/"
              +euclBankBranch+
              ",,TRANSACTION.TYPE::=ACLT"
              +
              ","
              +
              "DEBIT.ACCT.NO::="
              +authenticateAgentResponse.getData().getAccountNumber()
              +
              ",CREDIT.CURRENCY::=RWF"
              +
              ",CREDIT.AMOUNT::="
              +amount+
              ",BPR.ID.DOC.NO::="
              +identification+
              ",BPR.SENDER.NAME::= "
              +senderName+
              ",MOBILE.NO::=1231231231"
              +
              ",PAYMENT.DETAILS:1:1="
              +payment1+
              ",PAYMENT.DETAILS:2:1="
              +payment2+
              ",PAYMENT.DETAILS:3:1="
              +payment3+
              ",CHANNEL::=AGB";




String formartedOFS= String.format("%04d", bareOFS.length()) + bareOFS;
      System.out.println("formartedOFS = " + formartedOFS);

 tot24.setRequestleg(formartedOFS);
 tot24.setTxnmti("1200");
 tot24.setProcode("460000");
 tot24.setTxnchannel(channel);
 String t24refRRN = RRNGenerator.getInstance("EH").getRRN();
 tot24.setGatewayref(t24refRRN);
 tot24.setStarttime(System.currentTimeMillis());
 tot24.setTid(tid);
 tot24.setPostedstatus("0");

 tot24.setDebitacctno(authenticateAgentResponse.getData().getAccountNumber());


      final String t24Ip = getT24Ip();
      final String t24Port = getT24Port();

      t24Channel.processTransactionToT24(t24Ip, Integer.parseInt(t24Port), tot24);
     transactionService.updateT24TransactionDTO(tot24);
     // System.out.println(".Gateway ref.. " + t24refRRN + " txn queued for t24 posting !!");


      String errorMessage =
              tot24.getT24failnarration() == null ? "" : tot24.getT24failnarration();
      if(errorMessage.isEmpty()){
//        boolean manual =false;
//        if (tot24.getT24responsecode() == null && tot24.getResponseleg().length() > 150){
//          //extract data  manually
//          manualExtraction(tot24,tot24.getResponseleg());
//          manual =true;
//        }
        if ( tot24.getT24responsecode().equals("1")){
          PaymentContributionResponse ltssResponse=new PaymentContributionResponse();
              ltssResponse.setBeneficiary( validationResponse);
              ltssResponse.setDescription(ltssRequest.getDescription());
              ltssResponse.setAmount(amounts);
              ltssResponse.setPaymentDate(tot24.getDatetime());
              ltssResponse.setExtReferenceNo(t24refRRN );
              ltssResponse.setRefNo(t24refRRN);

              paymentResponse=new LTSSPaymentResponse().builder()
                      .status("00")
                      .message("succesfull payment Transaction")
                      .data(ltssResponse)
                      .build();
        return  paymentResponse;
        }
        else {
          PaymentContributionResponse contributionResponse=new PaymentContributionResponse();

                 contributionResponse.setRefNo(t24refRRN);
                 contributionResponse.setBeneficiary(makeContributionPayment(ltssRequest).getData().getBeneficiary());
         paymentResponse.setStatus("098");
         paymentResponse.setMessage("Transaction Failed");
         paymentResponse.setData(contributionResponse);
                 
          return  paymentResponse;
        }

      } else {
          transactionService.saveCardLessTransactionToAllTransactionTable(tot24, "EJO HEZA", "1200",
                  Double.valueOf(ltssRequest.getAmount()), "135",
                  authenticateAgentResponse.getData().getTid(), authenticateAgentResponse.getData().getMid());
          return LTSSPaymentResponse.builder().build().builder()
                  .status("135")
                  .message(tot24.getT24failnarration().replace("\"", ""))
                  .data(null).build();
      }


    }









//  private void manualExtraction(T24TXNQueue tot24, String responseleg) {
//    String[] split = responseleg.split(",");
//    tot24.setT24reference(split[0].split("/")[0]);
//    String[] token = split[27].split(":");
//    tot24.setTokenNo(token[3].split("-")[0]);
//    tot24.setUnitsKw(token[4]);
//    tot24.setGatewayref(split[15].split("=")[1]);
//  }
  /**
   * checks payment by reference number
   *
   * @param checkPaymentRequest check payment request object
   * @return response entity from remote API; Structure of response is not specified in LTSS service
   *     documentation so an explicit response object to parse the response has not been used
   */
  public CheckPaymentResponse checkPaymentByRefNo(CheckPaymentRequest checkPaymentRequest) {
    ResponseEntity<String> response;
    CheckPaymentResponse checkPaymentResponse = new CheckPaymentResponse();
    try {
      String requestURL = ltssBaseUrl + checkPaymentByRefNoUrl;
      response = restHttpService.postLTSSRequest(checkPaymentRequest, requestURL);
      checkPaymentResponse = mapper.readValue(response.getBody(), CheckPaymentResponse.class);
      log.info(ltlssServiceResponse, response);
    } catch (Exception ex) {
      ex.printStackTrace();
      logError(ex);

      String s = ex.getMessage().split(":\\p{Space}")[1].strip();
      int endIndex = s.length()-1;
      String message = s.substring(1, endIndex);
      StatusCode statusCode = new StatusCode();
      try {
        statusCode = mapper.readValue(message,StatusCode.class);
      } catch (JsonProcessingException e) {
        e.printStackTrace();
      }
      checkPaymentResponse.setMessage(statusCode.getMessage());
      checkPaymentResponse.setStatus(statusCode.getStatus());

//      throw new ExternalHTTPRequestException(
//          "Error checking payment by reference number from LTSS API");
    }
    return checkPaymentResponse;
  }

  /**
   * Registers a new subscriber
   *
   * @param newSubscriberRequest new subscriber request object
   * @return new subscriber response
   */
  public NewSubscriberResponse registerNewSubscriber(NewSubscriberRequest newSubscriberRequest) {
    NewSubscriberResponse newSubscriberResponse = new NewSubscriberResponse();

    try {
      String requestURL = ltssBaseUrl + registerNewSubscriberURL;
      ResponseEntity<String> response =
          restHttpService.postLTSSRequest(newSubscriberRequest, requestURL);
      log.info(ltlssServiceResponse, newSubscriberResponse);
      newSubscriberResponse = mapper.readValue(response.getBody(), NewSubscriberResponse.class);

    } catch (Exception ex) {
      ex.printStackTrace();
      logError(ex);
      System.err.println("((((("+ex+")))))");

      String s = ex.getMessage().split(":\\p{Space}")[1].strip();
      int endIndex = s.length()-1;
      String message = s.substring(1, endIndex);
      StatusCode statusCode = new StatusCode();
      try {
        statusCode = mapper.readValue(message,StatusCode.class);
      } catch (JsonProcessingException e) {
        e.printStackTrace();
      }
      newSubscriberResponse.setMessage(statusCode.getMessage());
      newSubscriberResponse.setStatus(statusCode.getStatus());
      //throw new ExternalHTTPRequestException("Error registering a new subscriber in  LTSS Service");
    }
    return newSubscriberResponse;
  }

  private void logError(Exception ex) {
    log.error("LTSS SERVICE: {}", ex.getMessage());
  }
}
