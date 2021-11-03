package ke.co.tra.ufs.tms.service;

import ke.co.tra.ufs.tms.entities.ParMenuIndices;
import ke.co.tra.ufs.tms.entities.wrappers.ParameterCreateRequest;

import java.math.BigDecimal;
import java.util.List;

public interface ParConfigIndexingService {
    List<BigDecimal> saveAllMenus(ParameterCreateRequest<ParMenuIndices> request);
}
