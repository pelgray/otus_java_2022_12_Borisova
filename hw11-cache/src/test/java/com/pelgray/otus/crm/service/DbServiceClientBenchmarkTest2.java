package com.pelgray.otus.crm.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DbServiceClientBenchmarkTest2 extends DbServiceClientBenchmarkTest {
    @Test
    @DisplayName("Сравнение: сначала подсчет времени работы версии без кэширования")
    void testTime() throws InterruptedException {
        long timeDefault = testDefault(1);
        long timeCached = testCached(1);

        assertThat(timeDefault).as("Вариант без кэша работает быстрее").isGreaterThan(timeCached);
    }
}
