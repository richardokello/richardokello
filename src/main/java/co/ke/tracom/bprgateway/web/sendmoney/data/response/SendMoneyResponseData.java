package co.ke.tracom.bprgateway.web.sendmoney.data.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SendMoneyResponseData {
    private double charges;
    private String T24Reference;
    private String rrn;

}
