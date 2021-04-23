package ke.tra.ufs.webportal.resources;

import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.exceptions.ExpectationFailed;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ActionWrapper;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.tra.ufs.webportal.entities.UfsCustomerOutlet;
import ke.tra.ufs.webportal.entities.UfsEdittedRecord;
import ke.tra.ufs.webportal.service.TmsDeviceService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/customer-outlet")
public class UfsCustomerOutletResource extends ChasisResource<UfsCustomerOutlet, Long, UfsEdittedRecord> {

    private final TmsDeviceService deviceService;

    public UfsCustomerOutletResource(LoggerService loggerService, EntityManager entityManager, TmsDeviceService deviceService) {
        super(loggerService, entityManager);
        this.deviceService = deviceService;
    }


    @Override
    @Transactional
    public ResponseEntity<ResponseWrapper> approveActions(@RequestBody @Valid ActionWrapper<Long> actions) throws ExpectationFailed {
        ResponseEntity<ResponseWrapper> resp = super.approveActions(actions);

        // approve devices by outlet Ids
        List<Long> outletsIds = Arrays.stream(actions.getIds()).collect(Collectors.toList());
        deviceService.activateDevicesByOutletsIds(outletsIds, actions.getNotes());

        return resp;
    }
}
