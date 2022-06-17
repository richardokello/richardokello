package ke.co.tra.ufs.tms.config.multitenancy;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariDataSource;
import org.jasypt.encryption.StringEncryptor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


public class MultiTenantDynamicTenantAwareRoutingSource {
    private final Map<Object, Object> tenants;

    public MultiTenantDynamicTenantAwareRoutingSource(MultiTenantDatabaseConfiguration[] configs, StringEncryptor encryptor) {
        this.tenants = getDataSources(configs, encryptor);
    }

    private Map<Object, Object> getDataSources(MultiTenantDatabaseConfiguration[] configs, StringEncryptor encryptor) {
        // Deserialize the JSON:
        // Now create a Lookup Table:
        Map<Object, Object> connections = new HashMap<>();
        for (MultiTenantDatabaseConfiguration config : configs) {
            connections.put(config.getTenant(), this.buildDataSource(config, encryptor));
        }
//        return Arrays
//                .stream(configs)
//                .collect(Collectors.toMap(MultiTenantDatabaseConfiguration::getTenant, this::buildDataSource));

        return connections;
    }

    private HikariDataSource buildDataSource(MultiTenantDatabaseConfiguration configuration, StringEncryptor encryptor) {
        HikariDataSource dataSource = new HikariDataSource();

        dataSource.setInitializationFailTimeout(0);
        dataSource.setMaximumPoolSize(5);
        dataSource.setJdbcUrl(configuration.getUrl());
        dataSource.setUsername(encryptor.decrypt(configuration.getUser()));
        dataSource.setPassword(encryptor.decrypt(configuration.getPassword()));

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
