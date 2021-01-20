/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import ke.co.tra.ufs.tms.entities.UfsMno;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author Owori Juma
 */
public interface MNOService {

    /**
     *
     * @param mnoName
     * @return
     */
    public UfsMno findByMnoName(String mnoName);

    /**
     *
     * @param mno
     * @return
     */
    public UfsMno saveMno(UfsMno mno);

    /**
     *
     * @param intrash
     * @param pg
     * @return
     */
    public Page<UfsMno> findByintrash(String intrash, Pageable pg);

    /**
     *
     * @param mnoId
     * @return
     */
    public Optional<UfsMno> findMno(BigDecimal mnoId);

    /**
     *
     * @param actionStatus
     * @param pg
     * @return
     */
    public Page<UfsMno> fetchMnosExclude(String actionStatus, Pageable pg);
}
