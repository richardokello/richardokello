package ke.tra.ufs.webportal.resources;

import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.tra.ufs.webportal.service.template.ReportsServiceTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class ReportResources {

    private final ReportsServiceTemplate reportsServiceTemplate;

    public ReportResources(ReportsServiceTemplate reportsServiceTemplate) {
        this.reportsServiceTemplate = reportsServiceTemplate;
    }

    @RequestMapping(value = "/report_resources", method = RequestMethod.GET)
    public ResponseEntity<ResponseWrapper> getReportPerRegion() {
        ResponseWrapper responseWrapper = reportsServiceTemplate.generateGeographicalReports();
        return new ResponseEntity<>(responseWrapper, HttpStatus.OK);
    }
}
