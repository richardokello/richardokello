package ke.tra.ufs.webportal.resources;


import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.exceptions.ExpectationFailed;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ActionWrapper;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.tra.ufs.webportal.entities.*;
import ke.tra.ufs.webportal.repository.TmsDeviceRepository;
import ke.tra.ufs.webportal.repository.UfsCustomerOutletRepository;
import ke.tra.ufs.webportal.repository.UfsCustomerTransferRepository;
import ke.tra.ufs.webportal.utils.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.validation.Valid;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/customer-transfer")
public class UfsCustomerTransferResource extends ChasisResource<UfsCustomerTransfer, Long, UfsEdittedRecord> {

    private final UfsCustomerOutletRepository ufsCustomerOutletRepository;
    private final UfsCustomerTransferRepository transferRepository;
    private final TmsDeviceRepository tmsDeviceRepository;

    public UfsCustomerTransferResource(LoggerService loggerService, EntityManager entityManager, UfsCustomerOutletRepository ufsCustomerOutletRepository, UfsCustomerTransferRepository transferRepository, TmsDeviceRepository tmsDeviceRepository) {
        super(loggerService, entityManager);
        this.ufsCustomerOutletRepository = ufsCustomerOutletRepository;
        this.transferRepository = transferRepository;
        this.tmsDeviceRepository = tmsDeviceRepository;
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseWrapper<UfsCustomerTransfer>> create(@Valid @RequestBody UfsCustomerTransfer ufsCustomerTransfer) {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        UfsCustomerOutlet customerOutlet = ufsCustomerOutletRepository.findByIdAndIntrash(ufsCustomerTransfer.getOutletIds(),AppConstants.INTRASH_NO);
        if (customerOutlet.getActionStatus().equals(AppConstants.STATUS_UNAPPROVED)) {
            responseWrapper.setCode(HttpStatus.CONFLICT.value());
            responseWrapper.setMessage("Customer Outlet Has Unapproved Actions");

            return new ResponseEntity(responseWrapper, HttpStatus.CONFLICT);
        }
        return super.create(ufsCustomerTransfer);
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseWrapper> approveActions(@Valid @RequestBody ActionWrapper<Long> actions) throws ExpectationFailed {
        ResponseEntity<ResponseWrapper> response = super.approveActions(actions);
        if (!response.getStatusCode().equals(HttpStatus.OK)) {
            return response;
        }

        Arrays.stream(actions.getIds()).forEach(id->{
            Optional<UfsCustomerTransfer> transferOptional = transferRepository.findById(id);
            if(transferOptional.isPresent()) {
                UfsCustomerTransfer customerTransfer = transferOptional.get();
                Optional<UfsCustomerOutlet> optionalUfsCustomerOutlet = ufsCustomerOutletRepository.findById(customerTransfer.getOutletIds());
                if (customerTransfer.getAction().equals(AppConstants.ACTIVITY_CREATE) || customerTransfer.getAction().equals(AppConstants.ACTIVITY_UPDATE)) {
                    if (optionalUfsCustomerOutlet.isPresent()) {
                        UfsCustomerOutlet ufsCustomerOutlet = optionalUfsCustomerOutlet.get();
                        ufsCustomerOutlet.setBankBranchIds(customerTransfer.getDestinationBranchIds());
                        ufsCustomerOutletRepository.save(ufsCustomerOutlet);

                        List<TmsDevice> tmsDevices = tmsDeviceRepository.findAllByOutletIds(new BigDecimal(optionalUfsCustomerOutlet.get().getId()));
                        tmsDevices.forEach(tmsDevice -> {
                            tmsDevice.setBankBranchIds(customerTransfer.getDestinationBranchIds());
                            tmsDeviceRepository.save(tmsDevice);
                        });
                    }
                }
            }
         });

        return response;
    }

}
