package ke.tra.ufs.webportal.resources;

import io.swagger.annotations.ApiOperation;
import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.exceptions.NotFoundException;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.tra.ufs.webportal.entities.UfsCustomerComplaints;
import ke.tra.ufs.webportal.entities.UfsCustomerComplaintsBatch;
import ke.tra.ufs.webportal.entities.UfsEdittedRecord;
import ke.tra.ufs.webportal.entities.wrapper.CustomerComplaintsDetails;
import ke.tra.ufs.webportal.entities.wrapper.TrainedAgentsDetails;
import ke.tra.ufs.webportal.entities.wrapper.UfsCustomerComplaintsUpload;
import ke.tra.ufs.webportal.entities.wrapper.UfsCustomerComplaintsWrapper;
import ke.tra.ufs.webportal.repository.UfsSysConfigRepository;
import ke.tra.ufs.webportal.service.CustomerComplaintsService;
import ke.tra.ufs.webportal.service.CustomerService;
import ke.tra.ufs.webportal.service.GeographicalRegionService;
import ke.tra.ufs.webportal.service.SysConfigService;
import ke.tra.ufs.webportal.utils.AppConstants;
import ke.tra.ufs.webportal.utils.SharedMethods;
import ke.tra.ufs.webportal.utils.exports.CsvFlexView;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

@RestController
@RequestMapping("/customer-complaints")
public class UfsCustomerComplaintsResource extends ChasisResource<UfsCustomerComplaints,Long, UfsEdittedRecord> {

    private final CustomerService customerService;
    private final GeographicalRegionService geographicalRegionService;
    private final CustomerComplaintsService customerComplaintsService;
    private final SharedMethods sharedMethods;
    private final UfsSysConfigRepository sysConfigRepository;
    private final SysConfigService configService;

    public UfsCustomerComplaintsResource(LoggerService loggerService, EntityManager entityManager,CustomerService customerService,GeographicalRegionService geographicalRegionService,
                                         CustomerComplaintsService customerComplaintsService,SharedMethods sharedMethods,UfsSysConfigRepository sysConfigRepository,
                                         SysConfigService configService) {
        super(loggerService, entityManager);
        this.customerService = customerService;
        this.geographicalRegionService = geographicalRegionService;
        this.customerComplaintsService = customerComplaintsService;
        this.sharedMethods = sharedMethods;
        this.sysConfigRepository = sysConfigRepository;
        this.configService = configService;
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @Transactional
    @ApiOperation(value = "Creating Customer Complaints Report")
    public ResponseEntity<ResponseWrapper> create(@Valid UfsCustomerComplaintsWrapper customerComplaints) throws NotFoundException {
        ResponseWrapper response = new ResponseWrapper();
        UfsCustomerComplaints ufsCustomerComplaints = new UfsCustomerComplaints();
        if(Objects.nonNull(this.customerService.findByCustomerId(customerComplaints.getCustomerId()))){
            ufsCustomerComplaints.setAgentComplained(this.customerService.findByCustomerId(customerComplaints.getCustomerId()).getCustomerName());
            ufsCustomerComplaints.setAgentPhonenumber(this.customerService.findByCustomerId(customerComplaints.getCustomerId()).getPhonenumber());
        }else{
            throw  new NotFoundException("Customer Does Not Exist in The System");
        }

        if(Objects.nonNull(this.geographicalRegionService.findByGeographicalId(customerComplaints.getGeographicalRegionId()))){
            ufsCustomerComplaints.setAgentLocation(this.geographicalRegionService.findByGeographicalId(customerComplaints.getGeographicalRegionId()).getRegionName());
        }else{
            throw  new NotFoundException("Region Does Not Exist in The System");
        }
         ufsCustomerComplaints.setComplaintNature(customerComplaints.getComplaintNature());
        ufsCustomerComplaints.setComplaints(customerComplaints.getComplaints());
        ufsCustomerComplaints.setDateOfOccurence(customerComplaints.getDateOfOccurence());
        ufsCustomerComplaints.setRemedialActions(customerComplaints.getRemedialActions());
        customerComplaintsService.saveComplaint(ufsCustomerComplaints);

        loggerService.log("Successfully Created Trained Agents",
                UfsCustomerComplaints.class.getSimpleName(), ufsCustomerComplaints.getId(), ke.axle.chassis.utils.AppConstants.ACTIVITY_CREATE, ke.axle.chassis.utils.AppConstants.STATUS_COMPLETED,"Creation");
        response.setData(ufsCustomerComplaints);
        response.setCode(201);
        response.setMessage("Customer Complaints Created Successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }



    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    @Transactional
    @ApiOperation(value = "Uploading Customer Complaints Report")
    public ResponseEntity<ResponseWrapper> upload(@Valid UfsCustomerComplaintsUpload complaintsUpload, HttpServletRequest request) {
        ResponseWrapper response = new ResponseWrapper();
        if(complaintsUpload.getFile() != null){

            //System.out.println("File uploaded --" + trainedAgent.getFile().getContentType());

            //Validate file extension
            if (!(complaintsUpload.getFile().getContentType().equalsIgnoreCase("text/csv")
                    || complaintsUpload.getFile().getContentType().equalsIgnoreCase("application/vnd.ms-excel")
                    || complaintsUpload.getFile().getContentType().equalsIgnoreCase("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))) {
                response.setCode(400);
                response.setMessage("Unsupported file type. Expects a CSV file");
                return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
            }

            String fileName = sysConfigRepository.uploadDir(AppConstants.ENTITY_GLOBAL_INTEGRATION,"fileUploadDir");

            UfsCustomerComplaintsBatch batch = new UfsCustomerComplaintsBatch();
            batch.setFileName(fileName);
//            batch.setUploadedBy(user);
            try {
                String fileUrl = sharedMethods.store(complaintsUpload.getFile(), fileName);
                batch.setFilePath(fileUrl);
                batch.setProcessingStatus(AppConstants.STATUS_STRING_PENDING);
                batch = customerComplaintsService.saveBatch(batch);
                if (complaintsUpload.getFile().getContentType().equalsIgnoreCase("text/csv")  || complaintsUpload.getFile().getOriginalFilename().endsWith(".csv")) {
                    this.customerComplaintsService.processCustomerComplaintsUpload(batch, configService,
                            sharedMethods, complaintsUpload.getFile().getBytes(),
                            request.getRemoteAddr(), StringUtils.abbreviate(request.getHeader("user-agent"), 100),complaintsUpload);
                }
            } catch (IOException ex) {
                log.error(AppConstants.AUDIT_LOG, "Encountered an error while writing file to directory", ex);
                response.setCode(500);
                response.setMessage("An internal server error occured while uploading trained agents file");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } else {
            response.setCode(400);
            response.setMessage("Expects all data or a CSV file of that contains all data");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @org.springframework.transaction.annotation.Transactional
    @RequestMapping(method = RequestMethod.GET, path = "customer-complaints-template.csv")
    public ModelAndView exportGlsTemplate(HttpServletRequest request) {
        CsvFlexView view;
        String fileName = "Customer Complaints Template";
        view = new CsvFlexView(CustomerComplaintsDetails.class, new ArrayList(),
                fileName);
        return new ModelAndView(view);
    }
}
