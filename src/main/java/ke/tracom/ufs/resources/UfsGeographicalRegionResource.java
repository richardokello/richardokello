package ke.tracom.ufs.resources;

import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ResponseWrapper;
import io.swagger.annotations.ApiOperation;
import ke.tracom.ufs.entities.UfsEdittedRecord;
import ke.tracom.ufs.entities.UfsGeographicalRegion;
import ke.tracom.ufs.entities.UfsRegionsBatch;
import ke.tracom.ufs.services.OrganizationService;
import ke.tracom.ufs.services.SysConfigService;
import ke.tracom.ufs.utils.AppConstants;
import ke.tracom.ufs.utils.RegionDetails;
import ke.tracom.ufs.utils.SharedMethods;
import ke.tracom.ufs.utils.exports.CsvFlexView;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

@Controller
@RequestMapping(value = "/geographical-region")
public class UfsGeographicalRegionResource extends ChasisResource<UfsGeographicalRegion, BigDecimal, UfsEdittedRecord> {
    private final SharedMethods sharedMethods;
    private final SysConfigService configService;
    private final OrganizationService organizationService;

    public UfsGeographicalRegionResource(LoggerService loggerService, EntityManager entityManager, SharedMethods sharedMethods, SysConfigService configService, OrganizationService organizationService) {
        super(loggerService, entityManager);
        this.sharedMethods = sharedMethods;
        this.configService = configService;
        this.organizationService = organizationService;
    }

    @Override
    public ResponseEntity<ResponseWrapper<UfsGeographicalRegion>> create(@Valid @RequestBody UfsGeographicalRegion ufsGeographicalRegion) {
        ResponseWrapper<UfsGeographicalRegion> response = new ResponseWrapper<>();
        //check if its a file
        if (ufsGeographicalRegion.getFile() != null) {
            if (!(ufsGeographicalRegion.getFile().getContentType().equalsIgnoreCase("text/csv")
                    || ufsGeographicalRegion.getFile().getContentType().equalsIgnoreCase("application/vnd.ms-excel")
                    || ufsGeographicalRegion.getFile().getContentType().equalsIgnoreCase("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))) {
                response.setCode(400);
                response.setMessage("Unsupported file type. Expects a CSV file");
                return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
            }


            String fileName = configService.fetchSysConfigById(new BigDecimal(24)).getValue();
            UfsRegionsBatch batch = new UfsRegionsBatch();
            batch.setFileName(fileName);
            batch.setBatchType("Ge");
            batch.setUploadedBy(sharedMethods.getUser());
            try {
                String fileUrl = sharedMethods.store(ufsGeographicalRegion.getFile(), fileName);
                batch.setFilePath(fileUrl);
                batch.setProcessingStatus(AppConstants.STATUS_STRING_PENDING);
                batch = organizationService.saveBatch(batch);
                if (ufsGeographicalRegion.getFile().getContentType().equalsIgnoreCase("text/csv")) {
                    this.organizationService.processRegionsUploadCsv(batch, configService,
                            sharedMethods, loggerService, sharedMethods.getUser(), ufsGeographicalRegion);
                } else {
                    this.organizationService.processRegionsUploadXlxs(batch, configService,
                            sharedMethods, loggerService, sharedMethods.getUser(), ufsGeographicalRegion);
                }
            } catch (IOException ex) {
                log.error(AppConstants.AUDIT_LOG, "Encountered an error while writing file to directory", ex);
                loggerService.log("Uploading Device serial numbers failed. File cannot be written on the server",
                        SharedMethods.getEntityName(UfsRegionsBatch.class),
                        null, sharedMethods.getUser().getUserId(), "", "FAILED", ex.getMessage());
                //batch.setProcessingStatus(AppConstants.STATUS_FAILED);
                response.setCode(500);
                response.setMessage("An internal server error occured while uploading device whitelist file");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            return super.create(ufsGeographicalRegion);
        }
    }

    @ApiOperation(value = "Download Region template")
    @RequestMapping(value = "/region-template.csv", method = RequestMethod.GET)
    public ModelAndView exportVehicleTemplate(HttpServletRequest request) {
        CsvFlexView view;
        String fileName = "Region Template";
        view = new CsvFlexView(RegionDetails.class, new ArrayList(),
                fileName);
        return new ModelAndView(view);
    }
}
