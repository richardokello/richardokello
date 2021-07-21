package co.ke.tracom.bprgateway.web.sms.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SMSResponse {
    String status;
    String message;
}
