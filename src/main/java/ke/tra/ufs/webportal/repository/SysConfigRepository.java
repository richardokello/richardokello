/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.repository;

import java.math.BigDecimal;

import ke.tra.ufs.webportal.entities.UfsSysConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Owori Juma
 */
public interface SysConfigRepository extends CrudRepository<UfsSysConfig, BigDecimal> {

    /**
     * Used to fetch all System Configs using the specified Pagination
     *
     * @param pg
     * @return
     */
    Page<UfsSysConfig> findAll(Pageable pg);

    /**
     * Used to fetch System Config by Entity Name and Parameter
     *
     * @param entity
     * @param parameter
     * @return
     */
    UfsSysConfig findByEntityAndParameterAllIgnoreCase(String entity, String parameter);

    /**
     * Used to fetch System Config by Entity Name but Entity Parameter should
     * not exist
     *
     * @param id
     * @param entity
     * @param parameter
     * @return
     */
    @Query("select u from UfsSysConfig u where u.id != ?1 AND u.entity = ?2 AND u.parameter = ?3 ")
    UfsSysConfig findByEntityAndParameter(BigDecimal id, String entity, String parameter);

    /**
     * Used to fetch configurations not including the specified entity
     *
     * @param entity
     * @param pg
     * @return
     */
    Page<UfsSysConfig> findByentityNot(String entity, Pageable pg);

    /**
     * Used to fetch configurations using entity
     *
     * @param entity
     * @param pg
     * @return
     */
    Page<UfsSysConfig> findByentityIgnoreCase(String entity, Pageable pg);

    /**
     * Used to fetch configurations using entity and excluding the specified
     * entity
     *
     * @param entity
     * @param excludeEntity
     * @param pg
     * @return
     */
    Page<UfsSysConfig> findByentityIgnoreCaseAndEntityNot(String entity, String excludeEntity, Pageable pg);

    /**
     * Used to search for configurations
     *
     * @param needle should be lowercase
     * @param pg
     * @return
     */
    @Query("SELECT c FROM UfsSysConfig c WHERE lower(c.entity) LIKE %?1% OR lower(c.parameter) LIKE %?1% OR "
            + "lower(c.value) LIKE %?1% OR lower(c.description) LIKE %?1% OR lower(c.action) LIKE %?1% OR  "
            + "lower(c.actionStatus) LIKE %?1%")
    Page<UfsSysConfig> searchConfigs(String needle, Pageable pg);

}
