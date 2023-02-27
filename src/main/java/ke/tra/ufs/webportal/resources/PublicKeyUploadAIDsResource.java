package ke.tra.ufs.webportal.resources;

import io.swagger.annotations.ApiOperation;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.tra.ufs.webportal.entities.wrapper.TrainedAgentsDetails;
import ke.tra.ufs.webportal.service.template.PublicKeyUpload;
import ke.tra.ufs.webportal.utils.exports.CsvFlexView;
import ke.tra.ufs.webportal.wrappers.CaPublicKeyData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.List;

public class PublicKeyUploadAIDsResource {
    private PublicKeyUpload publicKeyUploadService;
    @RequestMapping(value = "/uploadCaKeys",method = RequestMethod.POST)
    @Transactional
    @ApiOperation(value = "Uploading CA Public Keys")
    ResponseEntity<ResponseWrapper>uploadPublicKey(MultipartFile file, HttpServletRequest request){
        ResponseWrapper response = new ResponseWrapper();
        if (!file.isEmpty()){
            if(!(file.getContentType().equalsIgnoreCase("CSV")||
            file.getContentType().equalsIgnoreCase("application/vnd.ms-excel")||
            file.getContentType().equalsIgnoreCase("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))){
                response.setCode(400);
                response.setMessage("Unsupported file type. Expects a CSV file");
                return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
            }try{
            publicKeyUploadService.saveKeys(file);
            response.setMessage("Uploaded the file successfully: " + file.getOriginalFilename());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
                response.setMessage("could not upload the file");
            }
        }
        else {
          response.setMessage("the file is empty");
          response.setCode(400);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
