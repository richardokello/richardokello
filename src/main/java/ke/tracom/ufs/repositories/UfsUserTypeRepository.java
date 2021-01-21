/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tracom.ufs.repositories;

import ke.tracom.ufs.entities.UfsUserType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

/**
 *
 * @author emuraya
 */
@Repository
public interface UfsUserTypeRepository extends CrudRepository<UfsUserType, BigDecimal> {
    UfsUserType findByuserType(String type);

    UfsUserType findBytypeId(BigDecimal typeId);
    
}
