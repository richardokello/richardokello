/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.repository;

import ke.tra.ufs.webportal.entities.UfsAssignedDevice;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Tracom
 */
@Repository
public interface UfsAssignDeviceRepository  extends CrudRepository<UfsAssignedDevice, Long>{
    
}
