/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.repository;

import java.math.BigDecimal;
import java.util.List;
import ke.co.tra.ufs.tms.entities.UfsSysConfig;
import ke.co.tra.ufs.tms.entities.wrappers.UfsSysConfigWrapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Owori Juma
 */
@Repository
public interface ConfigRepository extends CrudRepository<UfsSysConfig, BigDecimal> {

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
    @Query("SELECT new ke.co.tra.ufs.tms.entities.wrappers.UfsSysConfigWrapper(u.id, "
            + "u.entity, u.parameter, u.value, u.description, u.action, u.actionStatus, u.valueType) "
            + "FROM UfsSysConfig u WHERE u.entity = ?1")
    List<UfsSysConfigWrapper> findByentity(String entity);

    @Query("SELECT new ke.co.tra.ufs.tms.entities.wrappers.UfsSysConfigWrapper(u.id, "
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
