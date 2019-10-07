package ke.tra.ufs.webportal.entities.wrapper;


import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

public class UfsCustomerComplaintsUpload {

    @Transient
    @NotNull
    private MultipartFile file;

    public UfsCustomerComplaintsUpload() {
    }

    public UfsCustomerComplaintsUpload(MultipartFile file) {
        this.file = file;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
