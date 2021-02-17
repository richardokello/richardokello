/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.repository;

import ke.tra.ufs.webportal.entities.TmsDeviceFileExt;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;

/**
 *
 * @author ojuma
 */
public interface TmsDeviceFileExtRepository extends CrudRepository<TmsDeviceFileExt, BigDecimal>{
    
    /**
     *
     * @param modelId
     * @return
     */
    public TmsDeviceFileExt findBymodelId(BigDecimal modelId);
}
