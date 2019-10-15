package ke.tra.ufs.webportal.resources;

import io.swagger.annotations.ApiOperation;
import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.exceptions.NotFoundException;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.tra.ufs.webportal.entities.*;
import ke.tra.ufs.webportal.entities.wrapper.TrainedAgentsDetails;
import ke.tra.ufs.webportal.entities.wrapper.UfsTrainedAgentMobileWrapper;
import ke.tra.ufs.webportal.entities.wrapper.UfsTrainedAgentWrapper;
import ke.tra.ufs.webportal.entities.wrapper.UfsTrainedAgentsUpload;
import ke.tra.ufs.webportal.repository.UfsSysConfigRepository;
import ke.tra.ufs.webportal.service.*;
import ke.tra.ufs.webportal.utils.AppConstants;
import ke.tra.ufs.webportal.utils.SharedMethods;
import ke.tra.ufs.webportal.utils.exports.CsvFlexView;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Objects;

@RestController
@RequestMapping("/trained-agents")
public class UfsTrainedAgentsResource extends ChasisResource<UfsTrainedAgents,Long, UfsEdittedRecord> {

   private final CustomerService customerService;
   private final UfsCustomerOutletService customerOutletService;
   private final TrainedAgentsService trainedAgentsService;
   private final GeographicalRegionService geographicalRegionService;
   private final UserService userService;
   private final SharedMethods sharedMethods;
    private final UfsSysConfigRepository sysConfigRepository;
    private final SysConfigService configService;

    public UfsTrainedAgentsResource(LoggerService loggerService, EntityManager entityManager,CustomerService customerService,UfsCustomerOutletService customerOutletService,
                                    TrainedAgentsService trainedAgentsService,GeographicalRegionService geographicalRegionService,UserService userService,SharedMethods sharedMethods,
                                    UfsSysConfigRepository sysConfigRepository,SysConfigService configService) {
        super(loggerService, entityManager);
        this.customerService = customerService;
        this.customerOutletService = customerOutletService;
        this.trainedAgentsService = trainedAgentsService;
        this.geographicalRegionService = geographicalRegionService;
        this.userService = userService;
        this.sharedMethods = sharedMethods;
        this.sysConfigRepository = sysConfigRepository;
        this.configService = configService;
    }




    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @Transactional
    @ApiOperation(value = "Creating Training Report")
    public ResponseEntity<ResponseWrapper> create(@Valid UfsTrainedAgentWrapper trainedAgent) throws NotFoundException {
        ResponseWrapper response = new ResponseWrapper();

           UfsTrainedAgents ufsTrainedAgents = new UfsTrainedAgents();
           if(Objects.nonNull(this.customerService.findByCustomerId(trainedAgent.getCustomerId()))){
            ufsTrainedAgents.setAgentName(this.customerService.findByCustomerId(trainedAgent.getCustomerId()).getCustomerName());
           }else{
               throw  new NotFoundException("Customer Does Not Exist in The System");
           }

           if(Objects.nonNull(this.geographicalRegionService.findByGeographicalId(trainedAgent.getGeographicalRegionId()))){
               ufsTrainedAgents.setRegion(this.geographicalRegionService.findByGeographicalId(trainedAgent.getGeographicalRegionId()).getRegionName());
           }else{
               throw  new NotFoundException("Region Does Not Exist in The System");
           }

           if(Objects.nonNull(this.customerOutletService.findByCustomerId(new BigDecimal(trainedAgent.getCustomerId())))){
               ufsTrainedAgents.setOutletName(this.customerOutletService.findByCustomerId(new BigDecimal(trainedAgent.getCustomerId())).getOutletName());
           }else{
               throw  new NotFoundException("Outlet Does Not Exist in The System");
           }

           if(Objects.nonNull(this.userService.findByUserId(trainedAgent.getAgentSupervisorId()))){
               ufsTrainedAgents.setAgentSupervisor(this.userService.findByUserId(trainedAgent.getAgentSupervisorId()).getFullName());
           }else{
               throw  new NotFoundException("Agent Supervisor Does Not Exist in The System");
           }

           ufsTrainedAgents.setTitle(trainedAgent.getTitle());
           ufsTrainedAgents.setDescription(trainedAgent.getDescription());
           ufsTrainedAgents.setTrainingDate(trainedAgent.getTrainingDate());
           trainedAgentsService.save(ufsTrainedAgents);
           loggerService.log("Successfully Created Trained Agents",
                   UfsTrainedAgents.class.getSimpleName(), ufsTrainedAgents.getId(), ke.axle.chassis.utils.AppConstants.ACTIVITY_CREATE, ke.axle.chassis.utils.AppConstants.STATUS_COMPLETED,"Creation");
           response.setData(ufsTrainedAgents);
           response.setCode(201);
           response.setMessage("Agents Trained Created Successfully");
           return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }



