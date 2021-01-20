/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.service.templates;

import java.math.BigDecimal;
import java.util.Optional;

import ke.co.tra.ufs.tms.entities.UfsMno;
import ke.co.tra.ufs.tms.repository.MNORepository;
import ke.co.tra.ufs.tms.service.MNOService;
import ke.co.tra.ufs.tms.utils.AppConstants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Owori Juma
 */
@Service
@Transactional
public class MNOServiceTemplate implements MNOService {

    private final MNORepository mNORepository;

    public MNOServiceTemplate(MNORepository mNORepository) {
        this.mNORepository = mNORepository;
    }

    @Override
    public UfsMno findByMnoName(String mnoName) {
        return mNORepository.findBymnoName(mnoName);
    }

    @Override
    public UfsMno saveMno(UfsMno mno) {
        return mNORepository.save(mno);
    }

    @Override
    public Page<UfsMno> findByintrash(String intrash, Pageable pg) {
        return mNORepository.findByintrash(intrash, pg);
    }

    @Override
    public Optional<UfsMno> findMno(BigDecimal mnoId) {
        return mNORepository.findById(mnoId);
    }

    @Override
    public Page<UfsMno> fetchMnosExclude(String actionStatus, Pageable pg) {
        return mNORepository.findAll(actionStatus, AppConstants.NO, pg);
    }

}
