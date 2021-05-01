package co.ke.tracom.bprgateway.web.exceptions.custom;

public class ExternalHTTPRequestException extends RuntimeException {

  public ExternalHTTPRequestException(String message) {
    super(message);
  }
}
