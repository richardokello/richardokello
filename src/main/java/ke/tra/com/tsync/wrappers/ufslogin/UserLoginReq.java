/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.com.tsync.wrappers.ufslogin;

import lombok.*;
import lombok.Data;

/**
 * @author Tracom
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class UserLoginReq {

    String username;
    String pin;
   // @JsonAlias("serialNumber")
    String serialNumber;

}
