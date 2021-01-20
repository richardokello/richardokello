package ke.co.tra.ufs.tms.controller;

import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.exceptions.ExpectationFailed;
import ke.axle.chassis.utils.AppConstants;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ActionWrapper;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.co.tra.ufs.tms.entities.ParMenuItems;
import ke.co.tra.ufs.tms.entities.UfsEdittedRecord;
import ke.co.tra.ufs.tms.repository.ParMenuItemRepository;
import ke.co.tra.ufs.tms.service.ParMenuItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.*;

@Controller
@RequestMapping("/menu-items")
public class ParMenuItemsResource extends ChasisResource<ParMenuItems, BigDecimal, UfsEdittedRecord> {

    private final ParMenuItemService parMenuItemService;
    private final ParMenuItemRepository repository;


    public ParMenuItemsResource(LoggerService loggerService, EntityManager entityManager, ParMenuItemService parMenuItemService, ParMenuItemRepository repository) {
        super(loggerService, entityManager);
        this.parMenuItemService = parMenuItemService;
        this.repository = repository;
    }

    @Override
    public ResponseEntity<ResponseWrapper<ParMenuItems>> create(@Valid @RequestBody ParMenuItems items) {
        // check if the menu item exists - ensure you dont save menu item on same customer type
        Optional<ParMenuItems> optional = parMenuItemService.findByNameAndCustomerType(items.getName(), items.getCustomerTypeId());
        if (optional.isPresent()) {
            ResponseWrapper<ParMenuItems> wrapper = new ResponseWrapper<>();
            wrapper.setCode(HttpStatus.NOT_ACCEPTABLE.value());
            wrapper.setMessage("Menu item already exists");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(wrapper);
        }
        return super.create(items);
    }

    /**
     * @param actions
     * @return
     * @throws ExpectationFailed approves menu items and also updates a menu item to parent if they have children
     */
    @SuppressWarnings({"unchecked, rawtypes"})
    @Override
    public ResponseEntity<ResponseWrapper> approveActions(@RequestBody @Valid ActionWrapper<BigDecimal> actions) throws ExpectationFailed {
        ResponseEntity<ResponseWrapper> response = super.approveActions(actions);
        if(!response.getStatusCode().is2xxSuccessful()) return response;

        for (BigDecimal ids : actions.getIds()) {
            ParMenuItems parMenuItems = repository.findById(ids).get();
            if (parMenuItems.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_DELETE ) && parMenuItems.getIsParent()== 1) {
                List<ParMenuItems> parMenuItemsList = parMenuItemService.getAllChildren(parMenuItems.getId());
                parMenuItemService.deleteChildren(parMenuItemsList);
            }
        }


        Map<String, Object> map = (Map<String, Object>) Objects.requireNonNull(response.getBody()).getData();
        parMenuItemService.updateParents((List<BigDecimal>) map.get("success"));
        return response;
    }

}
