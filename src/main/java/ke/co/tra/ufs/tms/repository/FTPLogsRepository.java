/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.repository;

import java.math.BigDecimal;
import ke.co.tra.ufs.tms.entities.TmsFtpLogs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Owori Juma
 */
public interface FTPLogsRepository extends CrudRepository<TmsFtpLogs, BigDecimal> {

    /**
     *
     * @param terminalSerial
     * @param pg
     * @return
     */
    public Page<TmsFtpLogs> findByterminalSerial(String terminalSerial, Pageable pg);
    
}
