/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tracom.ufs.repositories;

import ke.tracom.ufs.entities.UfsSysConfig;
import ke.tracom.ufs.entities.wrapper.UfsSysConfigWrapper;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author kmwangi
 */
@Repository
public interface UfsSysConfigRepository extends CrudRepository<UfsSysConfig, BigDecimal> {

    @Query("SELECT sys FROM UfsSysConfig sys WHERE sys.entity =?1 AND sys.parameter =?2 ")
    UfsSysConfig passwordExpiry(String entity, String parameter);

    @Query("SELECT sys FROM UfsSysConfig sys WHERE sys.entity =?1 AND sys.parameter =?2 ")
    UfsSysConfig passwordLowercase(String entity, String parameter);

    @Query("SELECT sys FROM UfsSysConfig sys WHERE sys.entity =?1 AND sys.parameter =?2 ")
    UfsSysConfig passwordUppercase(String entity, String parameter);

    @Query("SELECT sys FROM UfsSysConfig sys WHERE sys.entity =?1 AND sys.parameter =?2 ")
    UfsSysConfig getConfiguration(String entity, String parameter);
    
    @Query("SELECT sys.value FROM UfsSysConfig sys WHERE sys.entity =?1 AND sys.parameter =?2 ")
    String uploadDir(String entity, String parameter);

    /**
     * Used to fetch configurations by entity
     * @param entity
     * @return
     */
    @Query("SELECT new ke.tracom.ufs.entities.wrapper.UfsSysConfigWrapper(u.id, "
            + "u.entity, u.parameter, u.value, u.description, u.action, u.actionStatus, u.valueType) "
            + "FROM UfsSysConfig  u WHERE u.entity = ?1")
    List<UfsSysConfigWrapper> findByEntity(String entity);

}
