package ke.co.tra.ufs.tms.service.templates;

import ke.co.tra.ufs.tms.entities.ParMenuProfile;
import ke.co.tra.ufs.tms.repository.ParMenuProfileRepository;
import ke.co.tra.ufs.tms.service.ParMenuProfileService;
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
