package com.pelgray.otus.cache;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("unchecked")
@DisplayName("Реализация кеширования из домашней работы должна ")
class MyCacheTest {
    @Test
    @DisplayName(" очищать кэш после запуска GC при использовании в качестве ключа нативно кэшируемый объект (Integer)")
    void testCachedKeys() throws Exception {
        HwCache<Integer, String> cache = new MyCache<>(500);
        var cacheMap = (Map<Integer, String>) ReflectionUtils.tryToReadFieldValue(
                MyCache.class.getDeclaredField("cacheMap"), cache).get();

        var limit = 100;
        for (var idx = 0; idx < limit; idx++) {
            cache.put(idx, String.valueOf(idx));
        }
        assertThat(cacheMap).as("size before gc is wrong").size().isEqualTo(limit);

        System.gc();
        Thread.sleep(100);
        assertThat(cacheMap).as("size after gc is wrong").size().isEqualTo(0);
    }

    @Test
    @DisplayName(" очищать кэш после запуска GC")
    void testUsualKeys() throws Exception {
        HwCache<String, String> cache = new MyCache<>(500);
        var cacheMap = (Map<String, String>) ReflectionUtils.tryToReadFieldValue(
                MyCache.class.getDeclaredField("cacheMap"), cache).get();

        var limit = 100;
        for (var idx = 0; idx < limit; idx++) {
            cache.put(String.valueOf(idx), String.valueOf(idx));
        }
        assertThat(cacheMap).as("size before gc is wrong").size().isEqualTo(limit);

        System.gc();
        Thread.sleep(100);
        assertThat(cacheMap).as("size after gc is wrong").size().isEqualTo(0);
    }

}
