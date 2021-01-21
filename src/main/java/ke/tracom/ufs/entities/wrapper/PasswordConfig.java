/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.tracom.ufs.entities.wrapper;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Owori Juma
 */
public class PasswordConfig {
    @NotNull
    @Max(1000)
    @Min(0)
    public Integer characters;
    @NotNull
    @Max(1000)
    @Min(0)
    public Integer numeric;
    @NotNull
    @Max(10000)
    @Min(0)
    public Integer length;
    @NotNull
    @Max(1000)
    @Min(0)
    public Integer attempts;
    @NotNull
    @Min(1)
    public Long expiry;
    @NotNull
    @Max(1000)
    @Min(0)
    public Integer reuseCount;
    @NotNull
    @Max(1000)
    @Min(0)
    public Integer lowerCase;
    @NotNull
    @Max(1000)
    @Min(0)
    public Integer upperCase;
    @NotNull
    @Max(1000)
    @Min(0)
    public Integer specialChar;
    @NotNull
    @Min(1)
    public Long otpExpiry;
    @NotNull
    @Max(100)
    @Min(0)
    public Integer otpAttempts;

    public PasswordConfig() {
    }
    public PasswordConfig(int charNumber, int numericNumber, int specialCharNumber, int length) {
        this.characters = charNumber;
        this.numeric = numericNumber;
        this.specialChar = specialCharNumber;
        this.length = length;
    }    

    public PasswordConfig(Integer characters, Integer numeric, Integer length, 
            Integer attempts, Long expiry, Integer reuseCount, Integer lowerCase, 
            Integer upperCase, Integer specialChar, Long otpExpiry, Integer otpAttempts) {
        this.characters = characters;
        this.numeric = numeric;
        this.length = length;
        this.attempts = attempts;
        this.expiry = expiry;
        this.reuseCount = reuseCount;
        this.lowerCase = lowerCase;
        this.upperCase = upperCase;
        this.specialChar = specialChar;
        this.otpExpiry = otpExpiry;
        this.otpAttempts = otpAttempts;
    }
    
    
}
