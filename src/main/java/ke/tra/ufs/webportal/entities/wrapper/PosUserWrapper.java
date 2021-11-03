package ke.tra.ufs.webportal.entities.wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PosUserWrapper {
    private String firstName;
    private String otherName;

    public PosUserWrapper(String firstName) {
        this.firstName = firstName;
    }
}