    @RequestMapping(value = "/mobile/add",method = RequestMethod.POST)
    @Transactional
    @ApiOperation(value = "Creating Training Agent Report")
    public ResponseEntity<ResponseWrapper> createTrainedAgentsFromMobile(@Valid UfsTrainedAgentMobileWrapper trainedAgent) throws NotFoundException {
        ResponseWrapper response = new ResponseWrapper();

        UfsTrainedAgents ufsTrainedAgents = new UfsTrainedAgents();

        if(Objects.nonNull(this.customerService.findByOutletCode(trainedAgent.getOutletCode()))){
            UfsCustomerOutlet customerOutletDb = this.customerService.findByOutletCode(trainedAgent.getOutletCode());
            //save customer Name
            if(Objects.nonNull(this.customerService.findByCustomerId(customerOutletDb.getCustomerIds().longValue()))){
                ufsTrainedAgents.setAgentName(this.customerService.findByCustomerId(customerOutletDb.getCustomerIds().longValue()).getCustomerName());
            }else{
                throw  new NotFoundException("Customer Does Not Exist in The System");
            }

            //save Geographical Region
            if(Objects.nonNull(this.geographicalRegionService.findByGeographicalId(customerOutletDb.getGeographicalRegionIds()))){
                ufsTrainedAgents.setRegion(this.geographicalRegionService.findByGeographicalId(customerOutletDb.getGeographicalRegionIds()).getRegionName());
            }else{
                throw  new NotFoundException("Region Does Not Exist in The System");
            }
            //save outlet name
           ufsTrainedAgents.setOutletName(customerOutletDb.getOutletName());

            //save agent supervisor
            if(Objects.nonNull(this.userService.findByUserId(trainedAgent.getAgentSupervisorId()))){
                ufsTrainedAgents.setAgentSupervisor(this.userService.findByUserId(trainedAgent.getAgentSupervisorId()).getFullName());
            }else{
                throw  new NotFoundException("Agent Supervisor Does Not Exist in The System");
            }
        }else{
            throw  new NotFoundException("Outlet Code Does Not Exist in The System");
        }

        ufsTrainedAgents.setTitle(trainedAgent.getTitle());
        ufsTrainedAgents.setDescription(trainedAgent.getDescription());
        ufsTrainedAgents.setTrainingDate(trainedAgent.getTrainingDate());
        trainedAgentsService.save(ufsTrainedAgents);
        loggerService.log("Successfully Created Trained Agents",
                UfsTrainedAgents.class.getSimpleName(), ufsTrainedAgents.getId(), ke.axle.chassis.utils.AppConstants.ACTIVITY_CREATE, ke.axle.chassis.utils.AppConstants.STATUS_COMPLETED,"Creation");
        response.setData(ufsTrainedAgents);
        response.setCode(201);
        response.setMessage("Agents Trained Created Successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }


    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    @Transactional
    @ApiOperation(value = "Uploading Agent Training Report")
    public ResponseEntity<ResponseWrapper> upload(@Valid UfsTrainedAgentsUpload trainedAgentsUpload, HttpServletRequest request) {
        ResponseWrapper response = new ResponseWrapper();
        if(trainedAgentsUpload.getFile() != null){

            //System.out.println("File uploaded --" + trainedAgent.getFile().getContentType());

            //Validate file extension
            if (!(trainedAgentsUpload.getFile().getContentType().equalsIgnoreCase("text/csv")
                    || trainedAgentsUpload.getFile().getContentType().equalsIgnoreCase("application/vnd.ms-excel")
                    || trainedAgentsUpload.getFile().getContentType().equalsIgnoreCase("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))) {
                response.setCode(400);
                response.setMessage("Unsupported file type. Expects a CSV file");
                return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
            }

            String fileName = sysConfigRepository.uploadDir(AppConstants.ENTITY_GLOBAL_INTEGRATION,"fileUploadDir");

            UfsTrainedAgentsBatch batch = new UfsTrainedAgentsBatch();
            batch.setFileName(fileName);
//            batch.setUploadedBy(user);
            try {
                String fileUrl = sharedMethods.store(trainedAgentsUpload.getFile(), fileName);
                batch.setFilePath(fileUrl);
                batch.setProcessingStatus(AppConstants.STATUS_STRING_PENDING);
                batch = trainedAgentsService.saveBatch(batch);
                if (trainedAgentsUpload.getFile().getContentType().equalsIgnoreCase("text/csv")  || trainedAgentsUpload.getFile().getOriginalFilename().endsWith(".csv")) {
                    this.trainedAgentsService.processTrainedAgentsUpload(batch, configService,
                            sharedMethods, trainedAgentsUpload.getFile().getBytes(),
                            request.getRemoteAddr(), StringUtils.abbreviate(request.getHeader("user-agent"), 100),trainedAgentsUpload);
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

    @Transactional
    @RequestMapping(method = RequestMethod.GET, path = "trained-agents-template.csv")
    public ModelAndView exportTrainedAgentsTemplate(HttpServletRequest request) {
        CsvFlexView view;
        String fileName = "Trained Agents Template";
        view = new CsvFlexView(TrainedAgentsDetails.class, new ArrayList(),
                fileName);
        return new ModelAndView(view);
    }
}
