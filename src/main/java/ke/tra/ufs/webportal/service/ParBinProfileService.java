package ke.tra.ufs.webportal.service;


import ke.tra.ufs.webportal.entities.ParBinProfile;

import java.math.BigDecimal;
import java.util.Optional;

public interface ParBinProfileService {
    public Optional<ParBinProfile> findById(BigDecimal id);
}
