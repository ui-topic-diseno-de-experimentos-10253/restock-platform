package com.restock.platform.bdd;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TestContext {

    private final Map<String, Object> scenarioState = new HashMap<>();

    public void put(String key, Object value) {
        scenarioState.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) scenarioState.get(key);
    }

    public boolean has(String key) {
        return scenarioState.containsKey(key);
    }

    public void clear() {
        scenarioState.clear();
    }
}
