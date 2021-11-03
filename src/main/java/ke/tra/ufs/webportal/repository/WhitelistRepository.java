/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.tra.ufs.webportal.repository;


import ke.tra.ufs.webportal.entities.TmsWhitelist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author Cornelius M
 */
public interface WhitelistRepository extends CrudRepository<TmsWhitelist, BigDecimal> {
    /**
     * Filter whitelist by serial number and intrash
     *
     * @param serialNo
     * @param intrash
     * @return
     */
    public TmsWhitelist findByserialNoAndIntrash(String serialNo, String intrash);

    /**
     * Filter whitelist by assigned and intrash
     *
     * @param assigned
     * @param intrash
     * @return
     */
    public List<TmsWhitelist> findByAssignedAndIntrash(Short assigned, String intrash);

    /**
     * Filter whitelist
     *
     * @param actionStatus
     * @param modelId
     * @param from
     * @param to
     * @param needle       should be lowercase
     * @param intrash
     * @param pg
     * @return
     */
    @Query("SELECT u FROM #{#entityName} u WHERE u.actionStatus like ?1% "
            + "AND STR(COALESCE(u.modelId, -1)) LIKE ?2% AND u.creationDate BETWEEN ?3 AND ?4 AND "
            + "(lower(u.serialNo) LIKE %?5% ) AND lower(u.intrash) = lower(?6) AND u.serialNo like ?7% AND u.assignStr LIKE ?8%")
    public Page<TmsWhitelist> findAll(String actionStatus, String modelId, Date from, Date to, String needle, String intrash, String serialNo, String assignStr, Pageable pg);


}
