package ke.tracom.ufs.utils.exceptions;

import ke.axle.chassis.wrappers.ResponseWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseWrapper> validationException(MethodArgumentNotValidException e) {
        ResponseWrapper wrapper = new ResponseWrapper();
        ArrayList<String> errors = new ArrayList<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            errors.add(error.getDefaultMessage());
        });
        wrapper.setCode(400);
        wrapper.setMessage("Bad Request");
        wrapper.setData(errors);
        return ResponseEntity.badRequest().body(wrapper);
    }


}
