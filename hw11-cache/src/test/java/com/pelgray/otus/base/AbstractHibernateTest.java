package com.pelgray.otus.base;

import com.pelgray.otus.cache.HwListener;
import com.pelgray.otus.cache.MyCache;
import com.pelgray.otus.core.repository.DataTemplateHibernate;
import com.pelgray.otus.core.repository.HibernateUtils;
import com.pelgray.otus.core.sessionmanager.TransactionManagerHibernate;
import com.pelgray.otus.crm.dbmigrations.MigrationsExecutorFlyway;
import com.pelgray.otus.crm.model.Address;
import com.pelgray.otus.crm.model.Client;
import com.pelgray.otus.crm.model.Phone;
import com.pelgray.otus.crm.service.CachedDbServiceClient;
import com.pelgray.otus.crm.service.DBServiceClient;
import com.pelgray.otus.demo.DbServiceDemo;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.stat.EntityStatistics;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.mockito.Mockito.spy;

public abstract class AbstractHibernateTest {
    private static final Logger logger = LoggerFactory.getLogger(AbstractHibernateTest.class);

    protected SessionFactory sessionFactory;

    protected TransactionManagerHibernate transactionManager;

    protected DataTemplateHibernate<Client> clientTemplate;

    protected DBServiceClient dbServiceClient;

    private static TestContainersConfig.CustomPostgreSQLContainer CONTAINER;

    private HwListener<Long, Client> clientListener;

    private MyCache<Long, Client> clientCache;

    @BeforeAll
    public static void init() {
        CONTAINER = TestContainersConfig.CustomPostgreSQLContainer.getInstance();
        CONTAINER.start();
    }

    @AfterAll
    public static void shutdown() {
        CONTAINER.stop();
    }

    @BeforeEach
    public void setUp() {
        String dbUrl = System.getProperty("app.datasource.demo-db.jdbcUrl");
        String dbUserName = System.getProperty("app.datasource.demo-db.username");
        String dbPassword = System.getProperty("app.datasource.demo-db.password");

        var migrationsExecutor = new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword);
        migrationsExecutor.executeMigrations();

        Configuration configuration = new Configuration().configure(DbServiceDemo.HIBERNATE_CFG_FILE);
        configuration.setProperty("hibernate.connection.url", dbUrl);
        configuration.setProperty("hibernate.connection.username", dbUserName);
        configuration.setProperty("hibernate.connection.password", dbPassword);

        sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class, Address.class, Phone.class);

        transactionManager = spy(new TransactionManagerHibernate(sessionFactory));
        clientTemplate = new DataTemplateHibernate<>(Client.class);

        clientListener = (key, value, action) -> {
            logger.info("key:{}, value:{}, action: {}", key, value, action);
        };
        clientCache = new MyCache<>(500);
        clientCache.addListener(clientListener);
        dbServiceClient = new CachedDbServiceClient(transactionManager, clientTemplate, clientCache);
    }

    @AfterEach
    void tearDown() {
        clientCache.removeListener(clientListener);
    }

    protected EntityStatistics getUsageStatistics() {
        Statistics stats = sessionFactory.getStatistics();
        return stats.getEntityStatistics(Client.class.getName());
    }
}
