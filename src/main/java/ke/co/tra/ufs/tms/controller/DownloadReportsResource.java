/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import ke.co.tra.ufs.tms.entities.TmsDeviceTask;
import ke.co.tra.ufs.tms.entities.TmsFtpLogs;
import ke.co.tra.ufs.tms.entities.TmsScheduler;
import ke.co.tra.ufs.tms.entities.TmsUpdateLogs;
import ke.co.tra.ufs.tms.entities.wrappers.filters.DeviceTaskFilter;
import ke.co.tra.ufs.tms.entities.wrappers.filters.DeviceUpdateFilter;
import ke.co.tra.ufs.tms.entities.wrappers.filters.ScheduleFilter;
import ke.co.tra.ufs.tms.repository.TmsUpdateLogsRepository;
import ke.co.tra.ufs.tms.service.DeviceService;
import ke.co.tra.ufs.tms.service.LoggerServiceLocal;
import ke.co.tra.ufs.tms.service.ProductService;
import ke.co.tra.ufs.tms.service.SchedulerService;
import ke.co.tra.ufs.tms.wrappers.ResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Optional;

/**
 * @author Owori Juma
 */
@Controller
@RequestMapping("/download-reports")
@Api(value = "devices Downloads Resource")
public class DownloadReportsResource {

    private final SchedulerService schedulerService;
    private final DeviceService deviceService;
    private final LoggerServiceLocal loggerService;
    private final ProductService productService;
    @Autowired
    private TmsUpdateLogsRepository tmsUpdateLogsRepository;

    public DownloadReportsResource(SchedulerService schedulerService, DeviceService deviceService, LoggerServiceLocal loggerService, ProductService productService) {
        this.schedulerService = schedulerService;
        this.deviceService = deviceService;
        this.loggerService = loggerService;
        this.productService = productService;
    }

