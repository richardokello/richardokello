/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.repository;

import ke.tra.ufs.webportal.entities.TmsEstateItem;
import ke.tra.ufs.webportal.entities.TmsScheduleEstate;
import ke.tra.ufs.webportal.entities.TmsScheduler;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;
import java.util.List;

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
