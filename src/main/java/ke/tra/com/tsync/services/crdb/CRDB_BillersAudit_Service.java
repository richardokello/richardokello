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
            PostGePGControlNumberPaymentRequest postGePGControlNumberPaymentRequest,
            String tid,
            String mid,
            String posref,
            String switchauthcode,
            Integer retriesCount,
            Integer httpcode,
            String requestSendDesc,
            Integer isOriginalRequest
    ) {
        try {
            crdbBillersAuditRepo.save(new CRDBBILLERS_AUDIT(
                    postGePGControlNumberPaymentRequest,
                    tid,
                    mid,
                    posref,
                    switchauthcode,
                    retriesCount,
                    httpcode,
                    requestSendDesc,
                    isOriginalRequest
                    )
            );
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("logPostGePGControlNumberPaymentRequestAsync {}", e);
        }
    }


    @Async
    public void logPostGePGControlNumberPaymentRequestAsync(
            PostGePGControlNumberPaymentRequest postGePGControlNumberPaymentRequest,
            String tid,
            String mid,
            String posref,
            String switchauthcode,
            Integer retriesCount,
            Integer httpcode,
            String requestSendDesc
    ) {
        try {
            crdbBillersAuditRepo.save(new CRDBBILLERS_AUDIT(
                    postGePGControlNumberPaymentRequest,
                    tid,
                    mid,
                    posref,
                    switchauthcode,
                    retriesCount,
                            httpcode,
                            requestSendDesc)
            );
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("logPostGePGControlNumberPaymentRequestAsync {}", e);
        }
    }

    @Async
    public void logPostGepgControlNumberResponseAsync(
            PostGepgControlNumberResponse postGepgControlNumberResponse,
            String tid,String mid, String posref,String status,String statusDesc) {
        try {
            crdbBillersAuditRepo.save(new CRDBBILLERS_AUDIT(
                    postGepgControlNumberResponse,tid,mid,posref,status,statusDesc));
        } catch (Exception e) {
            LOGGER.error("LogPostGepgControlNumberResponseAsync {}", e);
        }
    }

    @Async
    public void logGepgControlNumberRequestAsync(
            GepgControlNumberRequest gepgControlNumberRequest,
     String tid,String mid, String posref, Integer retriesCount,
    Integer httpcode, String requestSendDesc
    ) {
        try {
            crdbBillersAuditRepo.save(
                new CRDBBILLERS_AUDIT(
                        gepgControlNumberRequest
                        ,tid
                        ,mid
                        ,posref
                        ,retriesCount
                        ,httpcode
                        ,requestSendDesc
                )
            );
        } catch (Exception e) {
            LOGGER.error("logGepgControlNumberRequestAsync Error {}", e);
        }

    }

    @Async
    public void logCRDBBillersLogEntity(CRDBBILLERS_AUDIT crdbbillersAudit){
        try {
            crdbBillersAuditRepo.save(crdbbillersAudit);
        }catch (Exception e){
            LOGGER.error("logCRDBBillersLogEntity {} ", e);
        }
    }


    @Async
    public void logCGetControlNumberDetailsResponseAsync(
            GetControlNumberDetailsResponse getControlNumberDetailsResponse,
            String tid,String mid, String posref,
            String status,String statusDesc) {
        try {
            crdbBillersAuditRepo.save(
                    new CRDBBILLERS_AUDIT(getControlNumberDetailsResponse,
                            tid
                            ,mid
                            ,posref,status,statusDesc
                    )
            );
        } catch (Exception e) {
            LOGGER.error("logCGetControlNumberDetailsResponseAsync {}", e);
        }
    }

    //GetControlNumberDetailsResponse postGepgControlNumberResponse

}
