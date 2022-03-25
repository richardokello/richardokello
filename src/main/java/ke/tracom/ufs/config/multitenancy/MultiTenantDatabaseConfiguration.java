package ke.tracom.ufs.config.multitenancy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class MultiTenantDatabaseConfiguration {

    private final String tenant;
    private final String url;
    private final String user;
    private final String dataSourceClassName;
    private final String password;
    private final boolean defaultSource;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public MultiTenantDatabaseConfiguration(@JsonProperty("tenant") String tenant,
                                            @JsonProperty("url") String url,
                                            @JsonProperty("user") String user,
                                            @JsonProperty("dataSourceClassName") String dataSourceClassName,
                                            @JsonProperty("password") String password,
                                            @JsonProperty("defaultSource") boolean defaultSource
                                            ) {
        this.tenant = tenant;
        this.url = url;
        this.user = user;
        this.dataSourceClassName = dataSourceClassName;
        this.password = password;
        this.defaultSource = defaultSource;
    }

    @JsonProperty("tenant")
    public String getTenant() {
        return tenant;
    }

    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    @JsonProperty("user")
    public String getUser() {
        return user;
    }

    @JsonProperty("dataSourceClassName")
    public String getDataSourceClassName() {
        return dataSourceClassName;
    }

    @JsonProperty("password")
    public String getPassword() {
        return password;
    }

    @JsonProperty("defaultSource")
    public boolean getDefaultSource() {
        return defaultSource;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MultiTenantDatabaseConfiguration that = (MultiTenantDatabaseConfiguration) o;
        return Objects.equals(tenant, that.tenant) &&
                Objects.equals(url, that.url) &&
                Objects.equals(user, that.user) &&
                Objects.equals(dataSourceClassName, that.dataSourceClassName) &&
                Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tenant, url, user, dataSourceClassName, password);
    }


    @Override
    public String toString() {
        return "MultiTenantDatabaseConfiguration{" +
                "tenant='" + tenant + '\'' +
                ", url='" + url + '\'' +
                ", user='" + user + '\'' +
                ", dataSourceClassName='" + dataSourceClassName + '\'' +
                ", password='" + password + '\'' +
                ", defaultSource=" + defaultSource +
                '}';
    }
}
