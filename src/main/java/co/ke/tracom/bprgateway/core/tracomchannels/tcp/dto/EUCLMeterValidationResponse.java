package co.ke.tracom.bprgateway.core.tracomchannels.tcp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
@AllArgsConstructor
public class EUCLMeterValidationResponse {
    private int status;
    private String customerName;
    private String meterLocation;
    private String failNarration;
}
