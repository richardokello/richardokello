package ke.tra.ufs.webportal.resources;

import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.tra.ufs.webportal.entities.wrapper.DashboardItemsWrapper;
import ke.tra.ufs.webportal.service.DashboardStatisticService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DashboardStatisticsResource {

    private final DashboardStatisticService dashboardStatisticService;

    DashboardStatisticsResource(DashboardStatisticService dashboardStatisticService){
       this.dashboardStatisticService = dashboardStatisticService;
    }

    @RequestMapping(path = "/webportal-stats", method = RequestMethod.GET)
    public ResponseEntity<ResponseWrapper<DashboardItemsWrapper>> getDashboardStatistics() {
        ResponseWrapper wrap = new ResponseWrapper();
        wrap.setData(this.dashboardStatisticService.getDashboardStatistics());
        return ResponseEntity.ok().body(wrap);
    }

}
