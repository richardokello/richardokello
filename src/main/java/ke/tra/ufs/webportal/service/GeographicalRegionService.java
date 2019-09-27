package ke.tra.ufs.webportal.service;

import ke.tra.ufs.webportal.entities.UfsGeographicalRegion;

import java.math.BigDecimal;

public interface GeographicalRegionService {

    UfsGeographicalRegion findByGeographicalId(BigDecimal id);
}
