/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.repository;


import ke.tra.ufs.webportal.entities.UfsSysConfig;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

/**
 *
 * @author eli.muraya
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

}
