/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.entities.wrappers;

import java.math.BigDecimal;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author ojuma
 */
public class TMSAppWrapper {
    @NotNull
    private BigDecimal appId;
    @NotNull
    @Size(min = 1, max = 50)
    private String appName;
    @NotNull
    @Size(min = 1, max = 50)
    private String appVersion;
    @Size(max = 100)
    @NotNull
    private String description;
    @Size(max = 255)
    private String notesFilepath;
    @NotNull
    private BigDecimal productId;
    @NotNull
    private BigDecimal modelId;
    @NotNull
    private MultipartFile application;

    public TMSAppWrapper() {
    }

    public BigDecimal getAppId() {
        return appId;
    }

    public void setAppId(BigDecimal appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNotesFilepath() {
        return notesFilepath;
    }

    public void setNotesFilepath(String notesFilepath) {
        this.notesFilepath = notesFilepath;
    }

    public BigDecimal getProductId() {
        return productId;
    }

    public void setProductId(BigDecimal productId) {
        this.productId = productId;
    }

    public BigDecimal getModelId() {
        return modelId;
    }

    public void setModelId(BigDecimal modelId) {
        this.modelId = modelId;
    }

    public MultipartFile getApplication() {
        return application;
    }

    public void setApplication(MultipartFile application) {
        this.application = application;
    }
    
    
}
