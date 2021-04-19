package ke.tra.ufs.webportal.service.template;

import ke.tra.ufs.webportal.entities.UfsContactPerson;
import ke.tra.ufs.webportal.entities.UfsCustomerOwners;
import ke.tra.ufs.webportal.entities.UfsPosUserIdTracker;
import ke.tra.ufs.webportal.entities.UfsSysConfig;
import ke.tra.ufs.webportal.entities.wrapper.PosUserWrapper;
import ke.tra.ufs.webportal.repository.UfsPosUserIdTrackerRepository;
import ke.tra.ufs.webportal.repository.UfsSysConfigRepository;
import ke.tra.ufs.webportal.service.ContactPersonService;
import ke.tra.ufs.webportal.service.CustomerOwnersService;
import ke.tra.ufs.webportal.service.PosUserIdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Owori Juma
 */
@Service
@RequiredArgsConstructor
@CommonsLog
public class PosUserIdGeneratorService implements PosUserIdGenerator {
    private final UfsPosUserIdTrackerRepository posUserIdTrackerRepository;
    private final UfsSysConfigRepository ufsSysConfigRepository;
    private final CustomerOwnersService ownersService;
    private final ContactPersonService contactPersonService;

    @Override
    public String generateUsername(PosUserWrapper wrapper) {
        String username = "";
        int userIdLength = 7;
        UfsSysConfig useAlphanumeric = ufsSysConfigRepository.getConfiguration("Global Configuration", "usernameIsAlphanumeric");
        UfsSysConfig posUserIdLength = ufsSysConfigRepository.getConfiguration("Global Configuration", "posUserIdLength");
        UfsSysConfig useFirstName = ufsSysConfigRepository.getConfiguration("Global Configuration", "useFirstName");
        UfsSysConfig posUserIdPrefixSizeObj = ufsSysConfigRepository.getConfiguration("Global Configuration", "posUserIdPrefixSize");

        int posUserIdPrefixSize = posUserIdPrefixSizeObj == null ? 3 : Integer.parseInt(posUserIdPrefixSizeObj.getValue());
        try {
            userIdLength = posUserIdLength == null ? 7 : Integer.parseInt(posUserIdLength.getValue());
        } catch (Exception ignored) {
        }
        if (useAlphanumeric.getValue().equals("1")) {
            if (useFirstName.getValue().equals("1")) {
                username = getUsername(wrapper, "useFirstName", posUserIdPrefixSize);
            } else {
                username = getUsername(wrapper, "userOther", posUserIdPrefixSize);
            }
        } else {
            if (useFirstName.getValue().equals("1")) {
                username += getUsername(wrapper, "useFirstName", posUserIdPrefixSize);
            } else {
                username += getUsername(wrapper, "userOther", posUserIdPrefixSize);
            }
        }

        UfsPosUserIdTracker tracker = posUserIdTrackerRepository.findTopByOrderByCurrentPosUserIdDesc();
        String usernamePadded = StringUtils.rightPad(username, userIdLength - tracker.getCurrentPosUserId().toString().length(), "0");
        username = usernamePadded.concat(tracker.getCurrentPosUserId().toString());

        Set<String> usernames = new HashSet<>();
        usernames.add(username);
        List<UfsCustomerOwners> customerOwners = ownersService.findByUsernameIn(usernames);
        List<UfsContactPerson> contactPeople = contactPersonService.findByUsernameIn(usernames);
        if (customerOwners.size() > 0 || contactPeople.size() > 0) {
            return this.generateUsername(new PosUserWrapper("_" + username));
        }
        tracker.setIdsRemaining(tracker.getIdsRemaining() - 1);
        tracker.setCurrentPosUserId(tracker.getCurrentPosUserId() + 1);
        posUserIdTrackerRepository.save(tracker);
        return username.toLowerCase();
    }

    private String getUsername(PosUserWrapper wrapper, String nameType, int posUserIdPrefixSize) {
        String username;
        switch (nameType) {
            case "useFirstName":
                username = (wrapper.getFirstName().length() > posUserIdPrefixSize) ? wrapper.getFirstName().substring(0, posUserIdPrefixSize) : (wrapper.getOtherName() != null) ? (wrapper.getFirstName() + wrapper.getOtherName()).substring(0, ((wrapper.getFirstName() + wrapper.getOtherName()).length() > posUserIdPrefixSize) ? posUserIdPrefixSize : (wrapper.getFirstName() + wrapper.getOtherName()).length() - 1) : wrapper.getFirstName();
                break;
            default:
                username = (wrapper.getOtherName().length() > posUserIdPrefixSize) ? wrapper.getOtherName().substring(0, posUserIdPrefixSize) : (wrapper.getFirstName() != null) ? (wrapper.getFirstName() + wrapper.getOtherName()).substring(0, ((wrapper.getFirstName() + wrapper.getOtherName()).length() > posUserIdPrefixSize) ? posUserIdPrefixSize : (wrapper.getFirstName() + wrapper.getOtherName()).length() - 1) : wrapper.getOtherName();
                break;
        }
        return username;
    }

}
