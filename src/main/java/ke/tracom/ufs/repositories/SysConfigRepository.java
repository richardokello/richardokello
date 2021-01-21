/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tracom.ufs.repositories;

import ke.tracom.ufs.entities.UfsSysConfig;
import ke.tracom.ufs.entities.wrapper.UfsSysConfigWrapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;
import java.util.List;

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


    /**
     * used to load configuration by parameter and entity
     *
     * @param entity
     * @param parameter
     * @return
     */
    @Query("SELECT u FROM UfsSysConfig u WHERE u.entity = ?1 AND u.parameter = ?2")
    UfsSysConfig findByentityAndParameter(String entity, String parameter);

    /**
     * Used to fetch configurations by entity
     *
     * @param entity
     * @return
     */
    @Query("SELECT new ke.tracom.ufs.entities.wrapper.UfsSysConfigWrapper(u.id, "
            + "u.entity, u.parameter, u.value, u.description, u.action, u.actionStatus, u.valueType) "
            + "FROM UfsSysConfig u WHERE u.entity = ?1")
    List<UfsSysConfigWrapper> findByentity(String entity);

    @Query("SELECT new ke.tracom.ufs.entities.wrapper.UfsSysConfigWrapper(u.id, "
            + "u.entity, u.parameter, u.value, u.description, u.action, u.actionStatus, u.valueType) FROM UfsSysConfig u WHERE u.entity = ?1 AND u.parameter in ?2")
    List<UfsSysConfigWrapper> findByentityAndParameterIn(String entity, List<String> parameters);
    /**
     * Filter system configuration
     * @param actionStatus
     * @param entity should be in lowercase
     * @param needle should be in lowercase
     * @param pg
     * @return
     */
    @Query("SELECT u FROM #{#entityName} u WHERE u.actionStatus LIKE ?1% AND LOWER(u.entity) LIKE ?2% AND "
            + "(LOWER(u.parameter) LIKE %?3% OR LOWER(u.value) LIKE %?3% OR LOWER(u.description) LIKE %?3%)")
    Page<UfsSysConfig> findAll(String actionStatus, String entity, String needle, Pageable pg);

}
