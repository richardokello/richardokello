package ke.tra.ufs.webportal.service.template;

import ke.tra.ufs.webportal.entities.ParGlobalConfigFormValues;
import ke.tra.ufs.webportal.repository.ParGlobalConfigFormValuesRepository;
import ke.tra.ufs.webportal.service.ParGlobalConfigFormValuesService;
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
