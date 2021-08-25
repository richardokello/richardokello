package ke.co.tra.ufs.tms.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.co.tra.ufs.tms.entities.CrdbBillers;
import ke.co.tra.ufs.tms.entities.UfsModifiedRecord;
import ke.co.tra.ufs.tms.entities.wrappers.InstitutionsResponse;
import ke.co.tra.ufs.tms.entities.wrappers.filters.CrdbBillersFilter;
import ke.co.tra.ufs.tms.service.CrdbBillersService;
import ke.co.tra.ufs.tms.wrappers.ResponseWrapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.validation.Valid;

/**
 * @author KMwangi
 */
@RequestMapping(value = "/crdb-billers")
@Api(value = "Crdb Billers Logs And Control Number Confirmation")
@RestController
public class CrdbBillersResource extends ChasisResource<CrdbBillers, Long, UfsModifiedRecord> {


    private final CrdbBillersService crdbBillersService;

    public CrdbBillersResource(LoggerService loggerService, EntityManager entityManager, CrdbBillersService crdbBillersService) {
        super(loggerService, entityManager);
        this.crdbBillersService = crdbBillersService;
    }

    @RequestMapping(value = "/pending-retries",method = RequestMethod.GET)
    @ApiOperation(value = "Fetch All Transactions That Require Retries")
    public ResponseEntity<ResponseWrapper<Page<CrdbBillers>>> getPendingRetries(Pageable pg,
                                                                                @Valid @ApiParam(value = "Entity filters and search parameters") CrdbBillersFilter filter) {
        ResponseWrapper<Page<CrdbBillers>> response = new ResponseWrapper();
        response.setData(crdbBillersService.getAllPendingRetriesBillers(filter.getNeedle(),filter.getFrom(), filter.getTo(), pg));
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/institutions",method = RequestMethod.GET)
    @ApiOperation(value = "Fetch All Crdb Institutions")
    public ResponseEntity<InstitutionsResponse> getInstitutions() {
        InstitutionsResponse response = crdbBillersService.getInstitutions();
        return ResponseEntity.ok(response);
    }


}
