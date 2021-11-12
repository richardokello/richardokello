package ke.co.tra.ufs.tms.service;

import ke.co.tra.ufs.tms.entities.ParGlobalConfigIndices;
import ke.co.tra.ufs.tms.entities.ParMenuIndices;
import ke.co.tra.ufs.tms.entities.ParReceiptIndices;
import ke.co.tra.ufs.tms.entities.wrappers.ParameterCreateRequest;

import java.math.BigDecimal;
import java.util.List;

public interface ParameterIndexingService {

    List<BigDecimal> saveAllMenus(ParameterCreateRequest<ParMenuIndices> request);

    List<BigDecimal> saveAllConfigs(ParameterCreateRequest<ParGlobalConfigIndices> request);

    List<BigDecimal> saveAllReceipts(ParameterCreateRequest<ParReceiptIndices> request);
}
