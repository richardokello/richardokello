package ke.tra.ufs.webportal.resources;

import ke.axle.chassis.wrappers.ResponseWrapper;
<<<<<<< HEAD
=======
import ke.tra.ufs.webportal.entities.wrapper.DashboardItemsWrapper;
>>>>>>> 23f67ebf5389b1cda1f0ffbf53570b86cc3801e6
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
