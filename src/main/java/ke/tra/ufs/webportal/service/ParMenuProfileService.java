package ke.tra.ufs.webportal.service;

import ke.tra.ufs.webportal.entities.ParMenuProfile;

import java.math.BigDecimal;
import java.util.Optional;

public interface ParMenuProfileService {

   public Optional<ParMenuProfile> findById(BigDecimal id);
}
