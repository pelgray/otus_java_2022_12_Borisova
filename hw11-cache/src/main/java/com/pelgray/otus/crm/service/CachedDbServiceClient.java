package com.pelgray.otus.crm.service;

import com.pelgray.otus.cache.HwCache;
import com.pelgray.otus.core.repository.DataTemplate;
import com.pelgray.otus.core.sessionmanager.TransactionManager;
import com.pelgray.otus.crm.model.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class CachedDbServiceClient implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(CachedDbServiceClient.class);

    private final DataTemplate<Client> clientDataTemplate;

    private final TransactionManager transactionManager;

    private final HwCache<Long, Client> cache;

    public CachedDbServiceClient(TransactionManager transactionManager, DataTemplate<Client> clientDataTemplate,
                                 HwCache<Long, Client> cache) {
        this.transactionManager = transactionManager;
        this.clientDataTemplate = clientDataTemplate;
        this.cache = cache;
    }

    @Override
    public Client saveClient(Client client) {
        var resultedClient = transactionManager.doInTransaction(session -> {
            var clientCloned = client.clone();
            if (client.getId() == null) {
                clientDataTemplate.insert(session, clientCloned);
                log.info("created client: {}", clientCloned);
                return clientCloned;
            }
            clientDataTemplate.update(session, clientCloned);
            log.info("updated client: {}", clientCloned);
            return clientCloned;
        });
        addCache(resultedClient);
        return resultedClient;
    }

    private void addCache(Client client) {
        cache.put(client.getId(), client);
    }

    @Override
    public Optional<Client> getClient(long id) {
        var client = Optional.ofNullable(cache.get(id));
        if (client.isEmpty()) {
            client = transactionManager.doInReadOnlyTransaction(session -> {
                var clientOptional = clientDataTemplate.findById(session, id);
                log.info("client: {}", clientOptional);
                return clientOptional;
            });
        }
        return client;
    }

    @Override
    public List<Client> findAll() {
        return transactionManager.doInReadOnlyTransaction(session -> {
            var clientList = clientDataTemplate.findAll(session);
            log.info("clientList:{}", clientList);
            return clientList;
        });
    }
}
