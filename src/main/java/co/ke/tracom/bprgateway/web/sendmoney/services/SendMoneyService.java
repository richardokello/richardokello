package co.ke.tracom.bprgateway.web.sendmoney.services;

import co.ke.tracom.bprgateway.web.agenttransactions.dto.response.AuthenticateAgentResponse;
import co.ke.tracom.bprgateway.web.agenttransactions.services.AgentTransactionService;
import co.ke.tracom.bprgateway.web.bankbranches.entity.BPRBranches;
import co.ke.tracom.bprgateway.web.bankbranches.service.BPRBranchService;
import co.ke.tracom.bprgateway.web.sendmoney.data.requests.SendMoneyRequest;
import co.ke.tracom.bprgateway.web.sendmoney.data.response.SendMoneyResponse;
import co.ke.tracom.bprgateway.web.sendmoney.data.response.SendMoneyResponseData;
import co.ke.tracom.bprgateway.web.sendmoney.entity.MoneySend;
import co.ke.tracom.bprgateway.web.sendmoney.repository.MoneySendRepository;
import co.ke.tracom.bprgateway.web.switchparameters.repository.XSwitchParameterRepository;
import co.ke.tracom.bprgateway.web.t24communication.services.T24Channel;
import co.ke.tracom.bprgateway.web.transactions.entities.T24TXNQueue;
import co.ke.tracom.bprgateway.web.transactions.services.TransactionService;
import co.ke.tracom.bprgateway.web.util.services.BaseServiceProcessor;
import co.ke.tracom.bprgateway.web.util.services.UtilityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

import static co.ke.tracom.bprgateway.web.t24communication.services.T24Channel.MASKED_T24_PASSWORD;
import static co.ke.tracom.bprgateway.web.t24communication.services.T24Channel.MASKED_T24_USERNAME;

@Slf4j
@RequiredArgsConstructor
@Service
public class SendMoneyService  {
  private final AgentTransactionService agentTransactionService;
  private final BaseServiceProcessor baseServiceProcessor;
  private final BPRBranchService branchService;
  private final UtilityService utilityService;
  private final T24Channel t24Channel;
  private final TransactionService transactionService;
  private final BPRCreditCardNumberGenerator bprCreditCardNumberGenerator;
  private final DesUtil desUtil;

  private final XSwitchParameterRepository xSwitchParameterRepository;
  private final MoneySendRepository moneySendRepository;

  @Value("${merchant.account.validation}")
  private String agentValidation;


