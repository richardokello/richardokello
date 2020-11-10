package ke.tra.com.tsync.services.template;

import entities.UfsOtpCategory;
import entities.UfsPosUser;

public interface OtpServiceTmpl {
    String generateOTP(UfsPosUser user, UfsOtpCategory category);
}
