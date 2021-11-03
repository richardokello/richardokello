package ke.tra.ufs.webportal.service.template;


import ke.tra.ufs.webportal.entities.UfsGeographicalRegion;
import ke.tra.ufs.webportal.repository.UfsGeographicalRegionRepository;
import ke.tra.ufs.webportal.service.GeographicalRegionService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class GeographicalRegionServiceTempl implements GeographicalRegionService {

    private final UfsGeographicalRegionRepository geographicalRegionRepository;

    public GeographicalRegionServiceTempl(UfsGeographicalRegionRepository geographicalRegionRepository) {
        this.geographicalRegionRepository = geographicalRegionRepository;
    }

    @Override
    public UfsGeographicalRegion findByGeographicalId(BigDecimal id) {
        return this.geographicalRegionRepository.findByGeographicalId(id);
    }
}
