package ke.tra.com.tsync.services.crdb;

import ke.tra.com.tsync.repository.CRDBBillersAuditRepo;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CRDBRespostAdvices {

    @Autowired
    private CRDBBillersAuditRepo crdbBillersAuditRepo;
    // fetch original requests i.e requests whose value

    @Autowired private CRDBPipService crdbPipService;
    private static final org.slf4j.Logger repostLogger = LoggerFactory.getLogger(CRDBBillersAuditRepo.class);


    @Scheduled(fixedDelay = 30000)
    private void CheckUnsentAdvices(){
        crdbBillersAuditRepo.findByHttpcodeIsNullAndRequestNameAndRequestDirectionAndIsOrginalRequestAndSwitchAgentCodeIsNotNull(
                "POSTCONTROL", "REQUEST", 0).iterator()
                .forEachRemaining(
                        (crdbbillersAudit)->{
                            repostLogger.info("\n\ncrdbbillersAudit repost {}",crdbbillersAudit);
                            crdbPipService.postPEGPAdviceControlEnt(crdbbillersAudit);
                         /*   try {
                                Thread.sleep(200l);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }*/
                            //call function to resend
                        }
                );
    }




}
