package ke.co.tra.ufs.tms.entities.wrappers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WhiteListAssignWrapper {
    private List<String> serials;
}
