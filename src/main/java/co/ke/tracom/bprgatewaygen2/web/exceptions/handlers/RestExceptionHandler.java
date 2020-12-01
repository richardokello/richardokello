package co.ke.tracom.bprgatewaygen2.web.exceptions.handlers;

import co.ke.tracom.bprgatewaygen2.web.exceptions.custom.ExternalHTTPRequestException;
import co.ke.tracom.bprgatewaygen2.web.exceptions.models.ErrorDetail;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

/**
 * This is the driving class when an exception occurs.
 * All exceptions are handled here.
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    public RestExceptionHandler() {
        super();
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex,
            Object body,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setTitle("Rest Internal Exception");
        errorDetail.setStatus(status.value());
        errorDetail.setMessage(ex.getMessage());
        errorDetail.setTimestamp(new Date());

        return new ResponseEntity<>(errorDetail,
                null,
                status);
    }

    // annotation to say the following method is meant to handle any time the ExternalHTTPRequestException is thrown
    @ExceptionHandler(ExternalHTTPRequestException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ExternalHTTPRequestException externalHTTPRequestException)
    {
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setTimestamp(new Date());
        // Todo: Fix with appropriate status
        errorDetail.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
        errorDetail.setTitle("External HTTP Error");
        errorDetail.setMessage(externalHTTPRequestException.getMessage());

        return new ResponseEntity<>(errorDetail,
                null,
                HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
