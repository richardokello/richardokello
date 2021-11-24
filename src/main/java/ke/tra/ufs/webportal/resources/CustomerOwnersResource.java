package ke.tra.ufs.webportal.resources;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.exceptions.ExpectationFailed;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ActionWrapper;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.tra.ufs.webportal.entities.CustomerOwnersCrime;
import ke.tra.ufs.webportal.entities.UfsCustomerOwners;
import ke.tra.ufs.webportal.entities.UfsEdittedRecord;
import ke.tra.ufs.webportal.entities.filters.CustomerIdFilter;
import ke.tra.ufs.webportal.entities.wrapper.PosUserWrapper;
import ke.tra.ufs.webportal.service.CustomerOwnersService;
import ke.tra.ufs.webportal.service.PosUserIdGenerator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@RestController
@RequestMapping(value = "/customer-owners")
public class CustomerOwnersResource extends ChasisResource<UfsCustomerOwners, Long, UfsEdittedRecord> {

    private final CustomerOwnersService customerOwnersService;
    private final PosUserIdGenerator posUserIdGenerator;

    public CustomerOwnersResource(LoggerService loggerService, EntityManager entityManager, CustomerOwnersService customerOwnersService, PosUserIdGenerator posUserIdGenerator) {
        super(loggerService, entityManager);
        this.customerOwnersService = customerOwnersService;
        this.posUserIdGenerator = posUserIdGenerator;
    }

    @Override
    public ResponseEntity<ResponseWrapper<UfsCustomerOwners>> create(@Valid @RequestBody UfsCustomerOwners ufsCustomerOwners) {
        ResponseWrapper response = new ResponseWrapper();
        String[] usernames = ufsCustomerOwners.getDirectorName().split("\\s+");

        String username = (usernames.length == 1) ? posUserIdGenerator.generateUsername(new PosUserWrapper(usernames[0])) : posUserIdGenerator.generateUsername(new PosUserWrapper(usernames[0], usernames[1]));
        ufsCustomerOwners.setUserName(username);
        //check if owner user name already exists
       /* UfsCustomerOwners contactPerson = customerOwnersService.findByUsername(ufsCustomerOwners.getUserName());
        if (Objects.nonNull(contactPerson)) {
            response.setCode(417);
            response.setMessage("Owner With That username Already Exists");
            return new ResponseEntity(response, HttpStatus.FAILED_DEPENDENCY);
        }*/
        ResponseEntity<ResponseWrapper<UfsCustomerOwners>> creationResp = super.create(ufsCustomerOwners);
        if ((!creationResp.getStatusCode().equals(HttpStatus.CREATED))) {
            return creationResp;
        }

        if (ufsCustomerOwners.getOwnersCrime() != null) {
            CustomerOwnersCrime ownersCrime = new CustomerOwnersCrime();
            ownersCrime.setCustomerOwnerIds(new BigDecimal(ufsCustomerOwners.getId()));
            ownersCrime.setDescription(ufsCustomerOwners.getOwnersCrime().getDescription());
            ownersCrime.setCustomerIds(ufsCustomerOwners.getCustomerIds());
            customerOwnersService.save(ownersCrime);
        }

        response.setCode(201);
        response.setData(creationResp);
        response.setMessage("Contact Person Created Successfully.");
        return new ResponseEntity(response, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ResponseWrapper> approveActions(@Valid @RequestBody ActionWrapper<Long> actions) throws ExpectationFailed {
        ResponseEntity<ResponseWrapper> resp = super.approveActions(actions);
        if (!resp.getStatusCode().equals(HttpStatus.OK)) {
            return resp;
        }

        List<Long> ownersList = Stream.of(actions.getIds()).collect(Collectors.toList());

        return resp;
    }

    @ApiOperation(value = "Fetch Owners belonging to a certain customer")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", dataType = "int", required = false, value = "Pagination size e.g 20", paramType = "query")
            ,
            @ApiImplicitParam(name = "page", dataType = "int", required = false, value = "Page number e.g 0", paramType = "query")
            ,
            @ApiImplicitParam(name = "sort", dataType = "string", required = false, value = "status,desc", paramType = "query")
    })
    @RequestMapping(value = "/customer", method = RequestMethod.GET)
    public ResponseEntity<ResponseWrapper<Page<UfsCustomerOwners>>> getOwnerByCustomerId(@Valid CustomerIdFilter filter, Pageable pg) {
        ResponseWrapper response = new ResponseWrapper();
        response.setData(this.customerOwnersService.getOwnersByCustomerId(filter.getActionStatus(),
                filter.getCustomerIds(),filter.getNeedle(), pg));
        return ResponseEntity.ok(response);

    }
}
