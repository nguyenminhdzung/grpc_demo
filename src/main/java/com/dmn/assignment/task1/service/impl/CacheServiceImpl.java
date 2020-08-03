package com.dmn.assignment.task1.service.impl;

import com.dmn.assignment.task1.service.CacheService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/***
 * NOTE: For demo purpose, this is a simple in-memory cache implementation.
 *       In production, this should use a high performance distributed cache such as Redis.
 */
@Service
public class CacheServiceImpl implements CacheService {

    private Map<String, Object> items = new HashMap<>();

    @Override
    public <T> T getItem(String key) {
        return (T)items.get(key);
    }

    @Override
    public <T> void updateItem(String key, T item) {
        items.put(key, item);
    }

    @Override
    public void removeItem(String key) {
        items.remove(key);
    }
}
