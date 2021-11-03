/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.config;

import org.springframework.security.access.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;
import ke.co.tra.ufs.tms.utils.CustomEntry;
import ke.co.tra.ufs.tms.utils.SharedMethods;
import ke.co.tra.ufs.tms.utils.exceptions.GeneralBadRequest;
import ke.co.tra.ufs.tms.utils.exceptions.RunTimeBadRequest;
import ke.co.tra.ufs.tms.wrappers.ResponseWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.common.exceptions.UserDeniedAuthorizationException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;
import org.apache.tomcat.util.http.fileupload.FileUploadBase.SizeLimitExceededException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.multipart.MultipartException;

/**
 * Controller advice to translate the server side exceptions to client-friendly
 * json structures.
 */
@ControllerAdvice
public class ExceptionTranslator {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(org.springframework.orm.jpa.JpaObjectRetrievalFailureException.class)
    public ResponseEntity<ResponseWrapper> entityRetriavalFailure(org.springframework.orm.jpa.JpaObjectRetrievalFailureException ex) {
        log.error("Constraint violation", ex);
        ResponseWrapper response = new ResponseWrapper();
        response.setCode(400);
        response.setMessage("Failed to locate items with the specified id(s)");
        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<ResponseWrapper> fileuploadException(MultipartException ex) {
        log.error("Multipart error: ", ex);
        ResponseWrapper response = new ResponseWrapper();
        response.setCode(400);
        if (ex.getCause().getCause() instanceof SizeLimitExceededException) {
            SizeLimitExceededException sizeEx = (SizeLimitExceededException) ex.getCause().getCause();
            response.setMessage("File size exceeded allowed limit (Permitted size: " + sizeEx.getPermittedSize() + " KB)");
        } else {
            response.setMessage("File upload validation failed. Ensure the file is within the specified size and type");
        }
//        log.warn("Multipart error cause: " + ex.getCause().getCause());
        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public ResponseEntity<ResponseWrapper> processConstraintViolation(org.springframework.dao.DataIntegrityViolationException ex) {
        log.error("Constraint violation", ex);
        ResponseWrapper response = new ResponseWrapper();
        response.setCode(424);
        response.setMessage("Dependency failed this may be caused by supplying id that is not related to "
                + "the current request object");
        return new ResponseEntity(response, HttpStatus.FAILED_DEPENDENCY);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseWrapper> processSpringValidationError(HttpMessageNotReadableException ex) {
        log.error(ex.getMessage(), ex);
        ResponseWrapper response = new ResponseWrapper();
        response.setCode(400);
        //response.setData(ex.getMessage());
        response.setMessage("Bad request please check your input before trying again");
        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(org.springframework.web.client.HttpClientErrorException.class)
    public ResponseEntity<ResponseWrapper> restTemplateErrors(org.springframework.web.client.HttpClientErrorException ex) {
        log.error(ex.getMessage(), ex);
        ResponseWrapper response = new ResponseWrapper();
        if (ex.getStatusCode().value() == 404) {
            response.setCode(404);
            response.setMessage("Failed to locate third party resources");
            return new ResponseEntity(response, HttpStatus.NOT_FOUND);
        }
        response.setCode(500);
        //response.setData(ex.getMessage());
        response.setMessage("Internal server error occured");
        return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(org.springframework.web.bind.MissingServletRequestParameterException.class)
    public ResponseEntity<ResponseWrapper> processSpringValidationError(org.springframework.web.bind.MissingServletRequestParameterException ex) {
        log.error(ex.getMessage(), ex);
        ResponseWrapper response = new ResponseWrapper();
        response.setCode(400);
        response.setData(new CustomEntry("description", ex.getMessage()));
        response.setMessage("Missing request parameters");
        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserDeniedAuthorizationException.class)
    public ResponseEntity<ResponseWrapper> processAccessDeniedError(UserDeniedAuthorizationException ex) {
        ResponseWrapper response = new ResponseWrapper();
        response.setCode(401);
        Map<String, Object> data = new HashMap();
        data.put("description", ex.getMessage());
        response.setData(data);
        response.setMessage("You are not authorized to use this resources");
        return new ResponseEntity(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ResponseWrapper> processExpectationError(HttpMediaTypeNotSupportedException ex) {
        ResponseWrapper response = new ResponseWrapper();
        response.setCode(417);
        response.setMessage("Sorry unsupported media type");
        return new ResponseEntity(response, HttpStatus.EXPECTATION_FAILED);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ResponseWrapper> processMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        ResponseWrapper response = new ResponseWrapper();
        response.setCode(405);
        response.setMessage(ex.getMessage());
        return new ResponseEntity(response, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler({NullPointerException.class, org.springframework.orm.jpa.JpaSystemException.class, 
        java.sql.SQLException.class, javax.persistence.PersistenceException.class})
    public ResponseEntity<ResponseWrapper> processNullPointerError(NullPointerException ex) {
        log.error("Null pointer exception occured", ex);
        ResponseWrapper response = new ResponseWrapper();
        response.setCode(500);
        response.setMessage("Sorry internal server error occured please check your request before trying again");
        return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(org.hibernate.AssertionFailure.class)
    public ResponseEntity<ResponseWrapper> processNullId(org.hibernate.AssertionFailure ex) {
        log.error("Trying to reference id that doesn't exist", ex);
        ResponseWrapper response = new ResponseWrapper();
        response.setCode(400);
        response.setMessage("Sorry bad request, the specified id not found");
        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({GeneralBadRequest.class})
    public ResponseEntity<ResponseWrapper> processGeneralBadRequest(Exception ex) {
        ResponseWrapper response = new ResponseWrapper();
        response.setCode(400);
        response.setMessage(ex.getMessage());
        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RunTimeBadRequest.class)
    public ResponseEntity<ResponseWrapper> processGeneralBadRequest(RunTimeBadRequest ex) {
        ResponseWrapper response = new ResponseWrapper();
        response.setCode(400);
        response.setMessage(ex.getMessage());
        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
    }    

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ResponseWrapper> processGeneralException(BadCredentialsException ex) {
        log.error("General exception thrown", ex);
        ResponseWrapper response = new ResponseWrapper();
        response.setCode(401);
        response.setMessage("Sorry bad credentials");
        return new ResponseEntity(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ResponseWrapper> processAccessDenied(AccessDeniedException ex) {
        log.error("Access Denied exception thrown", ex);
        ResponseWrapper response = new ResponseWrapper();
        response.setCode(403);
        response.setMessage("Sorry you don't have privileges to perform this action");
        response.setData(new CustomEntry("description", ex.getMessage()));
        return new ResponseEntity(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<ResponseWrapper> proccessAuthenticationError(InternalAuthenticationServiceException ex) {
        log.error("Internal authentication exception thrown", ex);
        ResponseWrapper response = new ResponseWrapper();
        response.setCode(401);
        response.setMessage("Sorry internal authentication error occured");
        return new ResponseEntity(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({InvalidDataAccessApiUsageException.class})
    public ResponseEntity<ResponseWrapper> proccessInternalError(Exception ex) {
        log.error("Internal server error occured", ex);
        ResponseWrapper response = new ResponseWrapper();
        response.setCode(500);
        response.setMessage("Sorry internal server error occured");
        return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(org.springframework.mail.MailSendException.class)
    public ResponseEntity<ResponseWrapper> processMailException(org.springframework.mail.MailSendException ex) {
        log.error("Failed to send email", ex);
        ResponseWrapper response = new ResponseWrapper();
        response.setCode(500);
        response.setMessage("Failed to send email, this may be due to using a wrong email address");
        return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseWrapper> validationException(org.springframework.web.bind.MethodArgumentNotValidException ex) {
        log.error("Validation errors occurred", ex);
        ResponseWrapper response = new ResponseWrapper();
        response.setCode(400);
        response.setMessage("Sorry validation errors occurred");
        response.setData(SharedMethods.getFieldMapErrors(ex.getBindingResult()));
        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(org.springframework.validation.BindException.class)
    public ResponseEntity<ResponseWrapper> validationException(org.springframework.validation.BindException ex) {
        log.error("Validation errors occurred", ex);
        ResponseWrapper response = new ResponseWrapper();
        response.setCode(400);
        response.setMessage("Sorry validation errors occurred");
        response.setData(SharedMethods.getFieldMapErrors(ex.getBindingResult()));
        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(java.lang.UnsupportedOperationException.class)
     public ResponseEntity<ResponseWrapper> unImplementedException(java.lang.UnsupportedOperationException ex) {
        log.debug("Unsupported exception: ",ex);
         ResponseWrapper response = new ResponseWrapper();
        response.setCode(HttpStatus.NOT_IMPLEMENTED.value());
        response.setMessage("Sorry the requested resource is not yet implemented");
        return new ResponseEntity(response, HttpStatus.NOT_IMPLEMENTED);
    }
}
