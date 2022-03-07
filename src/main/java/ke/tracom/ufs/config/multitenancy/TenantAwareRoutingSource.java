package ke.tracom.ufs.config.multitenancy;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class TenantAwareRoutingSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        String key = ThreadLocalStorage.getTenantName();
        System.err.println("Current lookup key >>>>> " + key);
        return key;
    }
}
