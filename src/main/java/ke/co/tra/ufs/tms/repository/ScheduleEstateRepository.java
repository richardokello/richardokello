/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.repository;

import java.math.BigDecimal;
import java.util.List;
import ke.co.tra.ufs.tms.entities.TmsEstateItem;
import ke.co.tra.ufs.tms.entities.TmsScheduleEstate;
import ke.co.tra.ufs.tms.entities.TmsScheduler;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Owori Juma
 */
public interface ScheduleEstateRepository extends CrudRepository<TmsScheduleEstate, BigDecimal> {

    /**
     *
     * @param scheduleId
     * @return
     */
    public List<TmsScheduleEstate> findByscheduleId(TmsScheduler scheduleId);   
    
    /**
     *
     * @param scheduleId
     * @param unitItemId
     * @return
     */
    public List<TmsScheduleEstate> findByscheduleIdAndUnitItemId(TmsScheduler scheduleId, TmsEstateItem unitItemId);
}
