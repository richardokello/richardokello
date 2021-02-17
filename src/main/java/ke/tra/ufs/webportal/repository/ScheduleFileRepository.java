/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.repository;

import ke.tra.ufs.webportal.entities.TmsScheduleFile;
import ke.tra.ufs.webportal.entities.TmsScheduler;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Owori Juma
 */
public interface ScheduleFileRepository extends CrudRepository<TmsScheduleFile, BigDecimal> {

    /**
     *
     * @param scheduleId
     * @return
     */
    public List<TmsScheduleFile> findByscheduleId(TmsScheduler scheduleId);
}
