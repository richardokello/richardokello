package ke.co.tra.ufs.tms.service.templates;

import ke.co.tra.ufs.tms.entities.ParBinProfile;
import ke.co.tra.ufs.tms.repository.ParBinProfileRepository;
import ke.co.tra.ufs.tms.service.ParBinProfileService;
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
