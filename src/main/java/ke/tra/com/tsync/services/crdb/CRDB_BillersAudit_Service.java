package ke.tra.com.tsync.services.crdb;


import ke.tra.com.tsync.repository.CRDBBillersAuditRepo;
import ke.tra.com.tsync.services.CoreProcessorService;
import ke.tra.com.tsync.wrappers.crdb.GepgControlNumberRequest;
import ke.tra.com.tsync.wrappers.crdb.GetControlNumberDetailsResponse;
import ke.tra.com.tsync.wrappers.crdb.PostGePGControlNumberPaymentRequest;
import ke.tra.com.tsync.wrappers.crdb.PostGepgControlNumberResponse;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;




@Service
public class CRDB_BillersAudit_Service {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(CRDB_BillersAudit_Service.class);

    @Autowired
    private CRDBBillersAuditRepo crdbBillersAuditRepo;


    @Async
    public  void logControlNumberPostingst(
            PostGePGControlNumberPaymentRequest postGePGControlNumberPaymentRequest
            , PostGepgControlNumberResponse postGepgControlNumberResponse){

// create entity ..save async
    }


    public  void logControlNumberInquiries(
            GepgControlNumberRequest gepgControlNumberRequest
            , GetControlNumberDetailsResponse postGepgControlNumberResponse){
// create entity ..save async
    }

}
