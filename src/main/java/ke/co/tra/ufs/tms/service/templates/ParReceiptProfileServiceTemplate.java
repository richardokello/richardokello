package ke.co.tra.ufs.tms.service.templates;

import ke.co.tra.ufs.tms.entities.ParReceiptProfile;
import ke.co.tra.ufs.tms.repository.ParReceiptProfileRepository;
import ke.co.tra.ufs.tms.service.ParReceiptProfileService;
import ke.co.tra.ufs.tms.utils.AppConstants;
import org.springframework.stereotype.Service;


@Service
public class ParReceiptProfileServiceTemplate implements ParReceiptProfileService {
    private final ParReceiptProfileRepository parReceiptProfileRepository;

    public ParReceiptProfileServiceTemplate(ParReceiptProfileRepository parReceiptProfileRepository) {
        this.parReceiptProfileRepository = parReceiptProfileRepository;
    }


    @Override
    public ParReceiptProfile findByProfileName(String name) {
        return parReceiptProfileRepository.findAllByNameAndIntrash(name, AppConstants.NO);
    }
}
