package ke.tra.ufs.webportal.service.template;

import ke.axle.chassis.utils.AppConstants;
import ke.axle.chassis.utils.LoggerService;
import ke.tra.ufs.webportal.entities.ParGlobalConfig;
import ke.tra.ufs.webportal.entities.ParGlobalConfigIndices;
import ke.tra.ufs.webportal.entities.ParGlobalConfigProfile;
import ke.tra.ufs.webportal.entities.wrapper.GlobalConfigFileRequest;
import ke.tra.ufs.webportal.repository.ParGlobalConfigIndexingRepository;
import ke.tra.ufs.webportal.repository.ParGlobalConfigProfileRepository;
import ke.tra.ufs.webportal.repository.ParGlobalConfigRepository;
import ke.tra.ufs.webportal.repository.UfsSysConfigRepository;
import ke.tra.ufs.webportal.service.FileExtensionRepository;
import ke.tra.ufs.webportal.service.ParFileConfigService;
import ke.tra.ufs.webportal.utils.SharedMethods;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@CommonsLog
public class ParFileConfigServiceTemplate extends ParFileService implements ParFileConfigService {

    ParGlobalConfigProfileRepository parGlobalConfigProfileRepository;
    ParGlobalConfigIndexingRepository parGlobalConfigIndexingRepository;
    ParGlobalConfigRepository parGlobalConfigRepository;
    LoggerService loggerServiceVersion;

    public ParFileConfigServiceTemplate(FileExtensionRepository fileExtensionRepository, LoggerService loggerService, SharedMethods sharedMethods,
                                        ParGlobalConfigProfileRepository parGlobalConfigProfileRepository, ParGlobalConfigIndexingRepository parGlobalConfigIndexingRepository,
                                        ParGlobalConfigRepository parGlobalConfigRepository,  UfsSysConfigRepository sysConfigRepository) {
        super(fileExtensionRepository, loggerService, sharedMethods);
        this.parGlobalConfigProfileRepository = parGlobalConfigProfileRepository;
        this.parGlobalConfigIndexingRepository = parGlobalConfigIndexingRepository;
        this.parGlobalConfigRepository = parGlobalConfigRepository;
        this.loggerServiceVersion = loggerService;
    }

    @Override
    public void generateGlobalConfigFile(GlobalConfigFileRequest request, String filePath) {
        // check if profile exists
        Optional<ParGlobalConfigProfile> optional = parGlobalConfigProfileRepository.findById(request.getProfile());
        if (optional.isPresent()) {
            generateGlobalConfigFileAsync(optional.get(), request.getDeviceModel(), filePath);
        } else {
            // log error - TODO log method not yet implemented - change
            System.out.println(">>>>>>>>>>>>>>>>>>>> Profile does not exist >>>>>>>>>>>>>>>>>>>>>");
            log.error("Global profile does not exist");
        }
    }

    @Override
    public void generateGlobalConfigFileAsync(ParGlobalConfigProfile profile, BigDecimal modelType, String filePath) {
        // get all configs of type - sort by index in ascending order
        List<ParGlobalConfigIndices> indices = parGlobalConfigIndexingRepository.findAllByConfigType(profile.getFileTypeId(),
                Sort.by(Sort.Direction.ASC, "configIndex"));

        // get all configs by profile id
        List<ParGlobalConfig> configs = parGlobalConfigRepository.findAllByProfileId(profile.getId());

        // add configs to hash table -- hash table or trie will make it easy to retrieve
        HashMap<String, ParGlobalConfig> map = new HashMap<>();
        for (ParGlobalConfig config : configs) {
            map.put(config.getParamName(), config);
            log.info("Global Config========>" + config.getId());
        }

        List<String> result = new ArrayList<>();
        for (ParGlobalConfigIndices index : indices) {
            log.info("Global Config Indices========>" +map.get(index.getConfigItem()));
            ParGlobalConfig config = map.get(index.getConfigItem());
            if (config != null) {
                result.add(config.getParamValue() + ";");
            } else {
                // add empty string
                result.add(";");
            }
        }
        createFile(result, modelType, profile.getConfigType().getFileName(), filePath);
    }
}
