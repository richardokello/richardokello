package ke.tracom.ufs.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.scheduling.annotation.Scheduled;

import javax.sql.DataSource;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class MultiTenantDynamicTenantAwareRoutingSource  {

    private final String filename;
    private final ObjectMapper objectMapper;
    private final Map<String, HikariDataSource> tenants;

    public MultiTenantDynamicTenantAwareRoutingSource(String filename) {
        this(filename, new ObjectMapper());
    }

    public MultiTenantDynamicTenantAwareRoutingSource(String filename, ObjectMapper objectMapper) {
        this.filename = filename;
        this.objectMapper = objectMapper;
        this.tenants = getDataSources();
    }

    private Map<String, HikariDataSource> getDataSources() {
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
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    private HikariDataSource buildDataSource(MultiTenantDatabaseConfiguration configuration) {
        HikariDataSource dataSource = new HikariDataSource();

        dataSource.setInitializationFailTimeout(0);
        dataSource.setMaximumPoolSize(5);
        dataSource.setDataSourceClassName(configuration.getDataSourceClassName());
        dataSource.addDataSourceProperty("url", configuration.getUrl());
        dataSource.addDataSourceProperty("user", configuration.getUser());
        dataSource.addDataSourceProperty("password", configuration.getPassword());

        return dataSource;
    }

    public Map<String, HikariDataSource> getTenants() {
        return tenants;
    }
}
