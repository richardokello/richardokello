package co.ke.tracom.bprgateway.web.wasac.data.customerprofile;

import co.ke.tracom.bprgateway.web.util.data.MerchantAuthInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerProfileRequest {

    @ApiModelProperty(name = "Customer ID", value = "Unique customer identifier", required = true)
    private String customerId;

    @ApiModelProperty(name = "Merchant Validation Data", value = "Authenticate and validate merchant", required = true)
    private MerchantAuthInfo credentials;
}
