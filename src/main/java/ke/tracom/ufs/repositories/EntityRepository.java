/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tracom.ufs.repositories;

import ke.tracom.ufs.entities.UfsEntity;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author eli.muraya
 */
public interface EntityRepository extends CrudRepository<UfsEntity, Short> {
    
}
