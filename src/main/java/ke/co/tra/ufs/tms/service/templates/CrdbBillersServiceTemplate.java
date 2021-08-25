package ke.co.tra.ufs.tms.service.templates;

import ke.co.tra.ufs.tms.entities.CrdbBillers;
import ke.co.tra.ufs.tms.entities.wrappers.InstitutionsResponse;
import ke.co.tra.ufs.tms.repository.CrdbBillersRepository;
import ke.co.tra.ufs.tms.service.CrdbBillersService;
import ke.co.tra.ufs.tms.utils.AppConstants;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Date;


@Service
public class CrdbBillersServiceTemplate implements CrdbBillersService {

    @Value("${app.institutions.url}")
    private String institutionsUrl;

    @Value("${app.crdb.billers.username}")
    private String username;

    @Value("${app.crdb.billers.password}")
    private String password;

    private final CrdbBillersRepository crdbBillersRepository;

    public CrdbBillersServiceTemplate(CrdbBillersRepository crdbBillersRepository) {
        this.crdbBillersRepository = crdbBillersRepository;
    }

    @Override
    @Transactional
    public Page<CrdbBillers> getAllPendingRetriesBillers(String needle,Date from ,Date to,Pageable pg) {
        return crdbBillersRepository.findAllPendingRetries(AppConstants.STAPI_PAY_CONTROL_NUMBER_CODE, AppConstants.BILL_OWNER_IS_HOSPITAL,AppConstants.REQUEST_DIRECTION,needle,from,to,pg);
    }

    @Override
    public InstitutionsResponse getInstitutions() {

        String plainCreds = new StringBuilder(username)
                .append(":")
                .append(password).toString();
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Creds);

        HttpEntity<String> request = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<InstitutionsResponse> response = restTemplate.exchange(institutionsUrl, HttpMethod.GET, request, InstitutionsResponse.class);
        return response.getBody();
    }
}
