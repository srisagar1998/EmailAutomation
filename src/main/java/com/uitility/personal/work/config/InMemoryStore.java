package com.uitility.personal.work.config;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class InMemoryStore {
    private final Map<String, String> store = new ConcurrentHashMap<>();

    public void save(String id, String html) {
        store.put(id, html);
    }

    public String get(String id) {
        return store.get(id);
    }

    public boolean exists(String id) {
        return store.containsKey(id);
    }
}

