package ke.co.tra.ufs.tms.service;

import ke.co.tra.ufs.tms.entities.ParMenuProfile;

import java.math.BigDecimal;
import java.util.Optional;

public interface ParMenuProfileService {

   public Optional<ParMenuProfile> findById(BigDecimal id);
}
