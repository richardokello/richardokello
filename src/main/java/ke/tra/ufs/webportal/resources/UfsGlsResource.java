package ke.tra.ufs.webportal.resources;


import com.fasterxml.jackson.databind.ObjectMapper;
import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.tra.ufs.webportal.entities.UfsEdittedRecord;
import ke.tra.ufs.webportal.entities.UfsGls;
import ke.tra.ufs.webportal.repository.UfsGlsRepository;
import ke.tra.ufs.webportal.service.SysConfigService;
import ke.tra.ufs.webportal.utils.AppConstants;
import ke.tra.ufs.webportal.utils.SharedMethods;
import ke.tra.ufs.webportal.utils.exports.CsvFlexView;
import ke.tra.ufs.webportal.wrappers.UfsGlsDetails;
import ke.tra.ufs.webportal.wrappers.UfsGlsWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@RestController
@RequestMapping(value = "/gls")
public class UfsGlsResource extends ChasisResource<UfsGls, Long, UfsEdittedRecord> {

    private final SysConfigService configService;
    private final SharedMethods sharedMethods;
    private final UfsGlsRepository ufsGlsRepository;

    public UfsGlsResource(LoggerService loggerService, EntityManager entityManager, SysConfigService configService, SharedMethods sharedMethods, UfsGlsRepository ufsGlsRepository) {
        super(loggerService, entityManager);
        this.configService = configService;
        this.sharedMethods = sharedMethods;
        this.ufsGlsRepository = ufsGlsRepository;
    }


    @Transactional
    @RequestMapping(method = RequestMethod.POST,path = "upload")
    public ResponseEntity<ResponseWrapper<UfsGls>> create(UfsGlsWrapper ufsGlsWrapper) {
        ResponseEntity<ResponseWrapper<UfsGls>> ufsgls = null;
        if (ufsGlsWrapper.getFile() != null) {
            ResponseWrapper response = new ResponseWrapper();
            //Validate file extension
            if (!(ufsGlsWrapper.getFile().getContentType().equalsIgnoreCase("text/csv")
                    || ufsGlsWrapper.getFile().getContentType().equalsIgnoreCase("application/vnd.ms-excel")
                    || ufsGlsWrapper.getFile().getContentType().equalsIgnoreCase("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))) {
                response.setCode(400);
                response.setMessage("Unsupported file type. Expects a CSV file");
                return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
            }

            String fileName = configService.fetchSysConfigById(new BigDecimal(24)).getValue();
            try {
                sharedMethods.store(ufsGlsWrapper.getFile(), fileName);
                log.info("Content Type {}", ufsGlsWrapper.getFile().getContentType());
                if (ufsGlsWrapper.getFile().getContentType().equalsIgnoreCase("text/csv")) {
                    List<UfsGls> ufsGlsList = new ArrayList<>();
                    List<UfsGlsWrapper> entities = sharedMethods.convertCsv(UfsGlsWrapper.class, ufsGlsWrapper.getFile());
                    long failed = 0;
                    long success = 0;
                    for (UfsGlsWrapper entity : entities) {
                        UfsGls ufsGls1 = ufsGlsRepository.findByGlCodeAndIntrash(entity.getGlCode(), AppConstants.NO);
                        if (ufsGls1 != null) {
                            failed++;
                            continue;
                        }
                        log.info("Entities.............. {}", new ObjectMapper().writeValueAsString(entity));
                        UfsGls ufsGls = new UfsGls();
                        ufsGls.setGlName(entity.getGlName());
                        ufsGls.setGlCode(entity.getGlCode());
                        ufsGls.setGlAccountNumber(entity.getGlAccountNumber());
                        ufsGls.setGlLocation(entity.getGlLocation());
                        ufsGls.setBankIds(entity.getBankIds());
                        ufsGls.setBankBranchIds(entity.getBankBranchIds());
                        ufsGls.setTenantIds(entity.getTenantIds());

                        ufsGlsList.add(ufsGls);

                        success++;
                    }

                    response.setCode(HttpStatus.CREATED.value());
                    response.setTimestamp(Calendar.getInstance().getTimeInMillis());
                    response.setMessage("Content Saved Successfully");
                    response.setData(ufsGlsRepository.saveAll(ufsGlsList));

                    return new ResponseEntity<>(response, HttpStatus.OK);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ufsgls;
    }

    @Transactional
    @RequestMapping(method = RequestMethod.GET,path = "gls-template.csv")
    public ModelAndView exportGlsTemplate(HttpServletRequest request) {
        CsvFlexView view;
        String fileName = "Gls Template";
        view = new CsvFlexView(UfsGlsDetails.class, new ArrayList(),
                fileName);
        return new ModelAndView(view);
    }
}
