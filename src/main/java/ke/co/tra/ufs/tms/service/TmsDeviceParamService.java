/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import ke.co.tra.ufs.tms.entities.TmsDevice;
import ke.co.tra.ufs.tms.entities.TmsDeviceParam;
import ke.co.tra.ufs.tms.entities.TmsParamDefinition;
import ke.co.tra.ufs.tms.entities.UfsProduct;

/**
 *
 * @author Owori Juma
 */
public interface TmsDeviceParamService {

    /**
     *
     * @param productId
     * @return
     */
    public List<TmsParamDefinition> getDefinitions(UfsProduct productId);

    /**
     *
     * @param deviceParam
     * @return
     */
    public TmsDeviceParam saveDeviceParam(TmsDeviceParam deviceParam);
    
    /**
     *
     * @param deviceId
     * @return
     */
    public TmsDeviceParam findByDeviceId(TmsDevice deviceId);

    /**
     *
     * @param paramDefId
     * @return
     */
    public Optional<TmsParamDefinition> findTmsParamDefinition(BigDecimal paramDefId);

    /**
     *
     * @param paramDefinition
     * @return
     */
    public TmsParamDefinition saveParam(TmsParamDefinition paramDefinition);
}
