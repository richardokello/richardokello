package ke.tra.com.tsync.services.crdb;


import ke.tra.com.tsync.entities.CRDBBILLERS_AUDIT;
import ke.tra.com.tsync.repository.CRDBBillersAuditRepo;
import ke.tra.com.tsync.services.CoreProcessorService;
import ke.tra.com.tsync.wrappers.crdb.GepgControlNumberRequest;
import ke.tra.com.tsync.wrappers.crdb.GetControlNumberDetailsResponse;
import ke.tra.com.tsync.wrappers.crdb.PostGePGControlNumberPaymentRequest;
import ke.tra.com.tsync.wrappers.crdb.PostGepgControlNumberResponse;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Service
public class CRDB_BillersAudit_Service {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(CRDB_BillersAudit_Service.class);

    @Autowired
    private CRDBBillersAuditRepo crdbBillersAuditRepo;

    @Async
    public void logPostGePGControlNumberPaymentRequestAsync(
            PostGePGControlNumberPaymentRequest postGePGControlNumberPaymentRequest,String tid,String mid, String posref
    ) {
        try {
            crdbBillersAuditRepo.save(new CRDBBILLERS_AUDIT(postGePGControlNumberPaymentRequest,tid,mid,posref));
        } catch (Exception e) {
            LOGGER.error("logPostGePGControlNumberPaymentRequestAsync {}", e);
        }
    }

    @Async
    public void logPostGepgControlNumberResponseAsync(
            PostGepgControlNumberResponse postGepgControlNumberResponse,String tid,String mid, String posref) {
        try {
            crdbBillersAuditRepo.save(new CRDBBILLERS_AUDIT(postGepgControlNumberResponse,tid,mid,posref));
        } catch (Exception e) {
            LOGGER.error("LogPostGepgControlNumberResponseAsync {}", e);
        }
    }

    @Async
    public void logGepgControlNumberRequestAsync(GepgControlNumberRequest gepgControlNumberRequest
     ,String tid,String mid, String posref) {
        try {
            crdbBillersAuditRepo.save(new CRDBBILLERS_AUDIT(gepgControlNumberRequest,tid,mid,posref));
        } catch (Exception e) {
            LOGGER.error("logGepgControlNumberRequestAsync {}", e);
        }
    }


    @Async
    public void logCGetControlNumberDetailsResponseAsync(GetControlNumberDetailsResponse getControlNumberDetailsResponse ,String tid,String mid, String posref) {
        try {
            crdbBillersAuditRepo.save(new CRDBBILLERS_AUDIT(getControlNumberDetailsResponse,tid,mid,posref));
        } catch (Exception e) {
            LOGGER.error("logCGetControlNumberDetailsResponseAsync {}", e);
        }
    }

    //GetControlNumberDetailsResponse postGepgControlNumberResponse

}
