//package ke.tra.com.tsync.config.db2;
//
//
//import javax.persistence.EntityManagerFactory;
//import javax.sql.DataSource;
//
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.orm.jpa.JpaTransactionManager;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//import com.zaxxer.hikari.HikariDataSource;
//
//
//@Configuration
//@EnableTransactionManagement
//@EnableJpaRepositories(
//        entityManagerFactoryRef = "db2EntityManagerFactory",
//        transactionManagerRef = "db2TransactionManager",
//        basePackages = {
//                "ke.tra.com.tsync.h2pkgs.repo", "ke.tra.com.tsync.config.db2"})
//public class Db2Config {
//
//    @Bean(name = "db2DataSourceProperties")
//    @ConfigurationProperties("h2db.datasource")
//    public DataSourceProperties data2SourceProperties() {
//        return new DataSourceProperties();
//    }
//
//    @Bean(name = "db2DataSource")
//    @ConfigurationProperties("h2db.datasource.configuration")
//    public DataSource dataSource2(@Qualifier("db2DataSourceProperties") DataSourceProperties data2SourceProperties) {
//        return data2SourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class)
//                .build();
//    }
//
//
//    @Bean(name = "db2EntityManagerFactory")
//    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
//            EntityManagerFactoryBuilder builder, @Qualifier("db2DataSource") DataSource dataSource2) {
//        return builder
//                .dataSource(dataSource2)
//                .packages("ke.tra.com.tsync.h2pkgs.models")
//                .persistenceUnit("h2db")
//                .build();
//    }
//
//
//    @Bean(name = "db2TransactionManager")
//    public PlatformTransactionManager transactionManager(
//            @Qualifier("db2EntityManagerFactory") EntityManagerFactory db2EntityManagerFactory) {
//        return new JpaTransactionManager(db2EntityManagerFactory);
//    }
//
//    @Bean(name = "h2JdbcTemplate")
//    public JdbcTemplate h2JdbcTemplate(@Qualifier("db2DataSource")
//                                                     DataSource dsPostgres) {
//        return new JdbcTemplate(dsPostgres);
//    }
//
//}
