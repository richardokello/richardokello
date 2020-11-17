package ke.tra.com.tsync.services;


import entities.UfsOtp;
import entities.UfsOtpCategory;
import entities.UfsPosUser;
import entities.UfsSysConfig;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ke.tra.com.tsync.services.template.OtpServiceTmpl;
import repository.AuthenticationRepository;
import repository.OTPRepository;
import repository.UfsSysConfigRepository;

import javax.transaction.Transactional;

/**
 * @author cotuoma
 */
@Service
@Transactional
@Qualifier("commonUserService")
public class OtpService implements OtpServiceTmpl {

    /**
     * Password Encoder
     */
    private final PasswordEncoder passEncoder;
    private final AuthenticationRepository authRepository;
    private final OTPRepository otpRepo;
    private final UfsSysConfigRepository configRepository;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public OtpService(PasswordEncoder passEncoder, AuthenticationRepository authRepository, OTPRepository otpRepo, UfsSysConfigRepository configRepository) {
        super();
        this.passEncoder = passEncoder;
        this.authRepository = authRepository;
        this.otpRepo = otpRepo;
        this.configRepository = configRepository;
    }

    @Override
    public String generateOTP(UfsPosUser user, UfsOtpCategory category) {
        UfsSysConfig config = configRepository.findByEntityAndParameter("Pos Configuration", "Pos Otp Length");
        String configValue = config == null?"4": config.getValue();
        String code = RandomStringUtils.random(Integer.parseInt(configValue), false, true);
        String encCode = passEncoder.encode(code);
        UfsOtp authOtp = otpRepo.findByotp(encCode);
        while (authOtp != null) {
            code = RandomStringUtils.random(Integer.parseInt(configValue), false, true);
            encCode = passEncoder.encode(code);
            authOtp = otpRepo.findByotp(encCode);
        }
        log.debug("=========== here ", code);
        authOtp = new UfsOtp();
        authOtp.setOtp(encCode);
        authOtp.setUserId(user.getPosUserId().toString());
        authOtp.setOtpCategory(category);

        otpRepo.save(authOtp);
        return code;
    }

}
