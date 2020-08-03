package com.dmn.assignment.task1.service;

public interface CacheService {
    <T> T getItem(String key);
    <T> void updateItem(String key, T item);
    void removeItem(String key);
}
