package org.example.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;

/**
 * Конфигурация компонентов для работы с БД
 */
@Configuration
@EnableTransactionManagement
public class DatabaseConfiguration {
    @Value("${db.driverClassName}")
    private String driver = "org.postgresql.Driver";

    @Value("${db.maxPoolSize}")
    private int poolLimit = 10;

    private final String dbUrl;
    private final String userName;
    private final String userPassword;

    @Autowired
    public DatabaseConfiguration(@Value("${db.username}") String userName,
                                 @Value("${db.password}") String userPassword,
                                 @Value("${db.url}") String dbUrl) {
        this.userName = userName;
        this.userPassword = userPassword;
        this.dbUrl = dbUrl;
    }

    @Bean(destroyMethod = "close")
    public HikariDataSource hikariDataSource() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(driver);

        config.setJdbcUrl(dbUrl);
        config.setUsername(userName);
        config.setPassword(userPassword);
        config.setMaximumPoolSize(poolLimit);

        return new HikariDataSource(config);
    }

    @Bean
    public TransactionAwareDataSourceProxy transactionAwareDataSource() {
        return new TransactionAwareDataSourceProxy(hikariDataSource());
    }

    @Bean
    public DataSourceTransactionManager dataSourceTransactionManager() {
        return new DataSourceTransactionManager(transactionAwareDataSource());
    }

    @Bean
    public TransactionTemplate transactionTemplate() {
        return new TransactionTemplate(dataSourceTransactionManager());
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(hikariDataSource());
    }

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate() {
        return new NamedParameterJdbcTemplate(jdbcTemplate());
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.liquibase")
    public LiquibaseProperties mainLiquibaseProperties() {
        LiquibaseProperties liquibaseProperties=new LiquibaseProperties();
        liquibaseProperties.setChangeLog("classpath:/db/changelog.xml");
        return liquibaseProperties;
    }

    @Bean
    public SpringLiquibase springLiquibase() {
        LiquibaseProperties liquibaseProperties = mainLiquibaseProperties();
        return createSpringLiquibase(hikariDataSource(), liquibaseProperties);
    }

    private SpringLiquibase createSpringLiquibase(DataSource source, LiquibaseProperties liquibaseProperties) {
        return new SpringLiquibase() {
            {
                setDataSource(source);
                setDropFirst(liquibaseProperties.isDropFirst());
                setContexts(liquibaseProperties.getContexts());
                setChangeLog(liquibaseProperties.getChangeLog());
                setDefaultSchema(liquibaseProperties.getDefaultSchema());
                setChangeLogParameters(liquibaseProperties.getParameters());
                setShouldRun(liquibaseProperties.isEnabled());
                setRollbackFile(liquibaseProperties.getRollbackFile());
                setLabels(liquibaseProperties.getLabels());
            }
        };
    }
}
