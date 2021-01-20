/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.repository;

import java.math.BigDecimal;
import ke.co.tra.ufs.tms.entities.DeviceCustomerDetails;
import ke.co.tra.ufs.tms.entities.TmsDevice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author ojuma
 */
public interface DeviceCustomerDetailsRepository extends CrudRepository<DeviceCustomerDetails, BigDecimal>{
    
    
    @Query("SELECT u FROM #{#entityName} u WHERE u.actionStatus LIKE ?1%"
            + " AND (u.agentMerchantId LIKE %?2%) AND lower(u.instrash) = lower(?3)")
    Page<DeviceCustomerDetails> findAll(String actionStatus, String needle, String instrash, Pageable pg);
    
    /**
     *
     * @param agentMerchantId
     * @param instrash
     * @return
     */
    public DeviceCustomerDetails findByAgentMerchantIdAndInstrash(String agentMerchantId,String instrash);    
}
