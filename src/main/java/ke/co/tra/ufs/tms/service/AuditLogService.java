/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import ke.co.tra.ufs.tms.entities.UfsAuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author Owori Juma
 */
public interface AuditLogService {

    
    /**
     *
     * @param id
     * @return
     */
    Optional<UfsAuditLog> findByTrailId(BigDecimal id);

}
