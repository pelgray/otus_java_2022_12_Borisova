package com.pelgray.otus.cache;

import lombok.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class MyCache<K, V> implements HwCache<K, V> {
    private final Map<CacheKey<K>, V> cacheMap;

    private List<HwListener<K, V>> listeners = null;

    public MyCache(int initialCapacity) {
        this.cacheMap = new WeakHashMap<>(initialCapacity);
    }

    @Override
    public void put(K key, V value) {
        try {
            cacheMap.put(CacheKey.of(key), value);
        } finally {
            notify(key, value, ActionType.PUT);
        }
    }

    @Override
    public void remove(K key) {
        V removedValue = null;
        try {
            removedValue = cacheMap.remove(CacheKey.of(key));
        } finally {
            notify(key, removedValue, ActionType.REMOVE);
        }
    }

    @Override
    public V get(K key) {
        V value = null;
        try {
            value = cacheMap.get(CacheKey.of(key));
        } finally {
            notify(key, value, ActionType.GET);
        }
        return value;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        if (listeners == null) {
            listeners = new ArrayList<>();
        }
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        if (listeners == null) {
            return;
        }
        listeners.remove(listener);
    }

    private void notify(K key, V value, ActionType action) {
        if (listeners == null) {
            return;
        }
        listeners.forEach(listener -> {
            try {
                listener.notify(key, value, action.name());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    @Value(staticConstructor = "of")
    static class CacheKey<T> {
        T id;
    }

    enum ActionType {
        PUT,
        GET,
        REMOVE
    }
}
