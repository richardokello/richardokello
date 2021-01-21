package ke.tracom.ufs.services.template;


import ke.tracom.ufs.entities.UfsOtp;
import ke.tracom.ufs.entities.UfsOtpCategory;
import ke.tracom.ufs.entities.UfsUser;
import ke.tracom.ufs.repositories.AuthenticationRepository;
import ke.tracom.ufs.repositories.OTPRepository;
import ke.tracom.ufs.services.CustomUserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * User Business Logic Actions
 *
 * @author Cornelius Muhatia
 */
@Service
@Transactional
@Qualifier("commonUserService")
public class CustomUserServiceTemplate implements CustomUserService {

    /**
     * Password Encoder
     */
    private final PasswordEncoder passEncoder;
    private final AuthenticationRepository authRepository;
    private final OTPRepository otpRepo;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public CustomUserServiceTemplate(PasswordEncoder passEncoder, AuthenticationRepository authRepository,
                                     OTPRepository otpRepo) {
        super();
        this.passEncoder = passEncoder;
        this.authRepository = authRepository;
        this.otpRepo = otpRepo;
    }

    @Override
    public String generateOTP(UfsUser user, UfsOtpCategory category) {
        String code = RandomStringUtils.random(6, false, true);
        String encCode = passEncoder.encode(code);
        UfsOtp authOtp = otpRepo.findByotp(encCode);
        while (authOtp != null) {
            code = RandomStringUtils.random(6, false, true);
            encCode = passEncoder.encode(code);
            authOtp = otpRepo.findByotp(encCode);
        }
        log.debug("Persisting generated otp {} in the database ", code);
        authOtp = new UfsOtp(encCode, category, user.getUserId().toString());
        otpRepo.save(authOtp);
        return code;
    }

}
