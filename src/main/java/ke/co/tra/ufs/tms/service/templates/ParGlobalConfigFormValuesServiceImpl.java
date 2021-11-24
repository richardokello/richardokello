package ke.co.tra.ufs.tms.service.templates;

import ke.axle.chassis.utils.AppConstants;
import ke.co.tra.ufs.tms.entities.ParGlobalConfigFormValues;
import ke.co.tra.ufs.tms.repository.ParGlobalConfigFormValuesRepository;
import ke.co.tra.ufs.tms.service.ParGlobalConfigFormValuesService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

@Service
public class ParGlobalConfigFormValuesServiceImpl implements ParGlobalConfigFormValuesService {
    private final ParGlobalConfigFormValuesRepository parGlobalConfigFormValuesRepository;

    public ParGlobalConfigFormValuesServiceImpl(ParGlobalConfigFormValuesRepository parGlobalConfigFormValuesRepository) {
        this.parGlobalConfigFormValuesRepository = parGlobalConfigFormValuesRepository;
    }

    @Override
    public Optional<ParGlobalConfigFormValues> findFormValue(BigDecimal id) {
        return parGlobalConfigFormValuesRepository.findById(id);
    }

    @Override
    public ParGlobalConfigFormValues save(ParGlobalConfigFormValues values) {
        return parGlobalConfigFormValuesRepository.save(values);
    }

    @Override
    public Page<ParGlobalConfigFormValues> getConfigFormByConfigType(String actionStatus, String configTypeId, Date from, Date to, String needle, Pageable pg) {
        return parGlobalConfigFormValuesRepository.findAllConfigFormsByConfigType(actionStatus,configTypeId,from,to,needle, AppConstants.NO,pg);
    }
}
