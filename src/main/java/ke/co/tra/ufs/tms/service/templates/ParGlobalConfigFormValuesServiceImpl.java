package ke.co.tra.ufs.tms.service.templates;

import ke.co.tra.ufs.tms.entities.ParGlobalConfigFormValues;
import ke.co.tra.ufs.tms.repository.ParGlobalConfigFormValuesRepository;
import ke.co.tra.ufs.tms.service.ParGlobalConfigFormValuesService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
}
