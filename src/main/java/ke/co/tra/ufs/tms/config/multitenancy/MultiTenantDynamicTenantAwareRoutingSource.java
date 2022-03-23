package ke.co.tra.ufs.tms.config.multitenancy;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariDataSource;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class MultiTenantDynamicTenantAwareRoutingSource {

    private final Map<Object, Object> tenants;

    public MultiTenantDynamicTenantAwareRoutingSource(MultiTenantDatabaseConfiguration[] configs) {
        this.tenants = getDataSources(configs);
    }

    private Map<Object, Object> getDataSources(MultiTenantDatabaseConfiguration[] configs) {
        // Deserialize the JSON:
        // Now create a Lookup Table:
        return Arrays
                .stream(configs)
                .collect(Collectors.toMap(MultiTenantDatabaseConfiguration::getTenant, this::buildDataSource));
    }


    private HikariDataSource buildDataSource(MultiTenantDatabaseConfiguration configuration) {
        HikariDataSource dataSource = new HikariDataSource();

        dataSource.setInitializationFailTimeout(0);
        dataSource.setMaximumPoolSize(5);
        dataSource.setJdbcUrl(configuration.getUrl());
        dataSource.setUsername(configuration.getUser());
        dataSource.setPassword(configuration.getPassword());

        if(configuration.getDefaultSource()){
            ThreadLocalStorage.setTenantName(configuration.getTenant());
        }

        System.out.println("Building datasource here >>>>>>> ");

        return dataSource;
    }

    public Map<Object, Object> getTenants() {
        return tenants;
    }
}
