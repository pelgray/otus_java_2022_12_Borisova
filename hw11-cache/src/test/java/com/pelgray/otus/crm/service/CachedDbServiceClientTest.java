package com.pelgray.otus.crm.service;

import com.pelgray.otus.base.AbstractHibernateTest;
import com.pelgray.otus.crm.model.Address;
import com.pelgray.otus.crm.model.Client;
import com.pelgray.otus.crm.model.Phone;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@DisplayName("Версия DbServiceClientTest с кэшированием должна ")
class CachedDbServiceClientTest extends AbstractHibernateTest {

    @Test
    @DisplayName(" корректно сохранять, изменять и загружать клиента")
    void shouldCorrectSaveClient() {
        //given
        var client = new Client(null, "Vasya", new Address(null, "AnyStreet"),
                                List.of(new Phone(null, "13-555-22"), new Phone(null, "14-666-333")));

        //when
        var savedClient = dbServiceClient.saveClient(client);
        System.out.println(savedClient);

        //then
        var loadedSavedClient = dbServiceClient.getClient(savedClient.getId());
        assertThat(loadedSavedClient).isPresent();
        assertThat(loadedSavedClient.get()).usingRecursiveComparison().isEqualTo(savedClient);

        //when
        var savedClientUpdated = loadedSavedClient.get().clone();
        savedClientUpdated.setName("updatedName");
        dbServiceClient.saveClient(savedClientUpdated);

        //then
        var loadedClient = dbServiceClient.getClient(savedClientUpdated.getId());
        assertThat(loadedClient).isPresent();
        assertThat(loadedClient.get()).usingRecursiveComparison().isEqualTo(savedClientUpdated);
        System.out.println(loadedClient);

        //when
        var clientList = dbServiceClient.findAll();

        //then
        assertThat(clientList.size()).isEqualTo(1);
        assertThat(clientList.get(0)).usingRecursiveComparison().isEqualTo(loadedClient.get());

        verify(transactionManager, times(2)).doInTransaction(any()); // save() x2
        verify(transactionManager).doInReadOnlyTransaction(any()); // only first getClient()
        verifyNoMoreInteractions(transactionManager);
    }
}
