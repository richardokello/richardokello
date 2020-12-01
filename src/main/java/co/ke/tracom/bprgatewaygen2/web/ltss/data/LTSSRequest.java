package co.ke.tracom.bprgatewaygen2.web.ltss.data;

import lombok.Data;

import java.util.Date;

@Data
public class LTSSRequest {
    private String identification;
    private String extRefNo;
    private String intermediary;
    private String description;
    private Date datetime;
    private long amount;
}
