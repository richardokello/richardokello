package ke.tra.ufs.webportal.wrappers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ke.tra.ufs.webportal.utils.annotations.ExportField;

import java.math.BigDecimal;

@JsonIgnoreProperties(value = {"", " "})
public class UfsGlsDetails {

    @ExportField(name = "glName")
    private String glName;
    @ExportField(name = "glCode")
    private String glCode;
    @ExportField(name = "glAccountNumber")
    private String glAccountNumber;
    @ExportField(name = "glLocation")
    private String glLocation;
    @ExportField(name = "bankIds")
    private BigDecimal bankIds;
    @ExportField(name = "bankBranchIds")
    private BigDecimal bankBranchIds;
    @ExportField(name = "tenantIds")
    private BigDecimal tenantIds;

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

    public BigDecimal getTenantIds() {
        return tenantIds;
    }

    public void setTenantIds(BigDecimal tenantIds) {
        this.tenantIds = tenantIds;
    }
}
