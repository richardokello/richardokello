package co.ke.tracom.bprgateway.web.exceptions.custom;

public class InvalidMobileNumberException extends RuntimeException {

  public InvalidMobileNumberException(String message) {
    super(message);
  }
}
