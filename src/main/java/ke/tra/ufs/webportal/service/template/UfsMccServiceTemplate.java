package ke.tra.ufs.webportal.service.template;

import ke.axle.chassis.utils.AppConstants;
import ke.tra.ufs.webportal.entities.UfsMcc;
import ke.tra.ufs.webportal.entities.wrapper.MccWrapper;
import ke.tra.ufs.webportal.repository.UfsMccRepository;
import ke.tra.ufs.webportal.service.UfsMccService;
import ke.tra.ufs.webportal.utils.SharedMethods;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


@Transactional
@Service
@CommonsLog
public class UfsMccServiceTemplate implements UfsMccService {
    private final UfsMccRepository ufsMccRepository;
    private final SharedMethods sharedMethods;

    public UfsMccServiceTemplate(UfsMccRepository ufsMccRepository, SharedMethods sharedMethods) {
        this.ufsMccRepository = ufsMccRepository;
        this.sharedMethods = sharedMethods;
    }

    @Override
    @Async
    public void processFileUpload(MultipartFile file) throws IOException, InvalidFormatException {
        Set<MccWrapper> mccs = sharedMethods.convertXlsMcc(MccWrapper.class, sharedMethods.convert(file));
        Set<UfsMcc> mccBatch = new TreeSet<>();
        for (MccWrapper mcc : mccs) {
            List<UfsMcc> ufsMcc1 = ufsMccRepository.findByNameAndIntrash(mcc.getMccTitle(), AppConstants.NO);
            List<UfsMcc> ufsValue = ufsMccRepository.findByValueAndIntrash(mcc.getMcc(), AppConstants.NO);
            if (ufsMcc1.size() > 0 || ufsValue.size() > 0) {
                continue;
            }

            if (mcc.getMccTitle() == null || mcc.getMcc() == null) {
                log.error("MCC=>" + mcc.getMccTitle() + "Code =>" + mcc.getMcc());
            } else {
                mccBatch.add(new UfsMcc(mcc.getMccTitle(), mcc.getMcc()));
            }
        }
        ufsMccRepository.saveAll(mccBatch);
    }
}
