package ke.tra.ufs.webportal.resources;


import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.exceptions.ExpectationFailed;
<<<<<<< HEAD
import ke.axle.chassis.utils.AppConstants;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ActionWrapper;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.tra.ufs.webportal.entities.TmsDevice;
import ke.tra.ufs.webportal.entities.UfsCustomerOutlet;
import ke.tra.ufs.webportal.entities.UfsEdittedRecord;
import ke.tra.ufs.webportal.repository.UfsCustomerOutletRepository;
import ke.tra.ufs.webportal.repository.UfsCustomerTransferRepository;
import ke.tra.ufs.webportal.repository.UfsEdittedRecordRepository;
import ke.tra.ufs.webportal.service.TmsDeviceService;
import org.codehaus.jackson.map.ObjectMapper;
=======
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ActionWrapper;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.tra.ufs.webportal.entities.*;
import ke.tra.ufs.webportal.repository.TmsDeviceRepository;
import ke.tra.ufs.webportal.repository.UfsCustomerOutletRepository;
import ke.tra.ufs.webportal.repository.UfsCustomerTransferRepository;
import ke.tra.ufs.webportal.utils.AppConstants;
>>>>>>> brb-webportal
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.validation.Valid;

<<<<<<< HEAD
import ke.tra.ufs.webportal.entities.UfsCustomerTransfer;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
=======
import java.math.BigDecimal;
>>>>>>> brb-webportal
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/customer-transfer")
public class UfsCustomerTransferResource extends ChasisResource<UfsCustomerTransfer, Long, UfsEdittedRecord> {

    private final UfsCustomerOutletRepository ufsCustomerOutletRepository;
<<<<<<< HEAD
    private final UfsEdittedRecordRepository edittedRecordRepository;
    private final UfsCustomerTransferRepository customerTransferRepository;
    private final TmsDeviceService tmsDeviceService;

    public UfsCustomerTransferResource(LoggerService loggerService, EntityManager entityManager, UfsCustomerOutletRepository ufsCustomerOutletRepository, UfsEdittedRecordRepository edittedRecordRepository, UfsCustomerTransferRepository customerTransferRepository, TmsDeviceService tmsDeviceService) {
        super(loggerService, entityManager);
        this.ufsCustomerOutletRepository = ufsCustomerOutletRepository;
        this.edittedRecordRepository = edittedRecordRepository;
        this.customerTransferRepository = customerTransferRepository;
        this.tmsDeviceService = tmsDeviceService;
=======
    private final UfsCustomerTransferRepository transferRepository;
    private final TmsDeviceRepository tmsDeviceRepository;

    public UfsCustomerTransferResource(LoggerService loggerService, EntityManager entityManager, UfsCustomerOutletRepository ufsCustomerOutletRepository, UfsCustomerTransferRepository transferRepository, TmsDeviceRepository tmsDeviceRepository) {
        super(loggerService, entityManager);
        this.ufsCustomerOutletRepository = ufsCustomerOutletRepository;
        this.transferRepository = transferRepository;
        this.tmsDeviceRepository = tmsDeviceRepository;
>>>>>>> brb-webportal
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseWrapper<UfsCustomerTransfer>> create(@Valid @RequestBody UfsCustomerTransfer ufsCustomerTransfer) {
<<<<<<< HEAD
        ResponseWrapper wrapper = new ResponseWrapper();
        super.create(ufsCustomerTransfer);

        //save data to Modified Record
        try{
            UfsEdittedRecord modifiedRecord = new UfsEdittedRecord();
            modifiedRecord.setData(new ObjectMapper().writeValueAsString(ufsCustomerTransfer));
            modifiedRecord.setEntityId(String.valueOf(ufsCustomerTransfer.getId()));
            modifiedRecord.setUfsEntity(UfsCustomerTransfer.class.getSimpleName());
            edittedRecordRepository.save(modifiedRecord);
        } catch (Exception e) {
            System.out.println("EXCEPTION=====> "+e.getMessage());
=======
        ResponseWrapper responseWrapper = new ResponseWrapper();
        UfsCustomerOutlet customerOutlet = ufsCustomerOutletRepository.findByIdAndIntrash(ufsCustomerTransfer.getOutletIds(),AppConstants.INTRASH_NO);
        if (customerOutlet.getActionStatus().equals(AppConstants.STATUS_UNAPPROVED)) {
            responseWrapper.setCode(HttpStatus.CONFLICT.value());
            responseWrapper.setMessage("Customer Outlet Has Unapproved Actions");

            return new ResponseEntity(responseWrapper, HttpStatus.CONFLICT);
>>>>>>> brb-webportal
        }

        wrapper.setData(ufsCustomerTransfer);
        wrapper.setCode(201);
        wrapper.setMessage("Customer Transferred Successfully");
        return new ResponseEntity(wrapper, HttpStatus.CREATED);
    }


    @Override
    @Transactional
    public ResponseEntity<ResponseWrapper> approveActions(@Valid @RequestBody ActionWrapper<Long> actions) throws ExpectationFailed {
        ResponseEntity<ResponseWrapper> resp = super.approveActions(actions);

        if(!resp.getStatusCode().equals(HttpStatus.OK)) {
            return resp;
        }
        Arrays.stream(actions.getIds()).forEach(id -> {
            Optional<UfsCustomerTransfer> customerTransfer = customerTransferRepository.findById(id);
            if(customerTransfer.get().getAction().equals(AppConstants.ACTIVITY_CREATE) &&
                    customerTransfer.get().getActionStatus().equals(AppConstants.STATUS_APPROVED)){

                UfsEdittedRecord modifiedRecord = edittedRecordRepository.findByUfsEntityAndEntityId(UfsCustomerTransfer.class.getSimpleName(), String.valueOf(id));
                if(modifiedRecord != null){
                    try {
                        UfsCustomerTransfer transferWrapper = new com.fasterxml.jackson.databind.ObjectMapper().readValue(modifiedRecord.getData(), UfsCustomerTransfer.class);

                        Optional<UfsCustomerOutlet> optionalUfsCustomerOutlet = ufsCustomerOutletRepository.findById(transferWrapper.getOutletIds().longValue());
                        if (optionalUfsCustomerOutlet.isPresent()) {
                            UfsCustomerOutlet ufsCustomerOutlet = optionalUfsCustomerOutlet.get();
                            ufsCustomerOutlet.setBankBranchIds(transferWrapper.getDestinationBranchIds());
                            ufsCustomerOutletRepository.save(ufsCustomerOutlet);
                        }

                        List<BigDecimal> outletIds = new ArrayList<>();
                        outletIds.add(transferWrapper.getOutletIds());
                        List<TmsDevice> deviceList = tmsDeviceService.findByOutletIds(outletIds);

                        deviceList.stream().forEach(tmsDevice ->{
                            tmsDevice.setBankBranchIds(transferWrapper.getDestinationBranchIds());
                            tmsDeviceService.saveDevice(tmsDevice);
                        });

                        //delete from modifiedRecord Table
                        edittedRecordRepository.deleteByUfsEntityAndEntityId(UfsCustomerTransfer.class.getSimpleName(),String.valueOf(transferWrapper.getId()));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }

        });

        return resp;
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
