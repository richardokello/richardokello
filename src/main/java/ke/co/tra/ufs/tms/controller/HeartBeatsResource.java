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
import java.math.BigDecimal;
import java.util.List;
import javax.validation.Valid;
import ke.co.tra.ufs.tms.entities.TmsDeviceHeartbeat;
import ke.co.tra.ufs.tms.entities.wrappers.filters.DevicesFilter;
import ke.co.tra.ufs.tms.entities.wrappers.filters.HeartBeatsFilter;
import ke.co.tra.ufs.tms.service.DeviceService;
import ke.co.tra.ufs.tms.service.TmsDeviceHeartbeatService;
import ke.co.tra.ufs.tms.utils.exports.CsvFlexView;
import ke.co.tra.ufs.tms.utils.exports.ExcelFlexView;
import ke.co.tra.ufs.tms.utils.exports.PdfFlexView;
import ke.co.tra.ufs.tms.wrappers.ResponseWrapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Owori Juma
 */
@Controller
@RequestMapping("/heart-beat")
@Api(value = "devices Heart Beats Resource")
public class HeartBeatsResource {

    private final TmsDeviceHeartbeatService deviceHeartbeatService;
    private final DeviceService deviceService;

    public HeartBeatsResource(TmsDeviceHeartbeatService deviceHeartbeatService, DeviceService deviceService) {
        this.deviceHeartbeatService = deviceHeartbeatService;
        this.deviceService =  deviceService;
    }

