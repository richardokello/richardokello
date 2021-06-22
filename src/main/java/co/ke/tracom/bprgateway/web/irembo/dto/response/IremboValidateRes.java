/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.ke.tracom.bprgateway.web.irembo.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class IremboValidateRes {
    @JsonProperty("result_code")
    private String resultCode;
    @JsonProperty("message")
    private String message;
    @JsonProperty("bill_details")
    private BillDetails billDetails;
}
