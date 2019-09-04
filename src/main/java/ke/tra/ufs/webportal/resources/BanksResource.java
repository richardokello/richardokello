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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
}
