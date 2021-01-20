package ke.co.tra.ufs.tms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.co.tra.ufs.tms.entities.wrappers.ActionWrapper;
import ke.co.tra.ufs.tms.entities.wrappers.BillerUploadDetails;
import ke.co.tra.ufs.tms.entities.wrappers.Billers;
import ke.co.tra.ufs.tms.entities.wrappers.filters.CommonFilter;
import ke.co.tra.ufs.tms.utils.RestTemplateClient;
import ke.co.tra.ufs.tms.utils.exceptions.ExpectationFailed;
import ke.co.tra.ufs.tms.utils.exports.CsvFlexView;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;

@RequestMapping("/billers")
@Controller
public class BillerResource {


    private final RestTemplateClient restTemplateClient;
    private final ObjectMapper mapper;
    @Value("${app.tid.url}")
    private String tidUrl;
    @Value("${app.biller.url}")
    private String billerUrl;

    public BillerResource(RestTemplateClient restTemplateClient, ObjectMapper mapper) {
        this.restTemplateClient = restTemplateClient;
        this.mapper = mapper;
    }


    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation(value = "Create Billers")
    public ResponseEntity<ResponseWrapper> create(@Valid Billers payload, Authentication a) throws IOException {
        payload.setCreatedBy(a.getName());

        if (payload.getFile() != null) {
            if (!(payload.getFile().getContentType().equalsIgnoreCase("text/csv")
                    || payload.getFile().getContentType().equalsIgnoreCase("application/vnd.ms-excel")
                    || payload.getFile().getContentType().equalsIgnoreCase("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))) {
                ResponseWrapper response = new ResponseWrapper();
                response.setCode(400);
                response.setMessage("Unsupported file type. Expects a CSV / Excel file");
                return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
            }
        }
        try {
            if (payload.getFile() != null) {

                MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

                body.add("createdBy", a.getName());
                ByteArrayResource contentsAsResource = new ByteArrayResource(payload.getFile().getBytes()) {
                    @Override
                    public String getFilename() {
                        return payload.getFile().getOriginalFilename();
                    }
                };
                body.add("file", contentsAsResource);
                ResponseWrapper responseWrapper = restTemplateClient.postMultipartEntity(ResponseWrapper.class, billerUrl + "/upload", body);
                return new ResponseEntity(responseWrapper, HttpStatus.CREATED);
            } else {
                payload.setId(null);
                ResponseWrapper responseWrapper = restTemplateClient.postForEntity(ResponseWrapper.class, billerUrl, payload);
                return new ResponseEntity(responseWrapper, HttpStatus.CREATED);
            }
        } catch (HttpClientErrorException ex) {
            ResponseWrapper response = mapper.readValue(ex.getResponseBodyAsString(), ResponseWrapper.class);
            return ResponseEntity.status(ex.getStatusCode()).body(response);
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/update")
    @ApiOperation(value = "Update Billers")
    public ResponseEntity<ResponseWrapper> update(@Valid @RequestBody Billers payload, Authentication a) throws IOException {
        try {
            ResponseWrapper responseWrapper = restTemplateClient.putForEntity(ResponseWrapper.class, billerUrl, payload);
            return ResponseEntity.ok(responseWrapper);
        } catch (HttpClientErrorException ex) {
            ResponseWrapper response = mapper.readValue(ex.getResponseBodyAsString(), ResponseWrapper.class);
            return ResponseEntity.status(ex.getStatusCode()).body(response);
        }
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", dataType = "int", required = false, value = "Pagination size e.g 20", paramType = "query")
            ,
            @ApiImplicitParam(name = "page", dataType = "int", required = false, value = "Page number e.g 0", paramType = "query")
            ,
            @ApiImplicitParam(name = "sort", dataType = "string", required = false, value = "status,desc", paramType = "query")
    })
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<ResponseWrapper> getBillers(Pageable pg, @Valid @ApiParam(value = "Entity filters and search parameters") CommonFilter filter) {
        ResponseWrapper responseWrapper = restTemplateClient.getForEntity(ResponseWrapper.class, billerUrl);
        return ResponseEntity.ok(responseWrapper);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/inactive")
    public ResponseEntity<ResponseWrapper> getBillersInactive(Pageable pg, @Valid @ApiParam(value = "Entity filters and search parameters") CommonFilter filter) {
        ResponseWrapper responseWrapper = restTemplateClient.getForEntity(ResponseWrapper.class, billerUrl + "?status=0");
        return ResponseEntity.ok(responseWrapper);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", dataType = "int", required = false, value = "Pagination size e.g 20", paramType = "query")
            ,
            @ApiImplicitParam(name = "page", dataType = "int", required = false, value = "Page number e.g 0", paramType = "query")
            ,
            @ApiImplicitParam(name = "sort", dataType = "string", required = false, value = "status,desc", paramType = "query")
    })
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<ResponseWrapper> getBillersByID(@PathVariable("id") String id) throws IOException {
        try {
            ResponseWrapper responseWrapper = restTemplateClient.getForEntity(ResponseWrapper.class, billerUrl + "/" + id);
            return ResponseEntity.ok(responseWrapper);
        } catch (HttpClientErrorException ex) {
            ResponseWrapper response = mapper.readValue(ex.getResponseBodyAsString(), ResponseWrapper.class);
            return ResponseEntity.status(ex.getStatusCode()).body(response);
        }
    }


    @RequestMapping(method = RequestMethod.PUT, value = "/approve-actions")
    @ApiOperation(value = "Approve Billers")
    public ResponseEntity<ResponseWrapper> approveActions(@Valid @RequestBody ActionWrapper<Long> actions, Authentication a) throws ExpectationFailed, IOException {
        actions.setNotes(a.getName());
        try {
            ResponseWrapper responseWrapper = restTemplateClient.putForEntity(ResponseWrapper.class, billerUrl + "/approve-actions", actions);
            return ResponseEntity.status(HttpStatus.OK).body(responseWrapper);
        } catch (HttpClientErrorException ex) {
            ResponseWrapper response = mapper.readValue(ex.getResponseBodyAsString(), ResponseWrapper.class);
            return ResponseEntity.status(ex.getStatusCode()).body(response);
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/decline-actions")
    @ApiOperation(value = "Decline Billers")
    public ResponseEntity<ResponseWrapper> declineActions(@Valid @RequestBody ActionWrapper<Long> actions, Authentication a) throws ExpectationFailed, IOException {
        actions.setNotes(a.getName());
        try {
            ResponseWrapper responseWrapper = restTemplateClient.putForEntity(ResponseWrapper.class, billerUrl + "/decline-actions", actions);
            return ResponseEntity.status(HttpStatus.OK).body(responseWrapper);
        } catch (HttpClientErrorException ex) {
            ResponseWrapper response = mapper.readValue(ex.getResponseBodyAsString(), ResponseWrapper.class);
            return ResponseEntity.status(ex.getStatusCode()).body(response);
        }
    }

    @ApiOperation(value = "Download Billers template")
    @RequestMapping(value = "/billers-template.csv", method = RequestMethod.GET)
    public ModelAndView exportVehicleTemplate(HttpServletRequest request) {
        CsvFlexView view;
        String fileName = "Billers Template";
        view = new CsvFlexView(BillerUploadDetails.class, new ArrayList(), fileName);
        return new ModelAndView(view);
    }
}
