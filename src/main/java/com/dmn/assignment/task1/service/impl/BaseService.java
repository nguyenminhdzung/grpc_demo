package com.dmn.assignment.task1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseService {
    @Autowired
    private EntityManager entityManager;

    protected <TE> TE findByIdWithLock(Class<TE> clazz, Object id) {
        TE entity = entityManager.getReference(clazz, id);
        entityManager.refresh(entity, LockModeType.PESSIMISTIC_WRITE);

        return entity;
    }

    protected <TE> void lockForUpdate(TE entity) {
        Map<String,Object> properties = new HashMap<>();
        properties.put("javax.persistence.lock.timeout", 2000);
        entityManager.refresh(entity, LockModeType.PESSIMISTIC_WRITE, properties);
    }
}
