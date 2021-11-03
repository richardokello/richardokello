/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.service.templates;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import ke.co.tra.ufs.tms.entities.TmsApp;
import ke.co.tra.ufs.tms.entities.TmsDeviceFileExt;
import ke.co.tra.ufs.tms.entities.UfsDeviceModel;
import ke.co.tra.ufs.tms.repository.AppManagementRepository;
import ke.co.tra.ufs.tms.repository.ModelRepository;
import ke.co.tra.ufs.tms.service.AppManagementService;
import ke.co.tra.ufs.tms.service.FileExtensionRepository;
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
public class AppManagementServiceTemplate implements AppManagementService {

    private final AppManagementRepository appManagementRepository;
    private final ModelRepository modelRepository;
    private final FileExtensionRepository fileExtRepo;

    public AppManagementServiceTemplate(AppManagementRepository appManagementRepository, ModelRepository modelRepository,
            FileExtensionRepository fileExtRepo) {
        this.appManagementRepository = appManagementRepository;
        this.modelRepository = modelRepository;
        this.fileExtRepo = fileExtRepo;
    }

    @Override
    public Page<TmsApp> findApps(UfsDeviceModel modelId, Pageable pg) {
        return appManagementRepository.findBymodelIdAndIntrash(modelId, AppConstants.NO, pg);
    }

    @Override
    public UfsDeviceModel findDeviceModel(BigDecimal modelId) {
        return modelRepository.findBymodelIdAndIntrash(modelId, AppConstants.NO);
    }

    @Override
    public Optional<TmsApp> findTmsApp(BigDecimal appId) {
        return appManagementRepository.findById(appId);
    }

    @Override
    public List<TmsDeviceFileExt> findExtension(String ext, BigDecimal modelId
    ) {
        return this.fileExtRepo.findByappFileExtAndModelId(ext, modelId);
    }

    @Override
    public Page<TmsApp> getApplications(String actionStatus, Date from,
            Date to, String productId,
            String modelId, String needle,
            Pageable pg
    ) {
        return appManagementRepository.findAll(actionStatus, from, to, productId, modelId, needle.toLowerCase(), AppConstants.NO, pg);
    }

    @Override
    public TmsApp saveTmsApp(TmsApp app) {
        return appManagementRepository.save(app);
    }

    @Override
    public TmsApp findTmsAppByAppName(String appName) {
        return appManagementRepository.findTmsAppByAppNameAndIntrash(appName,AppConstants.NO);
    }

    @Override
    public TmsApp findTmsAppById(BigDecimal appId) {
        return appManagementRepository.findByAppId(appId);
    }


}
