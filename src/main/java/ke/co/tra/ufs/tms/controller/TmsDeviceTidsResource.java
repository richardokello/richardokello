package ke.co.tra.ufs.tms.controller;

import io.swagger.annotations.Api;
import ke.co.tra.ufs.tms.service.templates.DeviceTidsMidServiceTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tms_device_tids")
@Api(value = "Tms Devices Tid")
public class TmsDeviceTidsResource {

    @Autowired
    private DeviceTidsMidServiceTemplate tidsServiceTemplate;

    @RequestMapping(value = "/tids/{deviceId}", method = RequestMethod.GET)
    public ResponseEntity<List<String>> fetchTidsWithDeviceId(@PathVariable("deviceId") Long id) {
        return new ResponseEntity(tidsServiceTemplate.fetchData(id), HttpStatus.OK);
    }


}
