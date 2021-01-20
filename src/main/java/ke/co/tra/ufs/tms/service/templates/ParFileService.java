package ke.co.tra.ufs.tms.service.templates;

import ke.axle.chassis.utils.AppConstants;
import ke.co.tra.ufs.tms.entities.TmsDeviceFileExt;
import ke.co.tra.ufs.tms.entities.UfsSysConfig;
import ke.co.tra.ufs.tms.repository.UfsSysConfigRepository;
import ke.co.tra.ufs.tms.service.FileExtensionRepository;
import ke.co.tra.ufs.tms.utils.SharedMethods;
import lombok.extern.apachecommons.CommonsLog;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@CommonsLog
public class ParFileService {

    private final FileExtensionRepository fileExtensionRepository;
    private final LoggerServiceVersion loggerService;
    private final SharedMethods sharedMethods;

    public ParFileService(FileExtensionRepository fileExtensionRepository, LoggerServiceVersion loggerService, SharedMethods sharedMethods) {
        this.fileExtensionRepository = fileExtensionRepository;
        this.loggerService = loggerService;
        this.sharedMethods = sharedMethods;
    }

    public void createFile(List<String> data, BigDecimal modelType, String fileName, String filePath) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : data) {
            stringBuilder.append(s);
        }
        // TODO add file path
        createFile(stringBuilder.toString(), modelType, fileName, filePath);
    }

    public void createFile(String data, BigDecimal modelType, String fileName, String filePath) {
        log.error("Creating file==>" + data);
        // get file extension
        Optional<TmsDeviceFileExt> optional = fileExtensionRepository.findByModelId(modelType);
        if (optional.isPresent()) {
            // todo use fileName
            sharedMethods.generateParamField(data, fileName + optional.get().getParamFileExt(), filePath, loggerService);
        } else {
            loggerService.log("Model type file extension not found", "TmsDeviceFileExt", modelType,
                    "Generating menu param file", AppConstants.STATUS_FAILED, "Model Type file");
        }
    }
}
