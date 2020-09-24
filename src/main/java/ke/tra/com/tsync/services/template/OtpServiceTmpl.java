package ke.tra.com.tsync.services.template;

import ke.tra.com.tsync.entities.UfsOtpCategory;
import ke.tra.com.tsync.entities.UfsPosUser;

public interface OtpServiceTmpl {
    String generateOTP(UfsPosUser user, UfsOtpCategory category);
}
