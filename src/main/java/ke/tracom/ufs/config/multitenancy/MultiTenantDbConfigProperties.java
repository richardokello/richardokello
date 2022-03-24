package ke.tracom.ufs.config.multitenancy;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Data
@Configuration
@ConfigurationProperties(prefix = "tenant-db")
@Order
public class MultiTenantDbConfigProperties {
    private String url;
    private String port;
    private String ssid;
    private String[] users;
    private String[] password;
    private String[] tenants;
    private String dataSourceClassName;
    private Integer defaultValue;
}
