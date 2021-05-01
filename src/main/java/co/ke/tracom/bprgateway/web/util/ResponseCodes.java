package co.ke.tracom.bprgateway.web.util;

public enum ResponseCodes {
  PROCESSING_SUCCESS("000"),
  GENERAL_RESPONSE("045"),
  AMOUNT_OFF_LIMITS("061");

  private String responseCode;

  ResponseCodes(String responseCode) {
    this.responseCode = responseCode;
  }

  public String getResponseCode() {
    return responseCode;
  }
}
