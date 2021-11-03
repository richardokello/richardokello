package ke.tra.ufs.webportal.service.template;


import ke.axle.chassis.utils.LoggerService;
import ke.tra.ufs.webportal.entities.ParMenuIndices;
import ke.tra.ufs.webportal.entities.ParMenuProfile;
import ke.tra.ufs.webportal.entities.wrapper.MenuFileRequest;
import ke.tra.ufs.webportal.repository.ParMenuIndexingRepository;
import ke.tra.ufs.webportal.repository.ParMenuProfileRepository;
import ke.tra.ufs.webportal.service.FileExtensionRepository;
import ke.tra.ufs.webportal.service.ParFileMenuService;
import ke.tra.ufs.webportal.utils.SharedMethods;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
@CommonsLog
public class ParFileMenuServiceTemplate extends ParFileService implements ParFileMenuService {

    private final ParMenuProfileRepository menuProfileRepository;
    private final ParMenuIndexingRepository menuIndexingRepository;
    private final LoggerService loggerService;

    public ParFileMenuServiceTemplate(ParMenuProfileRepository menuProfileRepository, ParMenuIndexingRepository menuIndexingRepository,
                                      FileExtensionRepository fileExtensionRepository, LoggerService loggerService, SharedMethods sharedMethods) {
        super(fileExtensionRepository, loggerService, sharedMethods);
        this.menuProfileRepository = menuProfileRepository;
        this.menuIndexingRepository = menuIndexingRepository;
        this.loggerService = loggerService;
    }

    @Override
    public void generateMenuFileAsync(MenuFileRequest fileRequest, String filePath) {
        log.error("Menu file request" + filePath);
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
            log.error("Menu profile not found");
        }
    }
}
