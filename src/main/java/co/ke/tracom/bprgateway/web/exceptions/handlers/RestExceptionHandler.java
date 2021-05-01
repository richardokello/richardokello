package co.ke.tracom.bprgateway.web.exceptions.handlers;

import co.ke.tracom.bprgateway.web.exceptions.custom.ExternalHTTPRequestException;
import co.ke.tracom.bprgateway.web.exceptions.models.ErrorDetail;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/** This is the driving class when an exception occurs. All exceptions are handled here. */
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

  public RestExceptionHandler() {
    super();
  }

  @Override
  protected ResponseEntity<Object> handleExceptionInternal(
      Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
    ErrorDetail errorDetail = new ErrorDetail();
    errorDetail.setTitle("BPR Gateway Exception");
    errorDetail.setStatus(status.value());
    errorDetail.setMessage(ex.getMessage());
    errorDetail.setTimestamp(ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT));

    return new ResponseEntity<>(errorDetail, null, status);
  }

  /*
   Annotation to say the following method is meant to handle any time the ExternalHTTPRequestException is thrown
  */
  @ExceptionHandler(ExternalHTTPRequestException.class)
  public ResponseEntity<?> handleResourceNotFoundException(
      ExternalHTTPRequestException externalHTTPRequestException) {
    ErrorDetail errorDetail = new ErrorDetail();
    errorDetail.setTimestamp(ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT));
    // Todo: Fix with appropriate status
    errorDetail.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
    errorDetail.setTitle("External HTTP Error");
    errorDetail.setMessage(externalHTTPRequestException.getMessage());

    return new ResponseEntity<>(errorDetail, null, HttpStatus.UNPROCESSABLE_ENTITY);
  }
}
