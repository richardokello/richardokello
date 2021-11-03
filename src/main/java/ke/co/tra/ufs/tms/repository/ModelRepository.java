/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.co.tra.ufs.tms.repository;

import java.math.BigDecimal;
import ke.co.tra.ufs.tms.entities.UfsDeviceModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Cornelius M
 */
public interface ModelRepository extends CrudRepository<UfsDeviceModel, BigDecimal>{
    
    @Query("SELECT u FROM #{#entityName} u WHERE u.actionStatus LIKE ?1% AND STR(COALESCE(u.makeId, -1)) LIKE ?2% AND "
            + "STR(COALESCE(u.deviceTypeId, -1)) LIKE ?3% AND (u.model LIKE %?4% OR u.description LIKE %?4%) AND lower(u.intrash) = lower(?5)")
    Page<UfsDeviceModel> findAll(String actionStatus, String makeId, String deviceTypeId, String needle, String intrash, Pageable pg);

    /**
     *
     * @param modelId
     * @param intrash
     * @return
     */
    UfsDeviceModel findBymodelIdAndIntrash(BigDecimal modelId, String intrash);

    /**
     *
     * @param model
     * @param intrash
     * @return
     */
    UfsDeviceModel findByModelAndIntrash(String model, String intrash);
}
