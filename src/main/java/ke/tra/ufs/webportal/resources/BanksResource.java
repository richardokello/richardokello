package ke.tra.ufs.webportal.resources;


import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.tra.ufs.webportal.entities.UfsBankBins;
import ke.tra.ufs.webportal.entities.UfsBanks;
import ke.tra.ufs.webportal.entities.UfsEdittedRecord;
import ke.tra.ufs.webportal.service.BankService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/banks")
public class BanksResource extends ChasisResource<UfsBanks, Long, UfsEdittedRecord> {
    private final BankService bankService;

    public BanksResource(LoggerService loggerService, EntityManager entityManager, BankService bankService) {
        super(loggerService, entityManager);
        this.bankService = bankService;
    }

    @Override
    public ResponseEntity<ResponseWrapper<UfsBanks>> create(@Valid @RequestBody UfsBanks ufsBanks) {
        ResponseEntity response = super.create(ufsBanks);
        if ((!response.getStatusCode().equals(HttpStatus.CREATED)) || (ufsBanks.getUfsBankBinsSet() == null) || ufsBanks.getUfsBankBinsSet().isEmpty()) {
            return response;
        }

        List<UfsBankBins> bins = new ArrayList<>();
        ufsBanks.getUfsBankBinsSet().forEach(bin -> {
            bin.setId(null);
            bins.add(bin);
        });
        bankService.saveAllBins(bins);
        return response;
    }
}
