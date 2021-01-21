/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tracom.ufs.repositories;

import ke.tracom.ufs.entities.UfsAuthenticationType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

/**
 *
 * @author emuraya
 */
@Repository
public interface UfsAuthTypeRepository extends CrudRepository<UfsAuthenticationType, BigDecimal> {

    UfsAuthenticationType findByAuthenticationType(String authenticationType);

    UfsAuthenticationType findByTypeId(BigDecimal typeId);

}
