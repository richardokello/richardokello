package ke.tra.ufs.webportal.resources;

import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.tra.ufs.webportal.entities.UfsCustomer;
import ke.tra.ufs.webportal.entities.UfsEdittedRecord;
import ke.tra.ufs.webportal.utils.exceptions.ItemNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping(value = "/customers")
public class CustomerResource extends ChasisResource<UfsCustomer, Long, UfsEdittedRecord> {
    public CustomerResource(LoggerService loggerService, EntityManager entityManager) {
        super(loggerService, entityManager);
    }


    @RequestMapping(value = "/search-account", method = RequestMethod.GET)
    public ResponseEntity<ResponseWrapper<UfsCustomer>> searchCustomer(@NotNull @RequestParam String accountNumber) {
        ResponseWrapper<UfsCustomer> response = new ResponseWrapper<>();
        if (!accountNumber.equals("1234355")) {
            throw new ItemNotFoundException("Customer with account number :" + accountNumber);
        }
        UfsCustomer cust = new UfsCustomer();
        cust.setAccountNumber(accountNumber);
        cust.setPin("1234355");
        cust.setLocalRegNumber("QWER122334");
        cust.setBusinessLicenceNumber("AS1234");
        response.setData(cust);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

}
