package ke.tra.ufs.webportal.wrappers;

import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Transient;
import java.math.BigDecimal;

public class UfsGlsWrapper {
    private String glName;
    private String glCode;
    private String glAccountNumber;
    private String glLocation;
    private BigDecimal bankIds;
    private BigDecimal bankBranchIds;
    private BigDecimal tenantIds;
    @Transient
    private MultipartFile file;

    public UfsGlsWrapper() {
    }

    public UfsGlsWrapper(String glName, String glCode, String glAccountNumber, String glLocation, BigDecimal bankIds, BigDecimal bankBranchIds, BigDecimal tenantIds) {
        this.glName = glName;
        this.glCode = glCode;
        this.glAccountNumber = glAccountNumber;
        this.glLocation = glLocation;
        this.bankIds = bankIds;
        this.bankBranchIds = bankBranchIds;
        this.tenantIds = tenantIds;
    }

    public String getGlName() {
        return glName;
    }

    public void setGlName(String glName) {
        this.glName = glName;
    }

    public String getGlCode() {
        return glCode;
    }

    public void setGlCode(String glCode) {
        this.glCode = glCode;
    }

    public String getGlAccountNumber() {
        return glAccountNumber;
    }

    public void setGlAccountNumber(String glAccountNumber) {
        this.glAccountNumber = glAccountNumber;
    }

    public String getGlLocation() {
        return glLocation;
    }

    public void setGlLocation(String glLocation) {
        this.glLocation = glLocation;
    }

    public BigDecimal getBankIds() {
        return bankIds;
    }

    public void setBankIds(BigDecimal bankIds) {
        this.bankIds = bankIds;
    }

    public BigDecimal getBankBranchIds() {
        return bankBranchIds;
    }

    public void setBankBranchIds(BigDecimal bankBranchIds) {
        this.bankBranchIds = bankBranchIds;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public BigDecimal getTenantIds() {
        return tenantIds;
    }

    public void setTenantIds(BigDecimal tenantIds) {
        this.tenantIds = tenantIds;
    }
}
