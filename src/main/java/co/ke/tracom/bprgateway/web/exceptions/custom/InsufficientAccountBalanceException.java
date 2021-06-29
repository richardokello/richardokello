package co.ke.tracom.bprgateway.web.exceptions.custom;

public class InsufficientAccountBalanceException extends RuntimeException {

  public InsufficientAccountBalanceException(String message) {
    super(message);
  }
}
