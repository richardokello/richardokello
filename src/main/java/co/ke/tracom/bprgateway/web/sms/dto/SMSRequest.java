package co.ke.tracom.bprgateway.web.sms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class SMSRequest {
    private String recipient;
    private String message;
    private String SMSFunction;

    public static final String  SMS_FUNCTION_SENDER="SENDER";
    public static final String  SMS_FUNCTION_RECEIVER="RECEIVER";
    public static final String  SMS_FUNCTION_OTP="OTP";
}
