package ke.co.tra.ufs.tms.entities.wrappers;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class InstitutionsResponse {

    private int status;
    private String statusDesc;
    private List<Object> data;
}
