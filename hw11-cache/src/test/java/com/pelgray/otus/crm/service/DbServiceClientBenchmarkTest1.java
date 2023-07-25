package com.pelgray.otus.crm.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DbServiceClientBenchmarkTest1 extends DbServiceClientBenchmarkTest {
    @Test
    @DisplayName("Сравнение: сначала подсчет времени работы версии с кэшированием")
    void testTime() throws InterruptedException {
        long timeCached = testCached(2);
        long timeDefault = testDefault(2);

        assertThat(timeDefault).as("Вариант без кэша работает быстрее").isGreaterThan(timeCached);
    }
}
