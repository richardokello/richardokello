package ke.co.tra.ufs.tms.service;

import ke.co.tra.ufs.tms.entities.ParGlobalConfigFormValues;

import java.math.BigDecimal;
import java.util.Optional;

public interface ParGlobalConfigFormValuesService {
    Optional<ParGlobalConfigFormValues> findFormValue(BigDecimal type);

    ParGlobalConfigFormValues save(ParGlobalConfigFormValues values);
}
