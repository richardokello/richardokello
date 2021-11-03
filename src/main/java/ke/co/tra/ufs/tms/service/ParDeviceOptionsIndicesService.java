package ke.co.tra.ufs.tms.service;

import ke.co.tra.ufs.tms.entities.ParDeviceOptionsIndices;

import java.math.BigDecimal;
import java.util.List;

public interface ParDeviceOptionsIndicesService {
    List<BigDecimal> saveAll(List<ParDeviceOptionsIndices> indices);
}
