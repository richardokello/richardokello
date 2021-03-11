package ke.tra.ufs.webportal.service.template;

import ke.axle.chassis.utils.LoggerService;
import ke.tra.ufs.webportal.entities.TmsDeviceFileExt;
import ke.tra.ufs.webportal.service.FileExtensionRepository;
import ke.tra.ufs.webportal.utils.SharedMethods;
import lombok.extern.apachecommons.CommonsLog;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@CommonsLog
public class ParFileService {

    private final FileExtensionRepository fileExtensionRepository;
    private final LoggerService loggerService;
    private final SharedMethods sharedMethods;

    public ParFileService(FileExtensionRepository fileExtensionRepository, LoggerService loggerService, SharedMethods sharedMethods) {
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
            sharedMethods.generateParamField(data, fileName + optional.get().getParamFileExt(), filePath);
        } else {
            log.error("Model type file extension not found");
        }
    }
}
