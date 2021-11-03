package ke.tra.ufs.webportal.service;

import ke.tra.ufs.webportal.entities.ParGlobalConfigIndices;
import ke.tra.ufs.webportal.entities.ParMenuIndices;
import ke.tra.ufs.webportal.entities.wrapper.ParameterCreateRequest;

import java.math.BigDecimal;
import java.util.List;

public interface ParameterIndexingService {

    List<BigDecimal> saveAllMenus(ParameterCreateRequest<ParMenuIndices> request);

    List<BigDecimal> saveAllConfigs(ParameterCreateRequest<ParGlobalConfigIndices> request);
}
