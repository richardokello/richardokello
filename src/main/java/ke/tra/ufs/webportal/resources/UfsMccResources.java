package ke.tra.ufs.webportal.resources;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.AppConstants;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.tra.ufs.webportal.entities.UfsEdittedRecord;
import ke.tra.ufs.webportal.entities.UfsMcc;
import ke.tra.ufs.webportal.entities.wrapper.MccUploadWrapper;
import ke.tra.ufs.webportal.repository.UfsMccRepository;
import ke.tra.ufs.webportal.service.UfsMccService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping(value = "/mcc")
public class UfsMccResources extends ChasisResource<UfsMcc, BigDecimal, UfsEdittedRecord> {

    private final UfsMccRepository ufsMccRepository;
    private final UfsMccService ufsMccService;

    public UfsMccResources(LoggerService loggerService, EntityManager entityManager, UfsMccRepository ufsMccRepository, UfsMccService ufsMccService) {
        super(loggerService, entityManager);
        this.ufsMccRepository = ufsMccRepository;
        this.ufsMccService = ufsMccService;
    }


    @Override
    @Transactional
    public ResponseEntity<ResponseWrapper<UfsMcc>> create(@Valid @RequestBody UfsMcc ufsMcc) {
        ResponseWrapper<UfsMcc> responseWrapper = new ResponseWrapper<>();

        List<UfsMcc> ufsMcc1 = ufsMccRepository.findByNameAndIntrash(ufsMcc.getName(), AppConstants.NO);
        if (ufsMcc1.size() > 0) {
            responseWrapper.setCode(HttpStatus.CONFLICT.value());
            responseWrapper.setMessage(ufsMcc.getName() + " Mcc Name already exist");
            return new ResponseEntity<>(responseWrapper, HttpStatus.CONFLICT);
        }

        List<UfsMcc> ufsValue = ufsMccRepository.findByValueAndIntrash(ufsMcc.getValue(), AppConstants.NO);
        if (ufsValue.size() > 0) {
            responseWrapper.setCode(HttpStatus.CONFLICT.value());
            responseWrapper.setMessage(ufsMcc.getValue() + " Code already exist");
            return new ResponseEntity<>(responseWrapper, HttpStatus.CONFLICT);
        }

        return super.create(ufsMcc);
    }

    @Transactional
    @ApiOperation(value = "Upload MCC", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad Request as a result of validation errors")
            ,
            @ApiResponse(code = 409, message = "Similar mcc exists")
    })
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ResponseEntity<ResponseWrapper> uploadMcc(@Valid MccUploadWrapper payload, BindingResult validation) throws IOException, InvalidFormatException {
        ResponseWrapper response = new ResponseWrapper();
        System.out.println("File uploaded --" + payload.getFile().getContentType());
        if (!(payload.getFile().getContentType().equalsIgnoreCase("application/vnd.ms-excel")
                || payload.getFile().getContentType().equalsIgnoreCase("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))) {
            response.setCode(400);
            response.setMessage("Unsupported file type. Expects a CSV or xls file");
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }

        ufsMccService.processFileUpload(payload.getFile());
        response.setCode(HttpStatus.CREATED.value());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
