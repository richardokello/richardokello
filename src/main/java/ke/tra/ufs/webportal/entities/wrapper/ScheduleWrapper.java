/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.entities.wrapper;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author Owori Juma
 */
public class ScheduleWrapper {

    @NotNull
    BigDecimal modelId;
    @NotNull
    BigDecimal[] unitItemId;
    
    BigDecimal appId;
    private MultipartFile[] file;
    @NotNull
    BigDecimal productId;
    @NotNull
    String downloadType;
    @NotNull
    BigDecimal makeId;
    @NotNull
    Date scheduleTime;
    BigDecimal scheduleId;
    BigDecimal masterProfileId;



    public BigDecimal getModelId() {
        return modelId;
    }

    public void setModelId(BigDecimal modelId) {
        this.modelId = modelId;
    }

    public BigDecimal[] getUnitItemId() {
        return unitItemId;
    }

    public void setUnitItemId(BigDecimal[] unitItemId) {
        this.unitItemId = unitItemId;
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

    public BigDecimal getMakeId() {
        return makeId;
    }

    public void setMakeId(BigDecimal makeId) {
        this.makeId = makeId;
    }

    public Date getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(Date scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    public BigDecimal getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(BigDecimal scheduleId) {
        this.scheduleId = scheduleId;
    }

    public BigDecimal getMasterProfileId() {
        return masterProfileId;
    }

    public void setMasterProfileId(BigDecimal masterProfileId) {
        this.masterProfileId = masterProfileId;
    }

    @Override
    public String toString() {
        return "ScheduleWrapper{" + "modelId=" + modelId + ", unitItemId=" + unitItemId + ", appId=" + appId + ", file=" + file + ", productId=" + productId + ", downloadType=" + downloadType + ", makeId=" + makeId + ", scheduleTime=" + scheduleTime + ", scheduleId=" + scheduleId + '}';
    }

}
