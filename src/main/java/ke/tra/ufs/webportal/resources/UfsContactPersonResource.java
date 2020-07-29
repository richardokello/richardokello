package ke.tra.ufs.webportal.resources;

import io.swagger.annotations.ApiOperation;
import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.tra.ufs.webportal.entities.UfsContactPerson;
import ke.tra.ufs.webportal.entities.UfsEdittedRecord;
import ke.tra.ufs.webportal.service.ContactPersonService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;


@RestController
@RequestMapping(value = "/contact-person")
public class UfsContactPersonResource extends ChasisResource<UfsContactPerson,Long, UfsEdittedRecord> {

    private final ContactPersonService contactPersonService;

    public UfsContactPersonResource(LoggerService loggerService, EntityManager entityManager,ContactPersonService contactPersonService) {
        super(loggerService, entityManager);
        this.contactPersonService = contactPersonService;
    }

    @RequestMapping(value = "/{customerId}/all" , method = RequestMethod.GET)
    @Transactional
    @ApiOperation(value = "Contact Person", notes = "Get All Contact Person By Customer Id.")
    public ResponseWrapper<Object> getContactPersonByCustomerId(Pageable pg, @PathVariable("customerId") BigDecimal customerId) {
        ResponseWrapper response =  new ResponseWrapper<>();

        List<UfsContactPerson> contactPersonList = this.contactPersonService.getAllContactPersonByCustomerId(customerId);

        Page<UfsContactPerson> pageResponse = new PageImpl<>(contactPersonList, pg, contactPersonList.size());
        response.setData(pageResponse);
        response.setCode(HttpStatus.OK.value());
        response.setTimestamp(Calendar.getInstance().getTimeInMillis());
        response.setMessage("Request Was successful");

        return response;
    }
}
