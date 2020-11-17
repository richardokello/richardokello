/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.com.tsync.wrappers;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 *
 * @author Tracom
 */

@Data
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @ToString
public class TrcmGeneralRestWrapper {

    @JsonProperty("code")
    private String code;

    @JsonProperty("message")
    private String description;

    @JsonProperty("data")
    private Object object;

}
