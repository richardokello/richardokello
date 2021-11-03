package ke.tra.ufs.webportal.service;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UfsMccService {
    void processFileUpload(MultipartFile file) throws IOException, InvalidFormatException;

}
