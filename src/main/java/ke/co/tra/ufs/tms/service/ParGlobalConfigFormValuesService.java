package ke.co.tra.ufs.tms.service;

import ke.co.tra.ufs.tms.entities.ParGlobalConfigFormValues;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

public interface ParGlobalConfigFormValuesService {
    Optional<ParGlobalConfigFormValues> findFormValue(BigDecimal type);

    ParGlobalConfigFormValues save(ParGlobalConfigFormValues values);

    Page<ParGlobalConfigFormValues> getConfigFormByConfigType(String actionStatus, String configTypeId, Date from, Date to, String needle, Pageable pg);
}
