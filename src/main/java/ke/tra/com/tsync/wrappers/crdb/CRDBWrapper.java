/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.com.tsync.wrappers.crdb;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 *
 * @author Tracom
 */

@Data
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @ToString
public class CRDBWrapper {

    @JsonProperty("status")
    private int status;
    
    @JsonProperty("statusDesc")
    private String statusDesc;

    @JsonProperty("data")
    private Object data;

}
