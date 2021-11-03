/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.repository;

import java.math.BigDecimal;
import java.util.List;
import ke.co.tra.ufs.tms.entities.UfsModifiedRecord;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Owori Juma
 */
public interface ModifiedRecordRepository extends CrudRepository<UfsModifiedRecord, BigDecimal> {
    /**
     * Used to delete record by entity and entityId     * 
     * @param ufsEntity
     * @param entityId 
     */
    //@Query("DELETE FROM UfsModifiedRecord u WHERE AND u.ufsEntity = ?1 AND u.entityId = ?2")
    public void deleteByUfsEntityAndEntityId(String ufsEntity, String entityId);
    /**
     * Used to find record by entity and entityId
     * @param ufsEntity
     * @param entityId
     * @return 
     */
    //@Query("SELECT u FROM UfsModifiedRecord u WHERE AND u.ufsEntity = ?1 AND u.entityId = ?2")
    public UfsModifiedRecord findByUfsEntityAndEntityId(String ufsEntity, String entityId);
    /**
     * Search entity by value key 
     * @param ufsEntity
     * @param values
     * @return 
     */
    //@Query("SELECT u FROM UfsModifiedRecord u WHERE AND u.ufsEntity = ?1 AND u.values = ?2")
    public List<UfsModifiedRecord> findByUfsEntityAndValuesContaining(String ufsEntity, String values);
    
}
