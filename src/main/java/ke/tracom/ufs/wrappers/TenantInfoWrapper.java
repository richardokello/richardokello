package ke.tracom.ufs.wrappers;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TenantInfoWrapper {
    private String name;
    private String tenantid;
    private String language;

    @Override
    public String toString() {
        return "TenantInfoWrapper{" +
                "name='" + name + '\'' +
                ", tenantid='" + tenantid + '\'' +
                ", language='" + language + '\'' +
                '}';
    }
}
