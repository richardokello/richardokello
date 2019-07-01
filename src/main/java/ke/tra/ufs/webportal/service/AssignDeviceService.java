/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.service;

import java.util.List;
import ke.tra.ufs.webportal.entities.UfsAssignedSimdetails;

/**
 *
 * @author Tracom
 */
public interface AssignDeviceService {
    
     void saveAllSimDetails(List<UfsAssignedSimdetails> assignedSimDetails);
}
