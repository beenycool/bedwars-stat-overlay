package com.bedwarsstats.cache;

import java.util.HashMap;
import java.util.Map;

public class GameCache {
    private final Map<String, Object> cache = new HashMap<>();

    public void put(String key, Object value) {
        cache.put(key, value);
    }

    public Object get(String key) {
        return cache.get(key);
    }

    public void clear() {
        cache.clear();
    }
}
