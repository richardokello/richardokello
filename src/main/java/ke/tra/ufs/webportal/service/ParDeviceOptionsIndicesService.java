package ke.tra.ufs.webportal.service;


import ke.tra.ufs.webportal.entities.ParDeviceOptionsIndices;

import java.math.BigDecimal;
import java.util.List;

public interface ParDeviceOptionsIndicesService {
    List<BigDecimal> saveAll(List<ParDeviceOptionsIndices> indices);
}