  public SendMoneyResponse processSendMoneyRequest(SendMoneyRequest request, String transactionRRN) {

    Optional<AuthenticateAgentResponse> optionalAuthenticateAgentResponse = baseServiceProcessor.authenticateAgentUsernamePassword(request.getCredentials(), agentValidation);
    if (optionalAuthenticateAgentResponse.isEmpty()) {
      log.info(
              "Send Money transaction :[Failed] Missing agent information %n");
      return SendMoneyResponse.builder()
            .status("117")
            .message("Missing agent information")
            .data(null)
            .build();
    }

    AuthenticateAgentResponse authenticateAgentResponse = optionalAuthenticateAgentResponse.get();

    BPRBranches branch = branchService.fetchBranchAccountsByBranchCode(authenticateAgentResponse.getData().getAccountNumber());
    if (null == branch.getCompanyName()) {
      return SendMoneyResponse.builder()
              .status("065")
              .message("Missing agent branch details")
              .data(null)
              .build();
    }

    String branchAccountID = branch.getId();
    if (branchAccountID.isEmpty()) {
      return SendMoneyResponse.builder()
              .status("065")
              .message("Missing agent branch account id")
              .data(null)
              .build();
    }

    long agentFloatAccountBalance =
            agentTransactionService.fetchAgentAccountBalanceOnly( authenticateAgentResponse.getData().getAccountNumber() );

    try {

      /**
       * "Debit Agent float Acc - Credit Agent Suspense Acc, Dr Agent - Cr Agent Fees, Debit Agent
       * Fees - Credit Agent Tax Acc"
       */
      String charges = fetchSendMoneyTransactionCharges(authenticateAgentResponse.getData().getAccountNumber(),branchAccountID);
      long chargesLong = Long.parseLong(charges);

      System.out.printf(
              "%n Transaction %s T24 Charges (%s) and agent float account balance (%d) against send money amount (%f) %n",
              transactionRRN, charges, agentFloatAccountBalance, request.getAmount());

      if (request.getAmount() > (agentFloatAccountBalance + chargesLong)) {
        return SendMoneyResponse.builder()
                .status("117")
                .message("Insufficient agent float balance")
                .data(null)
                .build();
      }



      String senderMobileNo = request.getSenderMobileNo().trim();
      if (senderMobileNo.length() != 10) {
        return SendMoneyResponse.builder()
                .status("145")
                .message("Missing sender mobile no")
                .data(null)
                .build();
      }

      String receiverMobile = request.getRecipientMobileNo().trim();
      if (receiverMobile.equalsIgnoreCase(senderMobileNo)) {
        return SendMoneyResponse.builder()
                .status("144")
                .message("Invalid or missing recipient mobile no")
                .data(null)
                .build();
      }

      String configuredSendMoneySuspenseAccount = xSwitchParameterRepository.findByParamName("SENDMONEYSUSPENSE").get().getParamValue();


      String firstTransactionPaymentDetails = senderMobileNo + " / " + receiverMobile;
      String secondTransactionPaymentDetails = authenticateAgentResponse.getData().getNames() + " " + authenticateAgentResponse.getData().getAccountNumber() ;
      String thirdTransactionPaymentDetails = "SEND MONEY";

      String cardlesssendOFS =
              "0000AFUNDS.TRANSFER,SEND.MONEY/I//1/0,"
                      + ""
                      + MASKED_T24_USERNAME
                      + "/"
                      + MASKED_T24_PASSWORD
                      + "/"
                      + branchAccountID
                      + ",,"
                      + "TRANSACTION.TYPE::=ACMO,"
                      + "DEBIT.ACCT.NO::="
                      + authenticateAgentResponse.getData().getAccountNumber()
                      + ","
                      + "DEBIT.AMOUNT::="
                      + request.getAmount()
                      + ","
                      + "CREDIT.ACCT.NO::="
                      + configuredSendMoneySuspenseAccount
                      + ","
                      + "DEBIT.CURRENCY::=RWF,"
                      + "TCM.REF::="
                      + transactionRRN
                      + ","
                      + "PAYMENT.DETAILS:1:= "
                      +utilityService. sanitizePaymentDetails(firstTransactionPaymentDetails, "Send Money").trim()
                      + ","
                      + "PAYMENT.DETAILS:2:="
                      + secondTransactionPaymentDetails.trim()
                      + ","
                      + "PAYMENT.DETAILS:3:="
                      + thirdTransactionPaymentDetails.trim();

      System.out.println("send money OFS  " + cardlesssendOFS);
      String tot24str = String.format("%04d", cardlesssendOFS.length()) + cardlesssendOFS;

      //TODO CHECK IF TIN CAN SEND ID Type
//      String nationalIdDocumentName = senderrecepient[3].equals("0") ? "NID" : "OTHERS";
      String nationalIdDocumentName =  "NID"  ;

      System.out.println("transactionReferenceNo : " + transactionRRN);
      System.out.println("sendMoneyAmount : " + request.getAmount());
      System.out.println("receiverMobile : " + receiverMobile);
      System.out.println("senderMobileNo : " + senderMobileNo);

      //TODO find TID
      String tid = "PC";
      System.out.println("tid : " + tid);

      String t24ref = "";

      T24TXNQueue tot24 = new T24TXNQueue();
      tot24.setTid(tid);
      tot24.setRequestleg(tot24str);
      tot24.setStarttime(System.currentTimeMillis());

      String channel = "1510";
      tot24.setTxnchannel(channel);
      tot24.setGatewayref(transactionRRN);
      tot24.setPostedstatus("1");
      //TODO Define P Codes
      tot24.setProcode("410000");
      tot24.setDebitacctno(authenticateAgentResponse.getData().getAccountNumber());
      tot24.setCreditacctno(configuredSendMoneySuspenseAccount);


      final String t24Ip = xSwitchParameterRepository.findByParamName("T24_IP").get().getParamValue();
      final String t24Port = xSwitchParameterRepository.findByParamName("T24_PORT").get().getParamValue();

      t24Channel.processTransactionToT24(t24Ip, Integer.parseInt(t24Port), tot24);
      transactionService.updateT24TransactionDTO(tot24);

      t24ref = tot24.getT24reference() == null ? "NA" : tot24.getT24reference();
      if ((tot24.getT24responsecode().equalsIgnoreCase("1"))) {

        SendMoneyResponseData data = SendMoneyResponseData.builder().build();
        try {
          charges = tot24.getTotalchargeamt();
          System.out.println("Charges " + charges);
          String formattedcharge = charges.replace("RWF", "");
          System.out.println("Transaction charge : " + formattedcharge);
          //send money commission save

          String collectionCommissionAccount = xSwitchParameterRepository.findByParamName("SENDMONEYCOMMISSIONCOLLECTIONACC").get().getParamValue();
          this.saveSendMoneyCommission(formattedcharge, tot24, tid, transactionReferenceNo, agentFloatAccount,
                  firstTransactionPaymentDetails, secondTransactionPaymentDetails,
                  thirdTransactionPaymentDetails, branchAccountID, db, collectionCommissionAccount);

          String isoamount = String.format("%012d", Integer.parseInt(formattedcharge));
          data.setCharges(Double.parseDouble( isoamount));

        } catch (Exception e) {
          System.out.println(
                  "unable to get charges for send money txn ref " + transactionRRN);
          e.printStackTrace();
        }
        data.setT24Reference(t24ref);
        try {

          String vbin = xSwitchParameterRepository.findByParamName("CARDLESSTXNBIN").get().getParamValue();
          String CARDLESSTXNBIN = "123456";
          String VIRTUALCARDBIN = vbin.equals("") ? CARDLESSTXNBIN : vbin;
          String cno = bprCreditCardNumberGenerator.generate(VIRTUALCARDBIN, 12);
          String cnoenc = desUtil.encryptPlainText(cno);

          if (cnoenc != null || !cnoenc.isEmpty()) {
            Random generator = new Random();
            String token = String.format("%06d", 100000 + generator.nextInt(899999));

            MoneySend ms = new MoneySend();
            ms.setChannel("POS");
            ms.setAmount(String.valueOf(sendMoneyAmount));
            ms.setSendernumber(senderMobileNo);
            ms.setRecevernumber(receiverMobile);
            ms.setAgentid(agentFloatAccount);
            ms.setMstoken(token);
            ms.setCno(cnoenc);
            ms.setFulfilmentstatus(0);

            String senderNationalID = senderrecepient[2];
            ms.setSendernationalid(senderNationalID);
            ms.setTypeofid(nationalIdDocumentName);
            ms.setSendmoneylegt24ref(t24ref);

            moneySendRepository.save(ms);

            String recipientSMSMessage =
                    "You have received RWF "
                            + request.getAmount()
                            + " from "
                            + senderMobileNo
                            + " to "
                            + "withdraw at a BPR agent. Provide VCARD no: "
                            + cno
                            + ", "
                            + "Sender will share the passcode. MURAKOZE";

            String SMScontent = recipientSMSMessage;
            int ti = 0;
            while (SMScontent.length() > 0) {

              SmstransScheduled scheduledSMSTransaction = new SmstransScheduled();
              scheduledSMSTransaction.setSentstatus(0);
              scheduledSMSTransaction.setAttempts(0);
              scheduledSMSTransaction.setReceiverphone(receiverMobile);
              scheduledSMSTransaction.setTxnref(transactionReferenceNo);

              if (SMScontent.length() <= 160) {
                scheduledSMSTransaction.setMessage(BPRFunctions.base64encode(SMScontent));
                SMScontent = "";
              } else {
                scheduledSMSTransaction.setMessage(
                        BPRFunctions.base64encode(SMScontent.substring(0, 160)));
                SMScontent = SMScontent.substring(160);
              }
              BPRSms.scheduleSMS(scheduledSMSTransaction);
              ++ti;
            }

            String smssender1 =
                    "You have successfully sent RWF"
                            + request.getAmount()
                            + " to "
                            + receiverMobile
                            + " "
                            + "via BPR Cardless transfer. Kindly share passcode with the recipient."
                            + "passcode : "
                            + token
                            + ". Thanks for banking with us.";

            // Insert Second SMS
            String smssenderenc = BPRFunctions.base64encode(smssender1);
            SmstransScheduled smssendersched = new SmstransScheduled();
            smssendersched.setSentstatus(0);
            smssendersched.setAttempts(0);
            smssendersched.setMessage(smssenderenc);
            smssendersched.setReceiverphone(senderMobileNo);
            smssendersched.setTxnref(transactionReferenceNo);
            BPRSms.scheduleSMS(smssendersched);

          } else {
            System.out.println(
                    "Card less vcard generation error for rrn " + transactionReferenceNo);
            isomsg.set(39, "118");
          }
        } catch (Exception e) {
          isomsg.set(39, "118");
          e.printStackTrace();
        }
      } else {
        isomsg.set(39, "098");
        isomsg.set(
                60,
                "Transaction posting to cbs failed. reason :: " + tot24.getT24failnarration());
      }
      tot24.setT24reference(t24ref);

      SwitchFN.saveCardLessTransactionToAllTransactionTable(tot24, isomsg, "SEND MONEY");
    } catch (Exception w) {
      System.out.println(
              " Transaction processing  to T24 failed for ref ... " + isomsg.getString(37));
      isomsg.set(39, "116");
      isomsg.set(60, "");
      w.printStackTrace();
    }

    return isomsg;
  }
}
