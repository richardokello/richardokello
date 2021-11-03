/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.service.templates;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import ke.co.tra.ufs.tms.entities.UfsAuditLog;
import ke.co.tra.ufs.tms.repository.AuditLogRepository;
import ke.co.tra.ufs.tms.service.AuditLogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Owori Juma
 */
@Service
@Transactional
public class AuditLogServiceTemplate implements AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogServiceTemplate(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }






    @Override
    public Optional<UfsAuditLog> findByTrailId(BigDecimal id) {
        return auditLogRepository.findById(id);
    }

}
