package ke.tra.com.tsync.wrappers;

import entities.UfsPosUser;

import java.io.Serializable;
import java.util.Date;
import java.util.Optional;

public class ResponseWrapper<T> implements Serializable {

    private String code;
    private String message;
    private T data;
    private Long timestamp;
    private Boolean error;
    private Optional<UfsPosUser> posUser;

    public ResponseWrapper() {
        this.code = "424";
        this.message = "Request could not be processed";
        this.timestamp = new Date().getTime();
        this.error = false;


    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }


    public Optional<UfsPosUser> getPosUser() {
        return posUser;
    }

    public void setPosUser(Optional<UfsPosUser> posUser) {
        this.posUser = posUser;
    }
}
