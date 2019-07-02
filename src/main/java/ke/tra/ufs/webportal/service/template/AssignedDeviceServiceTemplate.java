/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.service.template;

import java.util.List;
import ke.tra.ufs.webportal.entities.UfsAssignedSimdetails;
import ke.tra.ufs.webportal.repository.UfsAssignedSimdetailsRepository;
import ke.tra.ufs.webportal.service.AssignDeviceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Kenny
 */
@Service
@Transactional
public class AssignedDeviceServiceTemplate implements AssignDeviceService{
    
    private final UfsAssignedSimdetailsRepository assignedSimdetailsRepository;
    
    
    public AssignedDeviceServiceTemplate(UfsAssignedSimdetailsRepository assignedSimdetailsRepository) {
        this.assignedSimdetailsRepository = assignedSimdetailsRepository;
    }

    @Override
    public void saveAllSimDetails(List<UfsAssignedSimdetails> assignedSimDetails) {
        assignedSimdetailsRepository.saveAll(assignedSimDetails);
    }

    
  
    
}
