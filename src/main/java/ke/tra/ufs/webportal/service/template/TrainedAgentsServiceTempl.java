package ke.tra.ufs.webportal.service.template;

import ke.tra.ufs.webportal.entities.UfsTrainedAgents;
import ke.tra.ufs.webportal.entities.UfsTrainedAgentsBatch;
import ke.tra.ufs.webportal.entities.wrapper.UfsTrainedAgentsUpload;
import ke.tra.ufs.webportal.repository.TrainedAgentsBatchRepository;
import ke.tra.ufs.webportal.repository.UfsTrainedAgentsRepository;
import ke.tra.ufs.webportal.service.SysConfigService;
import ke.tra.ufs.webportal.service.TrainedAgentsService;
import ke.tra.ufs.webportal.utils.AppConstants;
import ke.tra.ufs.webportal.utils.SharedMethods;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class TrainedAgentsServiceTempl implements TrainedAgentsService {

    private final UfsTrainedAgentsRepository trainedAgentsRepository;
    private final TrainedAgentsBatchRepository trainedAgentsBatchRepository;
    private final Logger log = LoggerFactory.getLogger(TrainedAgentsServiceTempl.class);

    public TrainedAgentsServiceTempl(UfsTrainedAgentsRepository trainedAgentsRepository,TrainedAgentsBatchRepository trainedAgentsBatchRepository) {
        this.trainedAgentsRepository = trainedAgentsRepository;
        this.trainedAgentsBatchRepository = trainedAgentsBatchRepository;
    }

    @Override
    public UfsTrainedAgents save(UfsTrainedAgents trainedAgents) {
        return trainedAgentsRepository.save(trainedAgents);
    }

    @Override
    public UfsTrainedAgentsBatch saveBatch(UfsTrainedAgentsBatch trainedAgentsBatch) {
        return this.trainedAgentsBatchRepository.save(trainedAgentsBatch);
    }

    @Override
    @Async
    public void processTrainedAgentsUpload(UfsTrainedAgentsBatch batch, SysConfigService configService, SharedMethods sharedMethods, byte[] file, String remoteAddress, String userAgent, UfsTrainedAgentsUpload trainedAgentsUpload) {

        try {

            List<UfsTrainedAgentsUpload> entities = sharedMethods.convertCsv(UfsTrainedAgentsUpload.class, trainedAgentsUpload.getFile());
            for(UfsTrainedAgentsUpload entity : entities){
                UfsTrainedAgents ufsTrainedAgents = new UfsTrainedAgents();
                ufsTrainedAgents.setAgentName(entity.getCustomer());
                ufsTrainedAgents.setRegion(entity.getGeographicalRegion());
                ufsTrainedAgents.setOutletName(entity.getCustomerOutlet());
                ufsTrainedAgents.setAgentSupervisor(entity.getAgentSupervisor());
                ufsTrainedAgents.setTitle(entity.getTitle());
                ufsTrainedAgents.setDescription(entity.getDescription());
                ufsTrainedAgents.setTrainingDate(entity.getTrainingDate());
                trainedAgentsRepository.save(ufsTrainedAgents);
            }
            batch.setProcessingStatus(AppConstants.STATUS_COMPLETED);

        } catch (IOException e) {
            e.printStackTrace();
            log.error(AppConstants.AUDIT_LOG, "Processing Trained Agents upload failed", e);
            batch.setProcessingStatus(AppConstants.ACTIVITY_STATUS_FAILED);
        }

        trainedAgentsBatchRepository.save(batch);
    }
}
