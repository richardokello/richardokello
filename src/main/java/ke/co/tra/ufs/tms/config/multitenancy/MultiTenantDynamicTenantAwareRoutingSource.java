package ke.co.tra.ufs.tms.config.multitenancy;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariDataSource;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class MultiTenantDynamicTenantAwareRoutingSource {

    private final String filename;
    private final ObjectMapper objectMapper;
    private final Map<Object, Object> tenants;

    public MultiTenantDynamicTenantAwareRoutingSource(String filename) {
        this(filename, new ObjectMapper());
    }

    public MultiTenantDynamicTenantAwareRoutingSource(String filename, ObjectMapper objectMapper) {
        this.filename = filename;
        this.objectMapper = objectMapper;
        this.tenants = getDataSources();
    }

    private Map<Object, Object> getDataSources() {
        // Deserialize the JSON:
        MultiTenantDatabaseConfiguration[] configurations = getDatabaseConfigurations();
        // Now create a Lookup Table:
        return Arrays
                .stream(configurations)
                .collect(Collectors.toMap(x -> x.getTenant(), x -> buildDataSource(x)));
    }

    private MultiTenantDatabaseConfiguration[] getDatabaseConfigurations() {
        try {
            return objectMapper.readValue(filename, MultiTenantDatabaseConfiguration[].class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private HikariDataSource buildDataSource(MultiTenantDatabaseConfiguration configuration) {
        HikariDataSource dataSource = new HikariDataSource();

        dataSource.setInitializationFailTimeout(3);
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
