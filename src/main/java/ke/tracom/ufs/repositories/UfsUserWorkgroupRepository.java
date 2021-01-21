/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tracom.ufs.repositories;

import ke.tracom.ufs.entities.UfsUserWorkgroup;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author eli.muraya
 */
@Repository
public interface UfsUserWorkgroupRepository extends CrudRepository<UfsUserWorkgroup, BigDecimal> {

    @Modifying
    @Transactional
    @Query(value="delete from UfsUserWorkgroup u where u.userId = ?1")
    void deleteAllByUserId(Long userId);

    List<UfsUserWorkgroup> findAllByUserId(Long userId);

}
