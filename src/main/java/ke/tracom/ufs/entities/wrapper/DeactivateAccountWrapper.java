package ke.tracom.ufs.entities.wrapper;

import lombok.Data;

import java.util.List;

@Data
public class DeactivateAccountWrapper {
    public List<Long> ids;
    public String notes;
    public String action;
}
