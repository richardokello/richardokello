package ke.tracom.ufs.services;

import ke.tracom.ufs.entities.UfsOtpCategory;
import ke.tracom.ufs.entities.UfsUser;

public interface CustomUserService {
	/**
	 * Generates otp based on type and persists in the database under the specified user
	 * @param user
	 * @param category OTP category i.e authentication
	 * @return generated OTP
	 */
	 public String generateOTP(UfsUser user, UfsOtpCategory category);
}
