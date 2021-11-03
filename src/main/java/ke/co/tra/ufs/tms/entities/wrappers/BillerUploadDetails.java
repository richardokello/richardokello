package ke.co.tra.ufs.tms.entities.wrappers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import ke.co.tra.ufs.tms.utils.annotations.ExportField;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@JsonIgnoreProperties(value = {"", " "})
public class BillerUploadDetails {
    @JsonProperty(value = "Biller Id")
    @ExportField(name = "Biller Id")
    @NotNull
    @Size(min = 1, max = 200)
    private String billerId;
    @JsonProperty(value = "Biller Name")
    @ExportField(name = "Biller Name")
    @NotNull
    @Size(min = 1, max = 200)
    private String billerName;
    @JsonProperty(value = "Biller Category")
    @ExportField(name = "Biller Category")
    @NotNull
    @Size(min = 1, max = 200)
    private String billerCategory;

    public BillerUploadDetails() {
    }

    public String getBillerId() {
        return billerId;
    }

    public void setBillerId(String billerId) {
        this.billerId = billerId;
    }

    public String getBillerName() {
        return billerName;
    }

    public void setBillerName(String billerName) {
        this.billerName = billerName;
    }

    public String getBillerCategory() {
        return billerCategory;
    }

    public void setBillerCategory(String billerCategory) {
        this.billerCategory = billerCategory;
    }
}
