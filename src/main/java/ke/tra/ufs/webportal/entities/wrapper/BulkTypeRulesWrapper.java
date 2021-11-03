package ke.tra.ufs.webportal.entities.wrapper;

import java.math.BigDecimal;
import java.util.List;

public class BulkTypeRulesWrapper {
    private BigDecimal tenantId;
    private List<TypeRulesWrapper> rulesWrapper;

    public BulkTypeRulesWrapper() {
    }

    public BulkTypeRulesWrapper(BigDecimal tenantId, List<TypeRulesWrapper> rulesWrapper) {
        this.tenantId = tenantId;
        this.rulesWrapper = rulesWrapper;
    }

    public BigDecimal getTenantId() {
        return tenantId;
    }

    public void setTenantId(BigDecimal tenantId) {
        this.tenantId = tenantId;
    }

    public List<TypeRulesWrapper> getRulesWrapper() {
        return rulesWrapper;
    }

    public void setRulesWrapper(List<TypeRulesWrapper> rulesWrapper) {
        this.rulesWrapper = rulesWrapper;
    }
}
