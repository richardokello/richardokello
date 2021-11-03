package ke.tra.ufs.webportal.service.template;

import ke.axle.chassis.utils.LoggerService;
import ke.tra.ufs.webportal.entities.UfsCustomerComplaints;
import ke.tra.ufs.webportal.entities.UfsCustomerComplaintsBatch;
import ke.tra.ufs.webportal.entities.wrapper.CustomerComplaintsDetails;
import ke.tra.ufs.webportal.entities.wrapper.UfsCustomerComplaintsUpload;
import ke.tra.ufs.webportal.repository.CustomerComplaintsBatchRepository;
import ke.tra.ufs.webportal.repository.UfsCustomerComplaintsRepository;
import ke.tra.ufs.webportal.service.CustomerComplaintsService;
import ke.tra.ufs.webportal.service.SysConfigService;
import ke.tra.ufs.webportal.utils.AppConstants;
import ke.tra.ufs.webportal.utils.SharedMethods;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Service
public class CustomerComplaintsServiceTempl implements CustomerComplaintsService {

    private final UfsCustomerComplaintsRepository customerComplaintsRepository;
    private final CustomerComplaintsBatchRepository customerComplaintsBatchRepository;
    private final LoggerService loggerService;
    private final Logger log = LoggerFactory.getLogger(CustomerComplaintsServiceTempl.class);


    public CustomerComplaintsServiceTempl(UfsCustomerComplaintsRepository customerComplaintsRepository,LoggerService loggerService,
                                          CustomerComplaintsBatchRepository customerComplaintsBatchRepository) {
        this.customerComplaintsRepository = customerComplaintsRepository;
        this.loggerService = loggerService;
        this.customerComplaintsBatchRepository = customerComplaintsBatchRepository;
    }

    @Override
    public UfsCustomerComplaints saveComplaint(UfsCustomerComplaints customerComplaints) {
        return this.customerComplaintsRepository.save(customerComplaints);
    }

    @Override
    public UfsCustomerComplaintsBatch saveBatch(UfsCustomerComplaintsBatch customerComplaintsBatch) {
        return this.customerComplaintsBatchRepository.save(customerComplaintsBatch);
    }

    @Override
    @Async
    public void processCustomerComplaintsUpload(UfsCustomerComplaintsBatch batch, SysConfigService configService, SharedMethods sharedMethods, byte[] file, String remoteAddress, String userAgent, UfsCustomerComplaintsUpload customerComplaintsUpload) {

        try{

            List<CustomerComplaintsDetails> entities = sharedMethods.convertCsv(CustomerComplaintsDetails.class, customerComplaintsUpload.getFile());

            for(CustomerComplaintsDetails entity : entities){
                UfsCustomerComplaints customerComplaints = new UfsCustomerComplaints();
                customerComplaints.setComplaintNature(entity.getComplaintNature());
                customerComplaints.setComplaints(entity.getComplaints());
                customerComplaints.setAgentComplained(entity.getAgentName());
                customerComplaints.setAgentPhonenumber(entity.getPhoneNumber());
                customerComplaints.setAgentLocation(entity.getGeographicalRegion());
                customerComplaints.setDateOfOccurence(entity.getDateOfOccurence());
                customerComplaints.setRemedialActions(entity.getRemedialActions());
                customerComplaints.setBatchIds(batch.getBatchId());
                customerComplaintsRepository.save(customerComplaints);
                loggerService.log("Successfully Created Customer Complaints",
                        UfsCustomerComplaints.class.getSimpleName(), customerComplaints.getId(), ke.axle.chassis.utils.AppConstants.ACTIVITY_CREATE, ke.axle.chassis.utils.AppConstants.STATUS_COMPLETED,"Creation");
            }
            batch.setProcessingStatus(AppConstants.STATUS_COMPLETED);
            batch.setTimeCompleted(new Date());

        }catch(IOException e){
            e.printStackTrace();
            log.error(AppConstants.AUDIT_LOG, "Processing Customer Complaints upload failed", e);
            batch.setProcessingStatus(AppConstants.ACTIVITY_STATUS_FAILED);
        }

        customerComplaintsBatchRepository.save(batch);
    }

}
