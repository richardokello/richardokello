package ke.tra.ufs.webportal.resources;


import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.tra.ufs.webportal.entities.UfsEdittedRecord;
import ke.tra.ufs.webportal.entities.UfsGls;
import ke.tra.ufs.webportal.repository.UfsGlsRepository;
import ke.tra.ufs.webportal.service.SysConfigService;
import ke.tra.ufs.webportal.utils.AppConstants;
import ke.tra.ufs.webportal.utils.SharedMethods;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
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
    @Override
    public ResponseEntity<ResponseWrapper<UfsGls>> create(UfsGls ufsGls) {
        ResponseEntity<ResponseWrapper<UfsGls>> ufsgls = null;
        if (ufsGls.getFile() != null) {
            ResponseWrapper response = new ResponseWrapper();
            //Validate file extension
            if (!(ufsGls.getFile().getContentType().equalsIgnoreCase("text/csv")
                    || ufsGls.getFile().getContentType().equalsIgnoreCase("application/vnd.ms-excel")
                    || ufsGls.getFile().getContentType().equalsIgnoreCase("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))) {
                response.setCode(400);
                response.setMessage("Unsupported file type. Expects a CSV file");
                return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
            }

            String fileName = configService.fetchSysConfigById(new BigDecimal(24)).getValue();
            try {
                sharedMethods.store(ufsGls.getFile(), fileName);
                if (ufsGls.getFile().getContentType().equalsIgnoreCase("text/csv")) {
                    List<UfsGls> ufsGlsList = new ArrayList<>();
                    List<UfsGls> entities = sharedMethods.convertCsv(UfsGls.class, ufsGls.getFile());
                    long failed = 0;
                    long success = 0;
                    System.out.println("Running...........................");
                    for (UfsGls entity : entities) {
                        UfsGls ufsGls1 = ufsGlsRepository.findByGlCodeAndIntrash(entity.getGlCode(), AppConstants.NO);
                        if (ufsGls1 != null) {
                            failed++;
                            continue;
                        }

                        ufsGls1.setGlName(entity.getGlName());
                        ufsGls1.setGlCode(entity.getGlCode());
                        ufsGls1.setGlAccountNumber(entity.getGlAccountNumber());
                        ufsGls1.setGlLocation(entity.getGlLocation());
                        ufsGls1.setBankIds(entity.getBankIds());
                        ufsGls1.setBankBranchIds(entity.getBankBranchIds());

                        ufsGlsList.add(ufsGls1);

                        success++;
                    }

                    response.setCode(HttpStatus.CREATED.value());
                    response.setTimestamp(Calendar.getInstance().getTimeInMillis());
                    response.setMessage("Content Saved Successfully");
                    response.setData(ufsGlsRepository.saveAll(ufsGlsList));

                    return new ResponseEntity<>(response,HttpStatus.OK);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            ufsgls =  super.create(ufsGls);
        }
            return  ufsgls;
        }
    }
