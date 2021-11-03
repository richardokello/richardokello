package ke.tra.ufs.webportal.service;

import ke.axle.chassis.exceptions.NotFoundException;
import ke.tra.ufs.webportal.entities.ParGlobalConfigProfile;
import ke.tra.ufs.webportal.entities.wrapper.GlobalProfileRequest;

public interface ParGlobalConfigProfileService {
    ParGlobalConfigProfile save(GlobalProfileRequest request);

    ParGlobalConfigProfile update(GlobalProfileRequest request) throws NotFoundException;
}
