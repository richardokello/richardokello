/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.tra.com.tsync.wrappers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePin {
    @NotNull
    @Size(max = 200)
    private String pin;
    @NotNull
    @Size(max = 200)
    private String username;
    @NotNull
    @Size(max = 200)
    private String newpin;
    @Size(max = 200)
    private String confirmPin;
    private String serialNumber;
    private String MID;
    private String TID;

}
