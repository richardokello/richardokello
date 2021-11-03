package ke.co.tra.ufs.tms.service.templates;

import ke.co.tra.ufs.tms.entities.ParDeviceSelectedOptions;
import ke.co.tra.ufs.tms.repository.ParDeviceSelectedOptionsRepository;
import ke.co.tra.ufs.tms.service.ParDeviceSelectedOptionsService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ParDeviceSelectedOptionsServiceTemplate implements ParDeviceSelectedOptionsService {

    private final ParDeviceSelectedOptionsRepository parDeviceSelectedOptionsRepository;

    public ParDeviceSelectedOptionsServiceTemplate(ParDeviceSelectedOptionsRepository parDeviceSelectedOptionsRepository) {
        this.parDeviceSelectedOptionsRepository = parDeviceSelectedOptionsRepository;
    }

    @Override
    public ParDeviceSelectedOptions save(ParDeviceSelectedOptions selectedOption) {
        return parDeviceSelectedOptionsRepository.save(selectedOption);
    }

    @Override
    public void deleteAll(BigDecimal deviceId) {
        parDeviceSelectedOptionsRepository.deleteAllByDeviceId(deviceId);
    }

    @Override
    public Iterable<ParDeviceSelectedOptions> saveAll(Iterable<ParDeviceSelectedOptions> selectedOptions) {
        return parDeviceSelectedOptionsRepository.saveAll(selectedOptions);
    }
}
