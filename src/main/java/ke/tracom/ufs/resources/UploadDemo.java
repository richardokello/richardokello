package ke.tracom.ufs.resources;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/upload")
public class UploadDemo {

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> upload(@Valid MultipartFile file) {
        // Upload file to a path, you can have a storage service that hanldes that
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
