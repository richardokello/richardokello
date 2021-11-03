package ke.co.tra.ufs.tms.service;

import ke.co.tra.ufs.tms.entities.ParGlobalConfigProfile;
import ke.co.tra.ufs.tms.entities.wrappers.GlobalProfileRequest;
import ke.co.tra.ufs.tms.utils.exceptions.NotFoundException;

public interface ParGlobalConfigProfileService {
    ParGlobalConfigProfile save(GlobalProfileRequest request);

    ParGlobalConfigProfile update(GlobalProfileRequest request) throws NotFoundException;
}
