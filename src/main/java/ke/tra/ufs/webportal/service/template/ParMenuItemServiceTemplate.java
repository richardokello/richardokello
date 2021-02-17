package ke.tra.ufs.webportal.service.template;

import ke.axle.chassis.utils.AppConstants;
import ke.tra.ufs.webportal.entities.ParMenuItems;
import ke.tra.ufs.webportal.repository.ParMenuItemRepository;
import ke.tra.ufs.webportal.service.ParMenuItemService;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@CommonsLog
public class ParMenuItemServiceTemplate implements ParMenuItemService {

    private final ParMenuItemRepository repository;

    public ParMenuItemServiceTemplate(ParMenuItemRepository repository) {
        this.repository = repository;
    }

    @Async
    @Override
    public void updateParents(List<BigDecimal> menuItems) {
        // get all successfully updated menu items
        List<ParMenuItems> items = repository.findAllByIdIn(menuItems);
        // filter parents that have not been set
        List<ParMenuItems> notParents = items.stream()
                .map(ParMenuItems::getParentId)
                .filter(menu -> menu.getIsParent() != 1)
                .collect(Collectors.toList());
        // update all to parents
        for (ParMenuItems menu : notParents) {
            menu.setIsParent((short) 1);
        }
        repository.saveAll(notParents);
    }

    @Override
    public Optional<ParMenuItems> findByNameAndCustomerType(String name, BigDecimal type) {
        return repository.findDistinctByNameAndCustomerTypeId(name, type);
    }

    @Override
    public List<ParMenuItems> getAllChildren(BigDecimal id) {
        return repository.findAllByParentIds(id);
    }

    @Override
    public  void deleteChildren(List<ParMenuItems> parMenuItemsList) {
        log.info(Arrays.asList("<<<<<<<<<<>>>>>>>>>>>>>"+parMenuItemsList));
        for (ParMenuItems x : parMenuItemsList){
            if (x.getIsParent()== 0) {
                x.setAction(AppConstants.ACTIVITY_DELETE);
                x.setIntrash(AppConstants.YES);
                x.setActionStatus(AppConstants.STATUS_APPROVED);
                repository.save(x);
            }else {
                x.setAction(AppConstants.ACTIVITY_DELETE);
                x.setIntrash(AppConstants.YES);
                x.setActionStatus(AppConstants.STATUS_APPROVED);
                repository.save(x);
                List<ParMenuItems> children = getAllChildren(x.getId());
                deleteChildren(children);
            }
        }
   }
}
