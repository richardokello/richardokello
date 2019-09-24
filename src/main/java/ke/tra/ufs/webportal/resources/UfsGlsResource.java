package ke.tra.ufs.webportal.resources;


import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.tra.ufs.webportal.entities.UfsEdittedRecord;
import ke.tra.ufs.webportal.entities.UfsGls;
import ke.tra.ufs.webportal.entities.UfsGlsBatch;
import ke.tra.ufs.webportal.repository.UfsGlsBatchRepository;
import ke.tra.ufs.webportal.repository.UfsGlsRepository;
import ke.tra.ufs.webportal.service.GlsBatchUploadService;
import ke.tra.ufs.webportal.service.SysConfigService;
import ke.tra.ufs.webportal.utils.AppConstants;
import ke.tra.ufs.webportal.utils.SharedMethods;
import ke.tra.ufs.webportal.utils.exports.CsvFlexView;
import ke.tra.ufs.webportal.wrappers.UfsGlsDetails;
import ke.tra.ufs.webportal.wrappers.UfsGlsWrapper;
import org.apache.commons.lang3.StringUtils;
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
import java.util.*;

@RestController
@RequestMapping(value = "/gls")
public class UfsGlsResource extends ChasisResource<UfsGls, Long, UfsEdittedRecord> {

    private final SysConfigService configService;
    private final  SharedMethods sharedMethods;
    private final UfsGlsRepository ufsGlsRepository;
    private final GlsBatchUploadService uploadService;
    private final UfsGlsBatchRepository ufsGlsBatchRepository;

    public UfsGlsResource(LoggerService loggerService, EntityManager entityManager, SysConfigService configService, SharedMethods sharedMethods, UfsGlsRepository ufsGlsRepository, GlsBatchUploadService uploadService, UfsGlsBatchRepository ufsGlsBatchRepository) {
        super(loggerService, entityManager);
        this.configService = configService;
        this.sharedMethods = sharedMethods;
        this.ufsGlsRepository = ufsGlsRepository;
        this.uploadService = uploadService;
        this.ufsGlsBatchRepository = ufsGlsBatchRepository;
    }


    @Transactional
    @RequestMapping(method = RequestMethod.POST, path = "upload")
    public ResponseEntity<ResponseWrapper> create(@Valid UfsGlsWrapper ufsGlsWrapper, HttpServletRequest request) {
        ResponseWrapper response = new ResponseWrapper();
        Map<String, Number> res = new HashMap<>();
        if (Objects.nonNull(ufsGlsWrapper.getFile())) {
            //Validate file extension
            if (!(ufsGlsWrapper.getFile().getContentType().equalsIgnoreCase("text/csv")
                    || ufsGlsWrapper.getFile().getContentType().equalsIgnoreCase("application/vnd.ms-excel")
                    || ufsGlsWrapper.getFile().getContentType().equalsIgnoreCase("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))) {
                response.setCode(400);
                response.setMessage("Unsupported file type. Expects a CSV file");
                return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
            }

            String fileName = configService.fetchSysConfigById(new BigDecimal(24)).getValue();
            UfsGlsBatch ufsGlsBatch = new UfsGlsBatch();
            ufsGlsBatch.setFileName(fileName);

            try {

                String fileUrl = sharedMethods.store(ufsGlsWrapper.getFile(), fileName);
                ufsGlsBatch.setFilePath(fileUrl);
                ufsGlsBatch.setProcessingStatus(AppConstants.STATUS_STRING_PENDING);
                ufsGlsBatch = ufsGlsBatchRepository.save(ufsGlsBatch);

                if (ufsGlsWrapper.getFile().getContentType().equalsIgnoreCase("text/csv") || ufsGlsWrapper.getFile().getOriginalFilename().endsWith(".csv")) {
                    uploadService.processGlsFileUpload(ufsGlsBatch,configService,sharedMethods,ufsGlsWrapper.getFile().getBytes(),
                            request.getRemoteAddr(), StringUtils.abbreviate(request.getHeader("user-agent"),100),ufsGlsWrapper);
                }

                }catch (IOException e) {
                e.printStackTrace();
            }

        }
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Transactional
    @RequestMapping(method = RequestMethod.GET, path = "gls-template.csv")
    public ModelAndView exportGlsTemplate(HttpServletRequest request) {
        CsvFlexView view;
        String fileName = "Gls Template";
        view = new CsvFlexView(UfsGlsDetails.class, new ArrayList(),
                fileName);
        return new ModelAndView(view);
    }
}
