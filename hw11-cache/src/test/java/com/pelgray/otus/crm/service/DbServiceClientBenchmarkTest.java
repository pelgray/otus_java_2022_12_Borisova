package com.pelgray.otus.crm.service;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.pelgray.otus.cache.MyCache;
import com.pelgray.otus.core.repository.DataTemplateHibernate;
import com.pelgray.otus.core.sessionmanager.TransactionManagerHibernate;
import com.pelgray.otus.crm.model.Address;
import com.pelgray.otus.crm.model.Client;
import com.pelgray.otus.crm.model.Phone;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class DbServiceClientBenchmarkTest {
    private final static int NUM_ENTITIES = 1000;

    private final static int NUM_ENTITIES_FOR_READ = 200;

    private final static int READ_TIMES = 3000;

    private SessionFactory sessionFactory;

    private DataTemplateHibernate<Client> clientTemplate;

    private TransactionManagerHibernate transactionManager;

    private Level previousLoggerLevel;

    @BeforeEach
    void setUp() {
        final Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        previousLoggerLevel = logger.getLevel();
        logger.setLevel(Level.WARN);
    }

    @AfterEach
    void tearDown() {
        final Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        logger.setLevel(previousLoggerLevel);
    }

    long testCached(int id) throws InterruptedException {
        initDb("testdb-cached-" + id);

        var clientCache = new MyCache<Long, Client>(500);
        var dbServiceClient = new CachedDbServiceClient(transactionManager, clientTemplate, clientCache);

        var start = System.currentTimeMillis();
        readEntities(dbServiceClient);
        endTest();

        return System.currentTimeMillis() - start;
    }

    private void initDb(String dbName) throws InterruptedException {
        var sf = getSessionFactory(dbName, "create");
        var tm = new TransactionManagerHibernate(sf);
        var ct = new DataTemplateHibernate<>(Client.class);
        var defaultDbServiceClient = new DefaultDbServiceClient(tm, ct);
        createEntities(defaultDbServiceClient, NUM_ENTITIES);
        sf.close();
        callGC();

        sessionFactory = getSessionFactory(dbName, "validate");
        transactionManager = new TransactionManagerHibernate(sessionFactory);
        clientTemplate = new DataTemplateHibernate<>(Client.class);
    }

    long testDefault(int id) throws InterruptedException {
        initDb("testdb-default-" + id);

        var dbServiceClient = new DefaultDbServiceClient(transactionManager, clientTemplate);

        var start = System.currentTimeMillis();
        readEntities(dbServiceClient);
        endTest();

        return System.currentTimeMillis() - start;
    }

    private void callGC() throws InterruptedException {
        System.gc();
        Thread.sleep(100);
    }

    private void endTest() throws InterruptedException {
        sessionFactory.close();
        transactionManager = null;
        sessionFactory = null;
        clientTemplate = null;

        callGC();
    }

    private void readEntities(DBServiceClient dbServiceClient) {
        var numEntitiesForRead = Math.min(DbServiceClientBenchmarkTest.NUM_ENTITIES, NUM_ENTITIES_FOR_READ);
        for (int j = 0; j < READ_TIMES; j++) {
            for (int i = 1; i <= numEntitiesForRead; i++) {
                var loadedSavedClient = dbServiceClient.getClient(i);
                assertThat(loadedSavedClient).isPresent();
                assertThat(loadedSavedClient.get()).extracting(Client::getName).isEqualTo("Name" + i);
            }
        }
    }

    private void createEntities(DBServiceClient dbServiceClient, int numEntities) {
        for (int i = 1; i <= numEntities; i++) {
            var client = new Client(null, "Name" + i,
                                    new Address(null, UUID.randomUUID().toString()),
                                    List.of(new Phone(null, UUID.randomUUID().toString()),
                                            new Phone(null, UUID.randomUUID().toString())));

            dbServiceClient.saveClient(client);
        }
    }

    private SessionFactory getSessionFactory(String dbName, String hbm2ddlValue) {
        var cfg = new Configuration();

        cfg.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        cfg.setProperty("hibernate.connection.driver_class", "org.h2.Driver");

        cfg.setProperty("hibernate.connection.url", "jdbc:h2:mem:" + dbName + ";DB_CLOSE_DELAY=-1");

        cfg.setProperty("hibernate.connection.username", "sa");
        cfg.setProperty("hibernate.connection.password", "");

        cfg.setProperty("hibernate.hbm2ddl.auto", hbm2ddlValue);
        cfg.setProperty("hibernate.enable_lazy_load_no_trans", "false");

        var serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(cfg.getProperties()).build();

        MetadataSources metadataSources = new MetadataSources(serviceRegistry);
        metadataSources.addAnnotatedClass(Phone.class);
        metadataSources.addAnnotatedClass(Address.class);
        metadataSources.addAnnotatedClass(Client.class);
        var metadata = metadataSources.getMetadataBuilder().build();
        return metadata.getSessionFactoryBuilder().build();
    }

}
