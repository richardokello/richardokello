/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import ke.co.tra.ufs.tms.entities.UfsAuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Owori Juma
 */
@Repository
public interface AuditLogRepository extends CrudRepository<UfsAuditLog, BigDecimal> {
    @Procedure(name = "IS_MAKER")
    boolean isInitiator(@Param("USER_ID_") Long userId, @Param("ENTITY_NAME_") String entityName,
                        @Param("ENTITY_ID_") String entityId, @Param("ACTIVITY_TYPE_") String activity, @Param("STATUS_") String status);
}
