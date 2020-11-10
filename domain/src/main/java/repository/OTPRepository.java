package repository;
import entities.UfsOtp;
import entities.UfsOtpCategory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface OTPRepository extends CrudRepository<UfsOtp, BigDecimal> {

    //@Modifiable

    /**
     * Used to delete OTP by user
     *
     * @param userId
     */
    public void deleteByuserId(String userId);

    /**
     * Delete OTP by user and category
     *
     * @param userId
     * @param category
     */
    public void deleteByuserIdAndOtpCategory(String userId, UfsOtpCategory category);

    /**
     * Fetch entity by otp
     *
     * @param otp
     * @return
     */
    public UfsOtp findByotp(String otp);

    /**
     * @param userId
     * @param category
     * @return
     */
    public List<UfsOtp> findByuserIdAndOtpCategory(String userId, UfsOtpCategory category);

    UfsOtp findByUserId(String userId);

}
