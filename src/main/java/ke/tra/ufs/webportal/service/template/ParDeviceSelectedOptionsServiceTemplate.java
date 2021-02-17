package ke.tra.ufs.webportal.service.template;

import ke.tra.ufs.webportal.entities.ParDeviceSelectedOptions;
import ke.tra.ufs.webportal.repository.ParDeviceSelectedOptionsRepository;
import ke.tra.ufs.webportal.service.ParDeviceSelectedOptionsService;
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
