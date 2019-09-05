package ke.tra.ufs.webportal.resources;


import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.tra.ufs.webportal.entities.UfsBankBins;
import ke.tra.ufs.webportal.entities.UfsBanks;
import ke.tra.ufs.webportal.entities.UfsEdittedRecord;
import ke.tra.ufs.webportal.repository.UfsBankBinsRepository;
import ke.tra.ufs.webportal.service.BankService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping(value = "/banks")
public class BanksResource extends ChasisResource<UfsBanks, Long, UfsEdittedRecord> {
    private final BankService bankService;
    private final UfsBankBinsRepository bankBinsRepository;

    public BanksResource(LoggerService loggerService, EntityManager entityManager, BankService bankService,UfsBankBinsRepository bankBinsRepository) {
        super(loggerService, entityManager);
        this.bankService = bankService;
        this.bankBinsRepository = bankBinsRepository;
    }

    @Override
    public ResponseEntity<ResponseWrapper<UfsBanks>> create(@Valid @RequestBody UfsBanks ufsBanks) {
        ResponseEntity response = super.create(ufsBanks);
        if ((!response.getStatusCode().equals(HttpStatus.CREATED)) || ufsBanks.getUfsBankBins().size() < 0 || Objects.isNull(ufsBanks.getUfsBankBins())) {
            return response;
        }

        List<UfsBankBins> bins = new ArrayList<>();
        ufsBanks.getUfsBankBins().forEach(bin -> {
            UfsBankBins bankBins = new UfsBankBins();
            bankBins.setBinType(bin.getBinType());
            bankBins.setBankIds(ufsBanks.getId());
            bankBins.setValue(bin.getValue());
            bins.add(bankBins);
        });
        bankService.saveAllBins(bins);
        return response;
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
}
