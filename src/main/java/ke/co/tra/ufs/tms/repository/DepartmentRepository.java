/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.co.tra.ufs.tms.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import ke.co.tra.ufs.tms.entities.UfsDepartment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Cornelius M
 */
public interface DepartmentRepository extends CrudRepository<UfsDepartment, BigDecimal>{
    
    @Query("SELECT u FROM #{#entityName} u WHERE u.actionStatus LIKE ?1%"
            + " AND (u.departmentName LIKE %?2% OR u.description LIKE %?2%) AND lower(u.intrash) = lower(?3)"+
    "AND u.creationDate BETWEEN ?4 AND ?5")
    Page<UfsDepartment> findAll(String actionStatus, String needle, String intrash, Date from, Date to, Pageable pg);
   
    
    /**
     * Filter by intrash
     * @param intrash
     * @return 
     */
    public List<UfsDepartment> findByintrash(String intrash);
    /**
     * Fetch by department id and intrash
     * @param id
     * @param intrash
     * @return 
     */
    public UfsDepartment findByIdAndIntrash(BigDecimal id, String intrash);
    /**
     * Filter by name and intrash
     * @param departmentName
     * @param intrash
     * @return 
     */
    public UfsDepartment findBydepartmentNameIgnoreCaseAndIntrash(String departmentName, String intrash);

}
