package ke.tra.ufs.webportal.resources;


import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.tra.ufs.webportal.entities.UfsAssignedSimdetails;
import ke.tra.ufs.webportal.entities.UfsCustomerTypeRules;
import ke.tra.ufs.webportal.entities.UfsEdittedRecord;
import ke.tra.ufs.webportal.service.AssignDeviceService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.validation.Valid;

import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.tra.ufs.webportal.entities.UfsAssignedDevice;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/assign-device")
public class UfsAssignDeviceResource extends ChasisResource<UfsAssignedDevice, Long, UfsEdittedRecord> {

    private final AssignDeviceService assignDeviceService;

    public UfsAssignDeviceResource(LoggerService loggerService, EntityManager entityManager,AssignDeviceService assignDeviceService) {
        super(loggerService, entityManager);
        this.assignDeviceService = assignDeviceService;
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseWrapper<UfsAssignedDevice>> create(@RequestBody @Valid UfsAssignedDevice assignDevice) {
        ResponseEntity response = super.create(assignDevice);
        if ((!response.getStatusCode().equals(HttpStatus.CREATED))) {
            return response;
        }

        List<UfsAssignedSimdetails> ufsAssignedSimdetails = new ArrayList<>();

        assignDevice.getAssignedSimDetails().stream().forEach(assignDeviceObject ->{

            UfsAssignedSimdetails assignedSimdetails = new UfsAssignedSimdetails();
            assignedSimdetails.setAssignedDeviceIds(new BigDecimal(assignDevice.getId()));
            assignedSimdetails.setPhoneNumber(assignDeviceObject.getPhoneNumber());
            assignedSimdetails.setSerialNumber(assignDeviceObject.getSerialNumber());
            assignedSimdetails.setSimPin(assignDeviceObject.getSimPin());
            ufsAssignedSimdetails.add(assignedSimdetails);

        });
        assignDeviceService.saveAllSimDetails(ufsAssignedSimdetails);

        return response;
    }
    
    
}
