/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.co.tra.ufs.tms.repository;

import java.math.BigDecimal;

import ke.co.tra.ufs.tms.entities.UfsAuthOtp;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Owori Juma
 */
public interface AuthOtpRepository extends CrudRepository<UfsAuthOtp, BigDecimal> {
    
    UfsAuthOtp findByotp(String otp);
    /**
     * Fetch otp by user and otp code
     * @param otp
     * @param userId
     * @return 
     */
    UfsAuthOtp findByotpAndUserId(String otp, BigDecimal userId);
    
    void deleteByuserId(BigDecimal userId);
}
