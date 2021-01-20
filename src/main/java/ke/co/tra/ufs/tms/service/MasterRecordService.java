/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.co.tra.ufs.tms.service;

import java.math.BigDecimal;
import java.util.Date;

import ke.co.tra.ufs.tms.entities.UfsDepartment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author Cornelius M
 */
public interface MasterRecordService {
    
    /**
     * Save department
     * @param department
     * @return 
     */
    public UfsDepartment saveDepartment(UfsDepartment department);    
    /**
     * Get single department using id
     * @param id
     * @return 
     */
    public UfsDepartment getDepartment(BigDecimal id);
    /**
     * Fetch department by name
     * @param departmentName
     * @return 
     */
    public UfsDepartment getDepartment(String departmentName);

    /**
     *
     * @param actionStatus
     * @param needle
     * @param pg
     * @return
     */
    public Page<UfsDepartment> getDepartments(String actionStatus, String needle, Date from, Date to, Pageable pg);
    

}
