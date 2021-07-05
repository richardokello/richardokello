package co.ke.tracom.bprgateway.web.util;

public enum TransactionISO8583ProcessingCode {
  SEND_MONEY("410000"),
  WITHDRAW_MONEY("420000"),
  BANK_ACCOUNT_VALDATION("430000"),
  CUSTOMER_DEPOSIT("440000"),
  RRA_PAYMENT("460000"),
  EUCL_BILL("460001"),
  IREMBO("470000"),
  AGENT_FLOAT_DEPOSIT("480000"),
  AGENT_FLOAT_WITHDRAWAL("490000"),
  AGENT_FLOAT_BALANCE_INQUIRY("500000"),
  AGENT_ACCOUNT_VALIDATION("510000"),
  NATIONAL_ID_VALIDATION("520000"),
  IZI_CASH_WITHDRAWAL("530000"),
  GENERAL_RESPONSE("045");

  private String code;

  TransactionISO8583ProcessingCode(String responseCode) {
    this.code = responseCode;
  }

  public String getCode() {
    return code;
  }
}
