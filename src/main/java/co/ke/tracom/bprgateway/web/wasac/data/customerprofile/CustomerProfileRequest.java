package co.ke.tracom.bprgateway.web.wasac.data.customerprofile;

import co.ke.tracom.bprgateway.web.util.data.MerchantAuthInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerProfileRequest {

    //ApiModelProperty(name = "Customer ID", value = "Unique customer identifier", required = true)
    @JsonProperty("customerId")
    private String customerId;

    //@ApiModelProperty(name = "Merchant Validation Data", value = "Authenticate and validate merchant", required = true)
    @JsonProperty("credentials")
    private MerchantAuthInfo credentials;
}
