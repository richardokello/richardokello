package co.ke.tracom.bprgatewaygen2.web.exceptions.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class ErrorDetail {
    private String title;
    private int status; // error code
    private String message;
    private Date timestamp;
}
