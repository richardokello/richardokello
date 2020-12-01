package co.ke.tracom.bprgatewaygen2.web.ltss.data.NewSubscriber;

import lombok.Data;

@Data
public class NewSubscriberRequest {
    private String identification;
    private String phone;
    private String occupation;
    private int frequency;
    private long amount;
}
