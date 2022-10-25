package co.ke.tracom.bprgateway.web.sendmoney.data.response;

import co.ke.tracom.bprgateway.web.util.data.BaseResponseData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SendMoneyResponseData  extends BaseResponseData {
    private double charges;
    private String T24Reference;
    private String rrn;
    private String tid;
    private String mid;
    private  String firstName;
    private String surname;
}
