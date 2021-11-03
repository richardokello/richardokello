/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.entities.wrappers;

import java.math.BigDecimal;
import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author Owori Juma
 */
public class AddTaskWrapper {
    @NotNull
    BigDecimal deviceId;
    BigDecimal appId;
    private MultipartFile[] file;
    @NotNull
    BigDecimal modelId;
    BigDecimal productId;
    @NotNull
    String downloadType;
    BigDecimal masterProfileId;

    public BigDecimal getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(BigDecimal deviceId) {
        this.deviceId = deviceId;
    }

    public BigDecimal getAppId() {
        return appId;
    }

    public void setAppId(BigDecimal appId) {
        this.appId = appId;
    }

    public MultipartFile[] getFile() {
        return file;
    }

    public void setFile(MultipartFile[] file) {
        this.file = file;
    }

    public BigDecimal getModelId() {
        return modelId;
    }

    public void setModelId(BigDecimal modelId) {
        this.modelId = modelId;
    }

    public BigDecimal getProductId() {
        return productId;
    }

    public void setProductId(BigDecimal productId) {
        this.productId = productId;
    }

    public String getDownloadType() {
        return downloadType;
    }

    public void setDownloadType(String downloadType) {
        this.downloadType = downloadType;
    }

    public BigDecimal getMasterProfileId() {
        return masterProfileId;
    }

    public void setMasterProfileId(BigDecimal masterProfileId) {
        this.masterProfileId = masterProfileId;
    }
}
