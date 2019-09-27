
package ke.tra.ufs.webportal.wrappers;

import java.util.List;
import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.annotations.Expose;
import lombok.Data;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Row {

    private String transactionType;
    private List<Values> values;
}
