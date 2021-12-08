package ke.co.tra.ufs.tms.service;

import ke.co.tra.ufs.tms.entities.ParGlobalConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;

public interface ParGlobalConfigService {

    Page<ParGlobalConfig> getGlobalConfigByProfile(String actionStatus, String profile, Date from, Date to, String needle, Pageable pg);

}
