package ke.tra.ufs.webportal.wrappers;


import ke.tra.ufs.webportal.entities.UfsUser;

import java.util.Date;

/**
 *
 * @author eli.muraya
 */

public class UserResponseWrapper {
    private Long code;
    private String message;
    private UfsUser data;
    private Date timestamp;

    public UserResponseWrapper(){}
    public UserResponseWrapper(Long code, String message, UfsUser data, Date timestamp) {
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

    public UfsUser getData() {
        return data;
    }

    public void setData(UfsUser data) {
        this.data = data;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
