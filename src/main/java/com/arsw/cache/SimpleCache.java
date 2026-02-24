package com.arsw.cache;

import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

@Component
public class SimpleCache {
    private final Map<String,String> cache = new ConcurrentHashMap<>();

    public String computeIfAbsent(String key, Supplier<String> supplier) {
        return cache.computeIfAbsent(key, k -> supplier.get());
    }
    
    public boolean containsKey(String key) {
        return cache.containsKey(key);
    }
    
    public void clear() {
        cache.clear();
    }
    
    public int size() {
        return cache.size();
    }
    
    public Map<String, String> getCacheStats() {
        return Map.of(
            "size", String.valueOf(cache.size()),
            "keys", cache.keySet().toString()
        );
    }
}