    @ApiOperation(value = "Fetch Device Heartbeats", notes = "Used to fetch Device Heartbeats")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "size", dataType = "integer", required = false, value = "Pagination size e.g 20")
        ,
        @ApiImplicitParam(name = "page", dataType = "integer", required = false, value = "Page number e.g 0")
        ,
        @ApiImplicitParam(name = "sort", dataType = "string", required = false, value = "Field name e.g status")
        ,
        @ApiImplicitParam(name = "dir", dataType = "integer", required = false, value = "Sorting direction e.g desc or asc")
    })
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<ResponseWrapper<Page<TmsDeviceHeartbeat>>> fetchheartBeats(Pageable pg, @Valid HeartBeatsFilter filter) {
        ResponseWrapper response = new ResponseWrapper();
        response.setData(deviceHeartbeatService.findAll(filter.getApplicationVersion(), filter.getChargingStatus(), filter.getOsVersion(), filter.getSerialNo(), filter.getFrom(), filter.getTo(), filter.getNeedle(), pg));
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @ApiOperation(value = "Fetch Device Heartbeats by device Serial no", notes = "Used to fetch Device Heartbeats")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "size", dataType = "integer", required = false, value = "Pagination size e.g 20")
        ,
        @ApiImplicitParam(name = "page", dataType = "integer", required = false, value = "Page number e.g 0")
        ,
        @ApiImplicitParam(name = "sort", dataType = "string", required = false, value = "Field name e.g status")
        ,
        @ApiImplicitParam(name = "dir", dataType = "integer", required = false, value = "Sorting direction e.g desc or asc")
    })
    @RequestMapping(method = RequestMethod.GET, value = "/{serialNo}")
    public ResponseEntity<ResponseWrapper<Page<TmsDeviceHeartbeat>>> fetchheartbeatsByDeviceSerialNo(@PathVariable("serialNo") String serialNo, Pageable pg) {
        ResponseWrapper response = new ResponseWrapper();
        response.setData(deviceHeartbeatService.findBySerialNo(serialNo, pg));
        return new ResponseEntity(response, HttpStatus.OK);
    }
    
    @ApiOperation(value = "Fetch Device Heartbeats by device id", notes = "Used to fetch Device Heartbeats")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "size", dataType = "integer", required = false, value = "Pagination size e.g 20")
        ,
        @ApiImplicitParam(name = "page", dataType = "integer", required = false, value = "Page number e.g 0")
        ,
        @ApiImplicitParam(name = "sort", dataType = "string", required = false, value = "Field name e.g status")
        ,
        @ApiImplicitParam(name = "dir", dataType = "integer", required = false, value = "Sorting direction e.g desc or asc")
    })
    @RequestMapping(method = RequestMethod.GET, value = "/device/{id}")
    public ResponseEntity<ResponseWrapper<Page<TmsDeviceHeartbeat>>> fetchheartbeatsByDeviceId(@PathVariable("id") BigDecimal id, Pageable pg) {
        ResponseWrapper response = new ResponseWrapper();
        response.setData(deviceHeartbeatService.findBySerialNo(deviceService.getDevice(id).get().getSerialNo(), pg));
        return new ResponseEntity(response, HttpStatus.OK);
    }
    
    
    @ApiOperation(value = "Fetch Device Heartbeats by ID", notes = "Used to fetch Device Heartbeats")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "size", dataType = "integer", required = false, value = "Pagination size e.g 20")
        ,
        @ApiImplicitParam(name = "page", dataType = "integer", required = false, value = "Page number e.g 0")
        ,
        @ApiImplicitParam(name = "sort", dataType = "string", required = false, value = "Field name e.g status")
        ,
        @ApiImplicitParam(name = "dir", dataType = "integer", required = false, value = "Sorting direction e.g desc or asc")
    })
    @RequestMapping(method = RequestMethod.GET, value = "/id/{id}")
    public ResponseEntity<ResponseWrapper<Page<TmsDeviceHeartbeat>>> fetchheartbeatsById(@PathVariable("id") BigDecimal id) {
        ResponseWrapper response = new ResponseWrapper();
        response.setData(deviceHeartbeatService.findById(id));
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @ApiOperation(value = "Export Heartbeats to PDF")
    @RequestMapping(method = RequestMethod.GET, value = "/export.pdf")
    public ModelAndView exportUsersPdf(Pageable pg, @Valid HeartBeatsFilter filter) {
        pg = new PageRequest(0, Integer.MAX_VALUE, pg.getSort());
        String fileName = "Heartbeats";
        String title = "Heartbeats";
        List<TmsDeviceHeartbeat> result = deviceHeartbeatService.findAll(filter.getApplicationVersion(), filter.getChargingStatus(), filter.getOsVersion(), filter.getSerialNo(), filter.getFrom(), filter.getTo(), filter.getNeedle(),pg).getContent();
        PdfFlexView view = new PdfFlexView(TmsDeviceHeartbeat.class, result,
                fileName, title);
        return new ModelAndView(view);
    }

    @ApiOperation(value = "Export Heartbeats to Excel")
    @RequestMapping(method = RequestMethod.GET, value = "/export.xls")
    public ModelAndView exportUsersXls(Pageable pg, @Valid HeartBeatsFilter filter) {
        pg = new PageRequest(0, Integer.MAX_VALUE, pg.getSort());
        String fileName = "Heartbeats";
        List<TmsDeviceHeartbeat> result = deviceHeartbeatService.findAll(filter.getApplicationVersion(), filter.getChargingStatus(), filter.getOsVersion(), filter.getSerialNo(),filter.getFrom(),filter.getTo(), filter.getNeedle(),pg).getContent();
        ExcelFlexView view = new ExcelFlexView(TmsDeviceHeartbeat.class, result, fileName);
        return new ModelAndView(view);
    }

    @ApiOperation(value = "Export Heartbeats to CSV")
    @RequestMapping(method = RequestMethod.GET, value = "/export.csv")
    public ModelAndView exportUsersCsv(Pageable pg, @Valid HeartBeatsFilter filter) {
        pg = new PageRequest(0, Integer.MAX_VALUE, pg.getSort());
        String fileName = "Heartbeats";
        List<TmsDeviceHeartbeat> result = deviceHeartbeatService.findAll(filter.getApplicationVersion(), filter.getChargingStatus(), filter.getOsVersion(), filter.getSerialNo(),filter.getFrom(),filter.getTo(), filter.getNeedle(), pg).getContent();
        CsvFlexView view = new CsvFlexView(TmsDeviceHeartbeat.class, result, fileName);
        return new ModelAndView(view);
    }

}
