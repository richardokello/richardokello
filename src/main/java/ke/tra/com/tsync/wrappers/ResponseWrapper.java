package ke.tra.com.tsync.wrappers;

import java.io.Serializable;
import java.util.Date;

public class ResponseWrapper<T> implements Serializable {

    private int code;
    private String message;
    private T data;
    private Long timestamp;
    private Boolean error;

    public ResponseWrapper() {
        this.code = 424;
        this.message = "Request could not be completed";
        this.timestamp = new Date().getTime();
        this.error = false;

    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
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
}
