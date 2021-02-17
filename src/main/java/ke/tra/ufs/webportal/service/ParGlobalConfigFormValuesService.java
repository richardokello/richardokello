package ke.tra.ufs.webportal.service;


import ke.tra.ufs.webportal.entities.ParGlobalConfigFormValues;

import java.math.BigDecimal;
import java.util.Optional;

public interface ParGlobalConfigFormValuesService {
    Optional<ParGlobalConfigFormValues> findFormValue(BigDecimal type);

    ParGlobalConfigFormValues save(ParGlobalConfigFormValues values);
}
