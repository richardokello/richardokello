/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.repository;

import java.math.BigDecimal;
import java.util.Date;
import ke.co.tra.ufs.tms.entities.TmsUpdateLogs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author ojuma
 */
public interface TmsUpdateLogsRepository  extends CrudRepository<TmsUpdateLogs, BigDecimal>{
    
    @Query("SELECT u FROM #{#entityName} u WHERE u.status LIKE ?1% AND STR(COALESCE(u.tmsDeviceId, -1)) LIKE ?4% AND "
            + "STR(COALESCE(u.taskId, -1)) LIKE ?5% AND u.dateTimeAdded BETWEEN ?2 AND ?3")
    public Page<TmsUpdateLogs> findAll(String status, Date from, Date to, String tmsDeviceId, String taskId,Pageable pg);
    
}
