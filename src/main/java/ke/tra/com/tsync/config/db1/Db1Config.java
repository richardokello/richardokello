package ke.tra.com.tsync.config.db1;


import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;


@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "db1EntityManagerFactory",
        basePackages = {
        "ke.tra.com.tsync.repository",
        "ke.tra.com.tsync.config.db1"},
        transactionManagerRef = "db1TransactionManager"
        )
public class Db1Config {
    @Primary
    @Bean(name = "db1DataSourceProperties")
    @ConfigurationProperties("maindb.datasource")
    public DataSourceProperties db1DataSourceProperties() {
        return new DataSourceProperties();
    }

    @Primary
    @Bean(name = "db1DataSource")
    @ConfigurationProperties("maindb.datasource.configuration")
    public DataSource dataSource(@Qualifier("db1DataSourceProperties") DataSourceProperties db1DataSourceProperties) {
        return db1DataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class)
                .build();
    }

    @Primary
    @Bean(name = "db1EntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder ebuilder, @Qualifier("db1DataSource") DataSource db1DataSource) {
        return ebuilder
                .dataSource(db1DataSource)
                .packages("ke.tra.com.tsync.entities")
                .persistenceUnit("maindb")
                .build();
    }

    @Primary
    @Bean(name = "db1TransactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("db1EntityManagerFactory") EntityManagerFactory db1EntityManagerFactory) {
        return new JpaTransactionManager(db1EntityManagerFactory);
    }

    @Primary
    @Bean(name = "oracleJdbcTemplate")
    public JdbcTemplate postgresJdbcTemplate(@Qualifier("db1DataSource")
                                                     DataSource dsPostgres) {
        return new JdbcTemplate(dsPostgres);
    }

}
