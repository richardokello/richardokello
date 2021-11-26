/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.controller;

import io.swagger.annotations.Api;
import ke.co.tra.ufs.tms.entities.wrappers.Dashboard;
import ke.co.tra.ufs.tms.entities.wrappers.DashboardItemsWrapper;
import ke.co.tra.ufs.tms.entities.wrappers.MultiItemWrapper;
import ke.co.tra.ufs.tms.service.*;
import ke.co.tra.ufs.tms.utils.AppConstants;
import ke.co.tra.ufs.tms.wrappers.ResponseWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Owori Juma
 */
@RestController
@Api(value = "Authorization", description = "Used to handle authorization request such otp")
public class AuthorizationResource {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final LoggerServiceLocal logger;
    private final NotificationService notifyService;
    private final SysConfigService sysConfigService;
    private final ConfigService configService;
    private final SchedulerService schedulerService;

//    @Autowired
//    private TokenStore tokenStore;

    private final TmsDeviceHeartbeatService deviceHeartbeatService;
    private final DeviceService deviceService;

    public AuthorizationResource(LoggerServiceLocal loggerService,
                                 NotificationService notifyService, SysConfigService sysConfigService, ConfigService configService, TmsDeviceHeartbeatService deviceHeartbeatService, DeviceService deviceService, SchedulerService schedulerService) {
        this.logger = loggerService;
        this.notifyService = notifyService;
        this.sysConfigService = sysConfigService;
        this.configService = configService;
        this.deviceHeartbeatService = deviceHeartbeatService;
        this.deviceService = deviceService;
        this.schedulerService = schedulerService;
    }


    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public ResponseEntity<ResponseWrapper<Dashboard>> dashboard(Authentication a, HttpServletRequest request) {
        ResponseWrapper response = new ResponseWrapper();
        List<DashboardItemsWrapper> single = new ArrayList<>();
        //single.add(new DashboardItemsWrapper("Todays Heartbeats", deviceHeartbeatService.findTodaysHeartbeats()));

        single.add(new DashboardItemsWrapper("Active Terminals", deviceService.findAllActiveDevices()));
//        single.add(new DashboardItemsWrapper("Total Terminals", deviceService.findActiveAll()));
        single.add(new DashboardItemsWrapper("Total Terminals", deviceService.findAllWhitelistedDevices()));

//        single.add(new DashboardItemsWrapper("Upcoming Schedules", schedulerService.findPendingSchedule()));
        single.add(new DashboardItemsWrapper("Upcoming Schedules", schedulerService.pendingSchedules()));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd");
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime minus1 = LocalDateTime.now().minusDays(1);
        LocalDateTime minus2 = LocalDateTime.now().minusDays(2);
        LocalDateTime minus3 = LocalDateTime.now().minusDays(3);
        LocalDateTime minus4 = LocalDateTime.now().minusDays(4);


        /*List<DashboardItemsWrapper> multiHeartbeats = new ArrayList<>();
        multiHeartbeats.add(new DashboardItemsWrapper(minus4.format(formatter), deviceHeartbeatService.findAdaysHeartbeats(minus4)));
        multiHeartbeats.add(new DashboardItemsWrapper(minus3.format(formatter), deviceHeartbeatService.findAdaysHeartbeats(minus3)));
        multiHeartbeats.add(new DashboardItemsWrapper(minus2.format(formatter), deviceHeartbeatService.findAdaysHeartbeats(minus2)));
        multiHeartbeats.add(new DashboardItemsWrapper(minus1.format(formatter), deviceHeartbeatService.findAdaysHeartbeats(minus1)));
        multiHeartbeats.add(new DashboardItemsWrapper(now.format(formatter), deviceHeartbeatService.findTodaysHeartbeats()));*/

        List<DashboardItemsWrapper> pendingDownloads = new ArrayList<>();
        pendingDownloads.add(new DashboardItemsWrapper(minus4.format(formatter), schedulerService.findPendingTasksByDay(minus4)));
        pendingDownloads.add(new DashboardItemsWrapper(minus3.format(formatter), schedulerService.findPendingTasksByDay(minus3)));
        pendingDownloads.add(new DashboardItemsWrapper(minus1.format(formatter), schedulerService.findPendingTasksByDay(minus2)));
        pendingDownloads.add(new DashboardItemsWrapper(minus1.format(formatter), schedulerService.findPendingTasksByDay(minus1)));
        pendingDownloads.add(new DashboardItemsWrapper(now.format(formatter), schedulerService.findPendingTasksByDay(now)));


        List<DashboardItemsWrapper> downloadTasks = new ArrayList<>();
        downloadTasks.add(new DashboardItemsWrapper(minus4.format(formatter), schedulerService.findAllTaskByDay(minus4)));
        downloadTasks.add(new DashboardItemsWrapper(minus3.format(formatter), schedulerService.findAllTaskByDay(minus3)));
        downloadTasks.add(new DashboardItemsWrapper(minus2.format(formatter), schedulerService.findAllTaskByDay(minus2)));
        downloadTasks.add(new DashboardItemsWrapper(minus1.format(formatter), schedulerService.findAllTaskByDay(minus1)));
        downloadTasks.add(new DashboardItemsWrapper(now.format(formatter), schedulerService.findAllTaskByDay(now)));

        List<DashboardItemsWrapper> downloadedTasks = new ArrayList<>();
        downloadedTasks.add(new DashboardItemsWrapper(minus4.format(formatter), schedulerService.findDownloadedTaskByDay(minus4)));
        downloadedTasks.add(new DashboardItemsWrapper(minus3.format(formatter), schedulerService.findDownloadedTaskByDay(minus3)));
        downloadedTasks.add(new DashboardItemsWrapper(minus2.format(formatter), schedulerService.findDownloadedTaskByDay(minus2)));
        downloadedTasks.add(new DashboardItemsWrapper(minus1.format(formatter), schedulerService.findDownloadedTaskByDay(minus1)));
        downloadedTasks.add(new DashboardItemsWrapper(now.format(formatter), schedulerService.findDownloadedTaskByDay(now)));


        List<MultiItemWrapper> multi = new ArrayList<>();
        //multi.add(new MultiItemWrapper("Heartbeats", multiHeartbeats));
        multi.add(new MultiItemWrapper("Pending Downloads", pendingDownloads));
        multi.add(new MultiItemWrapper("Download Tasks", downloadTasks));
        multi.add(new MultiItemWrapper("Completed Tasks", downloadedTasks));

        Dashboard dashboard = new Dashboard();
        dashboard.setIsAdmin(request.isUserInRole(AppConstants.ROLE_ADMIN)).setIsOperator(request.isUserInRole(AppConstants.ROLE_OPERATOR));
        dashboard.setSingle(single);
        dashboard.setMulti(multi);
        response.setData(dashboard);
        return new ResponseEntity(response, HttpStatus.OK);
    }

//    @RequestMapping(value = "/user/me")
//    public ResponseEntity<Principal> user(Principal principal) {
//        return ResponseEntity.ok(principal);
//    }


}
