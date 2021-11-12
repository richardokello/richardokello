package ke.co.tra.ufs.tms.service;

import ke.co.tra.ufs.tms.entities.ParReceiptProfile;

public interface ParReceiptProfileService {

    ParReceiptProfile findByProfileName(String name);
}
