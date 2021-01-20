package ke.co.tra.ufs.tms.service.templates;

import ke.co.tra.ufs.tms.entities.ParMenuIndices;
import ke.co.tra.ufs.tms.entities.ParMenuProfile;
import ke.co.tra.ufs.tms.entities.wrappers.MenuFileRequest;
import ke.co.tra.ufs.tms.repository.ParMenuIndexingRepository;
import ke.co.tra.ufs.tms.repository.ParMenuProfileRepository;
import ke.co.tra.ufs.tms.repository.UfsSysConfigRepository;
import ke.co.tra.ufs.tms.service.FileExtensionRepository;
import ke.co.tra.ufs.tms.service.ParFileMenuService;
import ke.co.tra.ufs.tms.utils.AppConstants;
import ke.co.tra.ufs.tms.utils.SharedMethods;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
@CommonsLog
public class ParFileMenuServiceTemplate extends ParFileService implements ParFileMenuService {

    private final ParMenuProfileRepository menuProfileRepository;
    private final ParMenuIndexingRepository menuIndexingRepository;
    private final LoggerServiceVersion loggerService;

    public ParFileMenuServiceTemplate(ParMenuProfileRepository menuProfileRepository, ParMenuIndexingRepository menuIndexingRepository,
                                      FileExtensionRepository fileExtensionRepository, LoggerServiceVersion loggerService, SharedMethods sharedMethods) {
        super(fileExtensionRepository, loggerService, sharedMethods);
        this.menuProfileRepository = menuProfileRepository;
        this.menuIndexingRepository = menuIndexingRepository;
        this.loggerService = loggerService;
    }

    // @Async
//    @Override
//    public void generateMenuFile(MenuFileRequest request, String filePath) {
//        generateMenuFileAsync(request, filePath);
//    }

    @Async
    public void generateMenuFileAsync(MenuFileRequest fileRequest, String filePath) {
        log.error("Menu file request"+filePath);
        // get menu profile
        Optional<ParMenuProfile> profile = menuProfileRepository.findByIdAndActionStatusAndIntrash(fileRequest.getMenuProfile(), "Approved", "NO");
        // should log if it does not exist
        if (profile.isPresent()) {
            ParMenuProfile menuProfile = profile.get();
            // get menu indices -- and arrange by index
            List<ParMenuIndices> indices = menuIndexingRepository.findAllByCustomerType(menuProfile.getCustomerTypeId(), Sort.by(Sort.Direction.ASC, "menuIndex"));
            // remove array brackets
            String menu = menuProfile.getMenuValue();
            String updatedMenus = menu.replace("[", "")
                    .replace("]", "")
                    .replaceAll(" ", "");

            String[] profileMenus = updatedMenus.split(",");
            // add menu items to hash map
            Set<BigDecimal> set = new HashSet<>();

            for (String m : profileMenus) {
                // TODO find way to remove redundant warning -- parseLong does not work with minus
                set.add(BigDecimal.valueOf(Long.valueOf(m)));
            }

            // result list - contains 0 and 1 for every index
            List<String> result = new ArrayList<>();
            for (ParMenuIndices index : indices) {
                if (set.contains(index.getMenuItem())) {
                    result.add("1;");
                } else {
                    result.add("0;");
                }
            }
            createFile(result, fileRequest.getDeviceModel(), "MENU", filePath);
        } else {
            loggerService.log("Menu profile not found", "ParMenuProfile", fileRequest.getMenuProfile(),
                    "Generating menu profile parameters", AppConstants.STATUS_FAILED);
        }
    }
}
