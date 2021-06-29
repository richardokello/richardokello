package co.ke.tracom.bprgateway.web.exceptions.handlers;

import co.ke.tracom.bprgateway.web.exceptions.custom.BankBranchException;
import co.ke.tracom.bprgateway.web.exceptions.custom.InterServiceCommunicationException;
import co.ke.tracom.bprgateway.web.exceptions.custom.InvalidAgentCredentialsException;
import co.ke.tracom.bprgateway.web.exceptions.models.GenericResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {InvalidAgentCredentialsException.class})
    protected ResponseEntity<?> handleInvalidAgentCredentialsError(Exception ex, WebRequest request) {
        GenericResponse response = new GenericResponse().setMessage("Invalid Agent Validation information. Please try again!")
                .setStatus("117");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ExceptionHandler(value = {InterServiceCommunicationException.class})
    protected ResponseEntity<?> handleInterServiceCommunicationError(RuntimeException ex, WebRequest request) {
        GenericResponse response = new GenericResponse().setMessage(ex.getMessage())
                .setStatus(String.valueOf( HttpStatus.INTERNAL_SERVER_ERROR.value()));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ExceptionHandler(value = {BankBranchException.class})
    protected ResponseEntity<?> bankBranchExceptionHandler(RuntimeException ex, WebRequest request) {
        GenericResponse response = new GenericResponse().setMessage(ex.getMessage())
                .setStatus("065");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
