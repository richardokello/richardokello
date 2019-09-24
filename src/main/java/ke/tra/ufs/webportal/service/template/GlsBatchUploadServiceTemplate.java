package ke.tra.ufs.webportal.service.template;

import ke.tra.ufs.webportal.entities.UfsGls;
import ke.tra.ufs.webportal.entities.UfsGlsBatch;
import ke.tra.ufs.webportal.repository.UfsGlsBatchRepository;
import ke.tra.ufs.webportal.repository.UfsGlsRepository;
import ke.tra.ufs.webportal.service.GlsBatchUploadService;
import ke.tra.ufs.webportal.service.SysConfigService;
import ke.tra.ufs.webportal.utils.AppConstants;
import ke.tra.ufs.webportal.utils.SharedMethods;
import ke.tra.ufs.webportal.wrappers.UfsGlsWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class GlsBatchUploadServiceTemplate implements GlsBatchUploadService {

    private final UfsGlsRepository ufsGlsRepository;
    private final UfsGlsBatchRepository ufsGlsBatchRepository;
    private final Logger log = LoggerFactory.getLogger(GlsBatchUploadServiceTemplate.class);

    public GlsBatchUploadServiceTemplate(UfsGlsRepository ufsGlsRepository, UfsGlsBatchRepository ufsGlsBatchRepository) {
        this.ufsGlsRepository = ufsGlsRepository;
        this.ufsGlsBatchRepository = ufsGlsBatchRepository;
    }

    @Override
    @Async
    public void processGlsFileUpload(UfsGlsBatch batch, SysConfigService configService, SharedMethods sharedMethods, byte[] file, String ipAddress, String userAgent, UfsGlsWrapper payload) {
        try {
            List<UfsGls> ufsGlsList = new ArrayList<>();
            List<UfsGlsWrapper> entities = sharedMethods.convertCsv(UfsGlsWrapper.class, payload.getFile());
            long failed = 0;
            long success = 0;
            for (UfsGlsWrapper entity : entities) {
                log.info("Entity Name {}", entity.getGlName());
                UfsGls ufsGls1 = ufsGlsRepository.findByGlCodeAndIntrash(entity.getGlCode(), AppConstants.NO);
                if (ufsGls1 != null) {
                    failed++;
                    continue;
                }

                UfsGls ufsGls = new UfsGls();
                ufsGls.setBatchs(batch.getBatchId());
                ufsGls.setGlName(entity.getGlName());
                ufsGls.setGlCode(entity.getGlCode());
                ufsGls.setGlAccountNumber(entity.getGlAccountNumber());
                ufsGls.setGlLocation(entity.getGlLocation());
                ufsGls.setBankIds(entity.getBankIds());
                ufsGls.setBankBranchIds(entity.getBankBranchIds());
                ufsGls.setTenantIds(entity.getTenantIds());

                ufsGlsList.add(ufsGls);

                success++;
            }
            ufsGlsRepository.saveAll(ufsGlsList);

            batch.setProcessingStatus(AppConstants.STATUS_COMPLETED);
        } catch (IOException ex) {
            ex.printStackTrace();
            log.error(AppConstants.AUDIT_LOG, "Processing GLS Devices upload failed", ex);
            batch.setProcessingStatus(AppConstants.ACTIVITY_STATUS_FAILED);
        }

        ufsGlsBatchRepository.save(batch);
    }
}
