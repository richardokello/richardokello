/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.co.tra.ufs.tms.entities.wrappers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import ke.co.tra.ufs.tms.utils.annotations.ExportField;
import lombok.Data;

/**
 *
 * @author Cornelius M
 */
@Data
@JsonIgnoreProperties(value = {"", " "})
public class WhitelistDetails {
    
    @JsonProperty(value = "Serial Number")
    @ExportField(name = "Serial Number")
    @NotNull
    @Size(min = 1, max = 200)
    private String serialNo;
    @NotNull
    @JsonProperty(value = "Bank Code")
    @ExportField(name = "Bank Code")
    @Size(max = 50)
    private String bankCode;

}
