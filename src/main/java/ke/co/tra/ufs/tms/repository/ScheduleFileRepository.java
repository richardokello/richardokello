/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.repository;

import java.math.BigDecimal;
import java.util.List;
import ke.co.tra.ufs.tms.entities.TmsScheduleFile;
import ke.co.tra.ufs.tms.entities.TmsScheduler;
import org.springframework.data.repository.CrudRepository;

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
