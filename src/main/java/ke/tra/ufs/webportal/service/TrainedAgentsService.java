package ke.tra.ufs.webportal.service;


import ke.tra.ufs.webportal.entities.UfsTrainedAgents;
import ke.tra.ufs.webportal.entities.UfsTrainedAgentsBatch;
import ke.tra.ufs.webportal.entities.wrapper.UfsTrainedAgentsUpload;
import ke.tra.ufs.webportal.utils.SharedMethods;

public interface TrainedAgentsService {

    public UfsTrainedAgents save(UfsTrainedAgents trainedAgents);

    UfsTrainedAgentsBatch saveBatch(UfsTrainedAgentsBatch trainedAgentsBatch);

    public void processTrainedAgentsUpload(UfsTrainedAgentsBatch batch, SysConfigService configService, SharedMethods sharedMethods, byte[] file, String remoteAddress,
                                       String userAgent, UfsTrainedAgentsUpload trainedAgentsUpload);
}
