package ke.tracom.ufs.wrappers;

import ke.tracom.ufs.wrappers.IsInitiatorResponseWrapper;

import java.util.Date;

public class IsInitiatorResonseEntity {
    private Long code;
    private String message;
    private IsInitiatorResponseWrapper data;
    private Date timestamp;

    public IsInitiatorResonseEntity(){}
    public IsInitiatorResonseEntity(Long code, String message, IsInitiatorResponseWrapper data, Date timestamp) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = timestamp;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public IsInitiatorResponseWrapper getData() {
        return data;
    }

    public void setData(IsInitiatorResponseWrapper data) {
        this.data = data;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
