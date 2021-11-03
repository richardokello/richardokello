/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.repository;

import java.math.BigDecimal;
import ke.co.tra.ufs.tms.entities.TmsDeviceFileExt;
import org.springframework.data.repository.CrudRepository;

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
