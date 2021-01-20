/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.service.templates;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import ke.co.tra.ufs.tms.entities.TmsDevice;
import ke.co.tra.ufs.tms.entities.TmsDeviceParam;
import ke.co.tra.ufs.tms.entities.TmsParamDefinition;
import ke.co.tra.ufs.tms.entities.UfsProduct;
import ke.co.tra.ufs.tms.repository.TmsDeviceParamRepository;
import ke.co.tra.ufs.tms.repository.TmsParamDefinitionRepository;
import ke.co.tra.ufs.tms.service.TmsDeviceParamService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Owori Juma
 */
@Service
@Transactional
public class TmsDeviceParamServiceTemplate implements TmsDeviceParamService {

    private final TmsParamDefinitionRepository paramDefinitionRepository;
    private final TmsDeviceParamRepository deviceParamRepository;

    public TmsDeviceParamServiceTemplate(TmsParamDefinitionRepository paramDefinitionRepository, TmsDeviceParamRepository deviceParamRepository) {
        this.paramDefinitionRepository = paramDefinitionRepository;
        this.deviceParamRepository = deviceParamRepository;
    }

    @Override
    public List<TmsParamDefinition> getDefinitions(UfsProduct productId) {
        return paramDefinitionRepository.findByproductId(productId);
    }

    @Override
    public TmsDeviceParam saveDeviceParam(TmsDeviceParam deviceParam) {
        return deviceParamRepository.save(deviceParam);
    }

    @Override
    public Optional<TmsParamDefinition> findTmsParamDefinition(BigDecimal paramDefId) {
        return paramDefinitionRepository.findById(paramDefId);
    }

    @Override
    public TmsParamDefinition saveParam(TmsParamDefinition paramDefinition) {
        return paramDefinitionRepository.save(paramDefinition);
    }

    @Override
    public TmsDeviceParam findByDeviceId(TmsDevice deviceId) {
        return deviceParamRepository.findBydeviceId(deviceId);
    }

}
