package ke.tra.ufs.webportal.service.template;

import ke.tra.ufs.webportal.entities.ParBinProfile;
import ke.tra.ufs.webportal.repository.ParBinProfileRepository;
import ke.tra.ufs.webportal.service.ParBinProfileService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@Transactional
public class ParBinProfileServiceTemplate implements ParBinProfileService {
    private final ParBinProfileRepository parBinProfileRepository;

    public ParBinProfileServiceTemplate(ParBinProfileRepository parBinProfileRepository) {
        this.parBinProfileRepository = parBinProfileRepository;
    }

    @Override
    public Optional<ParBinProfile> findById(BigDecimal id) {
        return parBinProfileRepository.findById(id);
    }
}
