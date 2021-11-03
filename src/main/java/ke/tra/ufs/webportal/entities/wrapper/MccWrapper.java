package ke.tra.ufs.webportal.entities.wrapper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import ke.tra.ufs.webportal.utils.annotations.ExportField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@JsonIgnoreProperties(value = {"", " "})
@AllArgsConstructor
@NoArgsConstructor
public class MccWrapper {
    @JsonProperty(value = "MCC")
    @ExportField(name = "MCC")
    @NotNull
    @Size(min = 1, max = 200)
    private String mcc;

    @JsonProperty(value = "MCC Title")
    @ExportField(name = "MCC Title")
    @NotNull
    @Size(min = 1, max = 200)
    private String mccTitle;
}
