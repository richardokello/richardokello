package ke.co.tra.ufs.tms.entities.wrappers;

import lombok.Data;

@Data
public class InstitutionWrapper {
    private String orgId;
    private String cardAccount;
    private String cashAccount;
    private String orgPrefix;
    private String orgName;
}
