package ke.co.tra.ufs.tms.service;

import ke.co.tra.ufs.tms.entities.CrdbBillers;
import ke.co.tra.ufs.tms.entities.wrappers.InstitutionsResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;

public interface CrdbBillersService {

    /**
     * Filter crdb billers by date,and search needle
     * @param pg
     * @param needle
     * @return
     */
    public Page<CrdbBillers> getAllPendingRetriesBillers(String needle,Date from, Date to,Pageable pg);

    InstitutionsResponse getInstitutions();
}
