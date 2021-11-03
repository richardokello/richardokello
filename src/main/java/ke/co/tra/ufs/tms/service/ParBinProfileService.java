package ke.co.tra.ufs.tms.service;

import ke.co.tra.ufs.tms.entities.ParBinProfile;

import java.math.BigDecimal;
import java.util.Optional;

public interface ParBinProfileService {
    public Optional<ParBinProfile> findById(BigDecimal id);
}
