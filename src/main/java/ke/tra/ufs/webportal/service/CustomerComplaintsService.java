package ke.tra.ufs.webportal.service;

import ke.tra.ufs.webportal.entities.UfsCustomerComplaints;
import ke.tra.ufs.webportal.entities.UfsCustomerComplaintsBatch;
import ke.tra.ufs.webportal.entities.UfsTrainedAgentsBatch;
import ke.tra.ufs.webportal.entities.wrapper.UfsCustomerComplaintsUpload;
import ke.tra.ufs.webportal.entities.wrapper.UfsTrainedAgentsUpload;
import ke.tra.ufs.webportal.utils.SharedMethods;

public interface CustomerComplaintsService {

     UfsCustomerComplaints saveComplaint(UfsCustomerComplaints customerComplaints);

     UfsCustomerComplaintsBatch saveBatch(UfsCustomerComplaintsBatch customerComplaintsBatch);

     public void processCustomerComplaintsUpload(UfsCustomerComplaintsBatch batch, SysConfigService configService, SharedMethods sharedMethods, byte[] file, String remoteAddress,
                                            String userAgent, UfsCustomerComplaintsUpload customerComplaintsUpload);

}
