package ke.tracom.ufs.utils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import ke.tracom.ufs.utils.annotations.ExportField;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@JsonIgnoreProperties(value = {"", " "})
@Getter
@Setter
@NoArgsConstructor
public class RegionDetails {
    @JsonProperty(value = "Region Name")
    @ExportField(name = "Region Name")
    @NotNull
    @Size(min = 1, max = 200)
    private String regionName;
}
