/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.service.templates;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import ke.co.tra.ufs.tms.entities.UfsDepartment;
import ke.co.tra.ufs.tms.repository.DepartmentRepository;
import ke.co.tra.ufs.tms.service.MasterRecordService;
import ke.co.tra.ufs.tms.utils.AppConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Cornelius M
 */
@Service
@Transactional
@Slf4j
public class MasterRecordServiceTemplate implements MasterRecordService {

    private final DepartmentRepository departmentRepo;

    public MasterRecordServiceTemplate(DepartmentRepository departmentRepo) {
        this.departmentRepo = departmentRepo;
    }

    @Override
    public UfsDepartment saveDepartment(UfsDepartment department) {
        return departmentRepo.save(department);
    }

    @Override
    public UfsDepartment getDepartment(BigDecimal id) {
        return departmentRepo.findByIdAndIntrash(id, AppConstants.NO);
    }

    @Override
    public UfsDepartment getDepartment(String departmentName) {
        return departmentRepo.findBydepartmentNameIgnoreCaseAndIntrash(departmentName, AppConstants.NO);
    }

    @Override
    public Page<UfsDepartment> getDepartments(String actionStatus, String needle, Date from, Date to, Pageable pg) {
        log.error("From {}", from);
        log.error("to {}", to);
        return departmentRepo.findAll(actionStatus, needle, AppConstants.NO, from, to, pg);
    }

}
