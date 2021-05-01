package co.ke.tracom.bprgateway.web.exceptions.custom;

public class UnprocessableEntityException extends RuntimeException {
  public UnprocessableEntityException(String message) {
    super(message);
  }
}
