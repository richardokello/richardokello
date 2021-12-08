package ke.co.tra.ufs.tms.service.templates;

import ke.co.tra.ufs.tms.entities.ParGlobalConfig;
import ke.co.tra.ufs.tms.repository.ParGlobalConfigRepository;
import ke.co.tra.ufs.tms.service.ParGlobalConfigService;
import ke.co.tra.ufs.tms.utils.AppConstants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
public class ParGlobalConfigServiceTemplate implements ParGlobalConfigService {

    private final ParGlobalConfigRepository parGlobalConfigRepository;

    public ParGlobalConfigServiceTemplate(ParGlobalConfigRepository parGlobalConfigRepository) {
        this.parGlobalConfigRepository = parGlobalConfigRepository;
    }

    @Override
    public Page<ParGlobalConfig> getGlobalConfigByProfile(String actionStatus, String profile, Date from, Date to, String needle, Pageable pg) {
        return parGlobalConfigRepository.findAllConfigsByProfile(actionStatus,profile,from,to,needle, AppConstants.NO,pg);
    }
}
