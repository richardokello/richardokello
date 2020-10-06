package ke.tra.ufs.webportal.resources;


import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.tra.ufs.webportal.entities.UfsCustomerOutlet;
import ke.tra.ufs.webportal.entities.UfsEdittedRecord;
import ke.tra.ufs.webportal.repository.UfsCustomerOutletRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.validation.Valid;

import ke.tra.ufs.webportal.entities.UfsCustomerTransfer;

import java.util.Optional;

@RestController
@RequestMapping(value = "/customer-transfer")
public class UfsCustomerTransferResource extends ChasisResource<UfsCustomerTransfer, Long, UfsEdittedRecord> {

    private final UfsCustomerOutletRepository ufsCustomerOutletRepository;

    public UfsCustomerTransferResource(LoggerService loggerService, EntityManager entityManager, UfsCustomerOutletRepository ufsCustomerOutletRepository) {
        super(loggerService, entityManager);
        this.ufsCustomerOutletRepository = ufsCustomerOutletRepository;
    }


    @Override
    @Transactional
    public ResponseEntity<ResponseWrapper<UfsCustomerTransfer>> create(@Valid @RequestBody UfsCustomerTransfer ufsCustomerTransfer) {
        Optional<UfsCustomerOutlet> optionalUfsCustomerOutlet = ufsCustomerOutletRepository.findById(ufsCustomerTransfer.getOutletIds().longValue());

        if (optionalUfsCustomerOutlet.isPresent()) {
            UfsCustomerOutlet ufsCustomerOutlet = optionalUfsCustomerOutlet.get();
            ufsCustomerOutlet.setBankBranchIds(ufsCustomerTransfer.getDestinationBranchIds());
            ufsCustomerOutletRepository.save(ufsCustomerOutlet);
        }
        return super.create(ufsCustomerTransfer);
    }
}
