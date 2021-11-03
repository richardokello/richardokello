package ke.tra.ufs.webportal.service.template;

import ke.tra.ufs.webportal.entities.ParMenuProfile;
import ke.tra.ufs.webportal.repository.ParMenuProfileRepository;
import ke.tra.ufs.webportal.service.ParMenuProfileService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@Transactional
public class ParMenuProfileServiceTemplate implements ParMenuProfileService {
    private final ParMenuProfileRepository parMenuProfileRepository;

    public ParMenuProfileServiceTemplate(ParMenuProfileRepository parMenuProfileRepository) {
        this.parMenuProfileRepository = parMenuProfileRepository;
    }

    @Override
    public Optional<ParMenuProfile> findById(BigDecimal id) {
        return parMenuProfileRepository.findById(id);
    }
}
