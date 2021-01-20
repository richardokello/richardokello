package ke.co.tra.ufs.tms.service.templates;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ke.axle.chassis.utils.AppConstants;
import ke.co.tra.ufs.tms.entities.ParGlobalConfig;
import ke.co.tra.ufs.tms.entities.ParGlobalConfigIndices;
import ke.co.tra.ufs.tms.entities.ParGlobalConfigProfile;
import ke.co.tra.ufs.tms.entities.wrappers.GlobalConfigFileRequest;
import ke.co.tra.ufs.tms.repository.*;
import ke.co.tra.ufs.tms.service.FileExtensionRepository;
import ke.co.tra.ufs.tms.service.ParFileConfigService;
import ke.co.tra.ufs.tms.utils.SharedMethods;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
@CommonsLog
public class ParFileConfigServiceTemplate extends ParFileService implements ParFileConfigService {

    ParGlobalConfigProfileRepository parGlobalConfigProfileRepository;
    ParGlobalConfigIndexingRepository parGlobalConfigIndexingRepository;
    ParGlobalConfigRepository parGlobalConfigRepository;
    LoggerServiceVersion loggerServiceVersion;

    public ParFileConfigServiceTemplate(FileExtensionRepository fileExtensionRepository, LoggerServiceVersion loggerService, SharedMethods sharedMethods,
                                        ParGlobalConfigProfileRepository parGlobalConfigProfileRepository, ParGlobalConfigIndexingRepository parGlobalConfigIndexingRepository,
                                        ParGlobalConfigRepository parGlobalConfigRepository, LoggerServiceVersion loggerServiceVersion, UfsSysConfigRepository sysConfigRepository) {
        super(fileExtensionRepository, loggerService, sharedMethods);
        this.parGlobalConfigProfileRepository = parGlobalConfigProfileRepository;
        this.parGlobalConfigIndexingRepository = parGlobalConfigIndexingRepository;
        this.parGlobalConfigRepository = parGlobalConfigRepository;
        this.loggerServiceVersion = loggerServiceVersion;
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
            loggerServiceVersion.log("Global profile does not exist", "ParGlobalConfigProfile", request.getProfile(), "Generate global config file", AppConstants.STATUS_FAILED, "");
        }
    }

    @Async
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
