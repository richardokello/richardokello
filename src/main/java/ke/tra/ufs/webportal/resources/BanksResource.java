package ke.tra.ufs.webportal.resources;


import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.exceptions.ExpectationFailed;
import ke.axle.chassis.utils.AppConstants;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ActionWrapper;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.tra.ufs.webportal.entities.UfsBankBins;
import ke.tra.ufs.webportal.entities.UfsBanks;
import ke.tra.ufs.webportal.entities.UfsEdittedRecord;
import ke.tra.ufs.webportal.repository.UfsBankBinsRepository;
import ke.tra.ufs.webportal.repository.UfsBankRepository;
import ke.tra.ufs.webportal.service.BankService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping(value = "/banks")
public class BanksResource extends ChasisResource<UfsBanks, Long, UfsEdittedRecord> {
    private final BankService bankService;
    private final UfsBankBinsRepository bankBinsRepository;
    private final UfsBankRepository ufsBankRepository;

    public BanksResource(LoggerService loggerService, EntityManager entityManager, BankService bankService,UfsBankBinsRepository bankBinsRepository,
                         UfsBankRepository ufsBankRepository) {
        super(loggerService, entityManager);
        this.bankService = bankService;
        this.bankBinsRepository = bankBinsRepository;
        this.ufsBankRepository = ufsBankRepository;
    }

    @Override
    public ResponseEntity<ResponseWrapper<UfsBanks>> create(@Valid @RequestBody UfsBanks ufsBanks) {
        ResponseWrapper response = new ResponseWrapper();
        UfsBanks bank = bankService.findByNameOrCode(ufsBanks.getBankName(),ufsBanks.getBankCode());
        if (bank != null ) {
            response.setCode(HttpStatus.CONFLICT.value());
            response.setMessage(bank.getBankName()+" Bank Name or "+bank.getBankCode()+" Bank Code already exist");

            return new ResponseEntity(response, HttpStatus.CONFLICT);
        }
        ResponseEntity responseEntity = super.create(ufsBanks);
        if ((!responseEntity.getStatusCode().equals(HttpStatus.CREATED))) {
            return responseEntity;
        }

        if(ufsBanks.getUfsBankBins() != null){
            List<UfsBankBins> bins = new ArrayList<>();
            ufsBanks.getUfsBankBins().forEach(bin -> {
                UfsBankBins bankBins = new UfsBankBins();
                bankBins.setBinType(bin.getBinType());
                bankBins.setBankIds(ufsBanks.getId());
                bankBins.setValue(bin.getValue());
                bins.add(bankBins);
            });
            bankService.saveAllBins(bins);
        }

        return responseEntity;
    }

    /*Getting Bank Bins */
    @RequestMapping(value = "/bank-bins/{id}", method = RequestMethod.GET)
    public ResponseEntity<ResponseWrapper> typeRules(@PathVariable("id") Long id) {
        ResponseWrapper response = new ResponseWrapper();
         List<UfsBankBins> bankBins = bankBinsRepository.findAllByBankIds(id);

        if (bankBins.isEmpty()) {

            response.setData(bankBins);
            return ResponseEntity.ok(response);
        }
       response.setData(bankBins);
        return ResponseEntity.ok(response);
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseWrapper> approveActions(@Valid @RequestBody ActionWrapper<Long> actions) throws ExpectationFailed {

        for(Long id: actions.getIds()){
            Optional<UfsBanks> b = ufsBankRepository.findById(id);

            if(b.isPresent()){
                if(b.get().getAction().equalsIgnoreCase(AppConstants.ACTIVITY_UPDATE) && b.get().getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)){
                        try {
                            UfsBanks entity = supportRepo.mergeChanges(id, b.get());

                            List<UfsBankBins> existingBankBins = new ArrayList<>();
                            List<UfsBankBins> newBankBins = entity.getUfsBankBins();


                             /*Getting the existing bankbins from the database and add them to existingBankBins list*/
                            bankBinsRepository.findAllByBankIds(id).forEach(bankBinFromDb->{
                                existingBankBins.add(bankBinFromDb);
                            });

                            List<UfsBankBins> isPresentObject = new ArrayList<>();
                            List<UfsBankBins> toDeleteObject = new ArrayList<>();
                            List<UfsBankBins> toCreateObject = new ArrayList<>();



                            /*Using objects*/
                            existingBankBins.forEach(obj->{
                                if(newBankBins.contains(obj)){
                                    isPresentObject.add(obj);
                                }

                                if(!newBankBins.contains(obj)){
                                    toDeleteObject.add(obj);
                                }
                            });

                            /*Ids of the bankBins to delete*/
                            List<Long> toDelete = new ArrayList<>();
                            for(UfsBankBins deleteBankBin : toDeleteObject){
                                toDelete.add(deleteBankBin.getId());
                            }

                            /*getting new bankBins object to create*/
                            newBankBins.forEach(obj -> {
                                if (!isPresentObject.contains(obj) && !toDeleteObject.contains(obj)) {
                                    toCreateObject.add(obj);
                                }
                            });

                            if (!toDeleteObject.isEmpty()) {
                                List<UfsBankBins> waitingDeletion = bankBinsRepository.findAllByBankIdsAndIdIn(id,toDelete);
                                log.info(" >>>>>>>>>>>>>>>>>>>>>>>>> size {}", waitingDeletion.size());
                                bankBinsRepository.deleteAll(waitingDeletion);
                            }

                            if (!toCreateObject.isEmpty()) {
                                List<UfsBankBins> items = new ArrayList<>();

                                toCreateObject.forEach(obj -> {
                                    UfsBankBins map = new UfsBankBins();
                                    map.setBankIds(id);
                                    map.setValue(obj.getValue());
                                    map.setBinType(obj.getBinType());

                                    items.add(map);
                                });

                                bankBinsRepository.saveAll(items);
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                }
            }

        }
        return super.approveActions(actions);
    }
}
