/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.co.tra.ufs.tms.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import ke.co.tra.ufs.tms.entities.UfsDeviceMake;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Cornelius M
 */
public interface MakeRepository extends CrudRepository<UfsDeviceMake, BigDecimal>{
    /**
     * Fetch device make by make id and intrash
     * @param makeId
     * @param intrash
     * @return 
     */
    public UfsDeviceMake findBymakeIdAndIntrash(BigDecimal makeId, String intrash);
    /**
     * Filter device make by actioin status date and search key
     * @param actionStatus
     * @param pg
     * @param intrash
     * @param needle
     * @return 
     */
    @Query("SELECT u FROM UfsDeviceMake u WHERE u.actionStatus LIKE ?1% AND (u.vendorName LIKE %?2% OR u.make LIKE %?2%) "
            + "AND lower(u.intrash) = lower(?3)")
    public Page<UfsDeviceMake> findAll(String actionStatus, String needle, String intrash, Pageable pg);

    List<UfsDeviceMake> findAllByVendorNameAndIntrash(String make,String intrash);

}