    @ApiOperation(value = "Fetch Schedules Reports", notes = "Used to fetch Schedules")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", dataType = "integer", required = false, value = "Pagination size e.g 20")
            ,
            @ApiImplicitParam(name = "page", dataType = "integer", required = false, value = "Page number e.g 0")
            ,
            @ApiImplicitParam(name = "sort", dataType = "string", required = false, value = "Field name e.g status")
            ,
            @ApiImplicitParam(name = "dir", dataType = "integer", required = false, value = "Sorting direction e.g desc or asc")
    })
    @RequestMapping(method = RequestMethod.GET, value = "/schedule")
    public ResponseEntity<ResponseWrapper<Page<TmsScheduler>>> fetchSchedules(@Valid ScheduleFilter filter, Pageable pg) {
        ResponseWrapper response = new ResponseWrapper();
        System.out.println("...................................."+filter.getFrom_());
        response.setData(schedulerService.findAllUsingDate(filter.getActionStatus(), filter.getStatus(), filter.getAction(), filter.getDownloadType(), filter.getScheduleType(),filter.getFrom_(),filter.getTo_(),filter.getNeedle(), pg));
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @ApiOperation(value = "Fetch Device Tasks Reports", notes = "Used to fetch Device Tasks")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", dataType = "integer", required = false, value = "Pagination size e.g 20")
            ,
            @ApiImplicitParam(name = "page", dataType = "integer", required = false, value = "Page number e.g 0")
            ,
            @ApiImplicitParam(name = "sort", dataType = "string", required = false, value = "Field name e.g status")
            ,
            @ApiImplicitParam(name = "dir", dataType = "integer", required = false, value = "Sorting direction e.g desc or asc")
    })
    @RequestMapping(method = RequestMethod.GET, value = "/device-task")
    public ResponseEntity<ResponseWrapper<Page<TmsDeviceTask>>> fetchDeviceTasks(@Valid DeviceTaskFilter filter, Pageable pg) {
        ResponseWrapper response = new ResponseWrapper();
        response.setData(schedulerService.findDeviceTasks(filter.getDownloadStatus(), filter.getFrom(), filter.getTo(), filter.getDeviceId(), filter.getScheduleId(),filter.getNeedle(), pg));
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @ApiOperation(value = "Fetch Device Tasks Reports by Device Id", notes = "Used to fetch Device Tasks")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", dataType = "integer", required = false, value = "Pagination size e.g 20")
            ,
            @ApiImplicitParam(name = "page", dataType = "integer", required = false, value = "Page number e.g 0")
            ,
            @ApiImplicitParam(name = "sort", dataType = "string", required = false, value = "Field name e.g status")
            ,
            @ApiImplicitParam(name = "dir", dataType = "integer", required = false, value = "Sorting direction e.g desc or asc")
    })
    @RequestMapping(method = RequestMethod.GET, value = "/device-task/{deviceId}")
    public ResponseEntity<ResponseWrapper<Page<TmsDeviceTask>>> fetchDeviceTasksBySerial(@Valid DeviceTaskFilter filter, Pageable pg, @PathVariable("deviceId") String deviceId) {
        ResponseWrapper response = new ResponseWrapper();
        response.setData(schedulerService.findDeviceTasks(filter.getDownloadStatus(), filter.getFrom(), filter.getTo(), deviceId, filter.getScheduleId(), filter.getNeedle(), pg));
        return new ResponseEntity(response, HttpStatus.OK);
    }


    @ApiOperation(value = "Fetch Device Tasks Reports by Serial No", notes = "Used to fetch Device Tasks")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", dataType = "integer", required = false, value = "Pagination size e.g 20")
            ,
            @ApiImplicitParam(name = "page", dataType = "integer", required = false, value = "Page number e.g 0")
            ,
            @ApiImplicitParam(name = "sort", dataType = "string", required = false, value = "Field name e.g status")
            ,
            @ApiImplicitParam(name = "dir", dataType = "integer", required = false, value = "Sorting direction e.g desc or asc")
    })
    @RequestMapping(method = RequestMethod.GET, value = "/update-logs")
    public ResponseEntity<ResponseWrapper<Page<TmsUpdateLogs>>> fetchDeviceUpdateLogs(@Valid DeviceUpdateFilter filter, Pageable pg) {
        ResponseWrapper response = new ResponseWrapper();
        response.setData(tmsUpdateLogsRepository.findAll(filter.getStatus(), filter.getFrom(), filter.getTo(), filter.getDeviceId(), filter.getTaskId(), pg));
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @ApiOperation(value = "Fetch FTP Update Logs Reports by Device ID", notes = "Used to fetch FTP Logs")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", dataType = "integer", required = false, value = "Pagination size e.g 20")
            ,
            @ApiImplicitParam(name = "page", dataType = "integer", required = false, value = "Page number e.g 0")
            ,
            @ApiImplicitParam(name = "sort", dataType = "string", required = false, value = "Field name e.g status")
            ,
            @ApiImplicitParam(name = "dir", dataType = "integer", required = false, value = "Sorting direction e.g desc or asc")
    })
    @RequestMapping(method = RequestMethod.GET, value = "/ftp-logs/device/{id}")
    public ResponseEntity<ResponseWrapper<Page<TmsFtpLogs>>> fetchFTPUpdateLogs(@PathVariable("id") BigDecimal id, Pageable pg) {
        ResponseWrapper response = new ResponseWrapper();
        response.setData(deviceService.findByterminalSerial(deviceService.getDevice(id).get().getSerialNo(), pg));
        return new ResponseEntity(response, HttpStatus.OK);
    }


    @ApiOperation(value = "Fetch FTP Update Logs Reports by Log ID", notes = "Used to fetch FTP Logs")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", dataType = "integer", required = false, value = "Pagination size e.g 20")
            ,
            @ApiImplicitParam(name = "page", dataType = "integer", required = false, value = "Page number e.g 0")
            ,
            @ApiImplicitParam(name = "sort", dataType = "string", required = false, value = "Field name e.g status")
            ,
            @ApiImplicitParam(name = "dir", dataType = "integer", required = false, value = "Sorting direction e.g desc or asc")
    })
    @RequestMapping(method = RequestMethod.GET, value = "/ftp-logs/{id}")
    public ResponseEntity<ResponseWrapper<TmsFtpLogs>> fetchFTPUpdateLogsById(@PathVariable("id") BigDecimal id) {
        ResponseWrapper response = new ResponseWrapper();
        Optional<TmsFtpLogs> ftp = deviceService.findFtpLogsByLogId(id);
        if (ftp.isPresent()) {
            response.setData(ftp.get());
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }


}
