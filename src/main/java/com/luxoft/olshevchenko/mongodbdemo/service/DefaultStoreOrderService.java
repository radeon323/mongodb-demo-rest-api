package com.luxoft.olshevchenko.mongodbdemo.service;

import com.luxoft.olshevchenko.mongodbdemo.entity.StoreOrder;
import com.luxoft.olshevchenko.mongodbdemo.repository.StoreOrderRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Oleksandr Shevchenko
 */
@RequiredArgsConstructor
@Service
public class DefaultStoreOrderService implements StoreOrderService {
    private final StoreOrderRepository repository;
    private final Map<String, StoreOrder> cacheById = new ConcurrentHashMap<>();
    private final Map<String, StoreOrder> cacheByName = new ConcurrentHashMap<>();
    Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    public List<StoreOrder> findAll() {
        return repository.findAll();
    }

    @Override
    public StoreOrder getById(String id) {
        long timeBefore = System.currentTimeMillis();
        if (cacheById.containsKey(id)) {
            calculateAndLogCacheRequestTime(timeBefore);
            return cacheById.get(id);
        } else {
            Optional<StoreOrder> optionalOrder = repository.findById(id);
            StoreOrder order = null;
            if (optionalOrder.isPresent()) {
                order = optionalOrder.get();
                cacheById.put(id, order);
                mergeCaches(cacheByName, order, order.getOrderName());
            } else {
                logger.info("Order with id {} not found", id);
            }
            calculateAndLogDbRequestTime(timeBefore);
            return order;
        }
    }

    @Override
    public StoreOrder getByName(String name) {
        long timeBefore = System.currentTimeMillis();
        if (cacheByName.containsKey(name)) {
            calculateAndLogCacheRequestTime(timeBefore);
            return cacheByName.get(name);
        } else {
            Optional<StoreOrder> optionalOrder = repository.findStoreOrderByOrderName(name);
            StoreOrder order = null;
            if (optionalOrder.isPresent()) {
                order = optionalOrder.get();
                cacheByName.put(name, order);
                mergeCaches(cacheById, order, order.getOrderId());
            } else {
                logger.info("Order with name {} not found", name);
            }
            calculateAndLogDbRequestTime(timeBefore);
            return order;
        }
    }

    @Override
    public synchronized String delete(String id) {
        Optional<StoreOrder> optionalOrder = repository.findById(id);
        if (optionalOrder.isPresent()) {
            StoreOrder order = cacheById.get(id);
            cacheByName.remove(order.getOrderName());
            cacheById.remove(id);
            
            repository.deleteById(id);
            logger.info("Order with id {} has been deleted!", id);
        } else {
            logger.info("Order with id {} does not exists!", id);
        }
        return id;
    }

    @Override
    public synchronized StoreOrder save(StoreOrder storeOrder) {
        storeOrder.setOrderCreationDate(LocalDateTime.now());

        String id = storeOrder.getOrderId();
        if (id != null && cacheById.containsKey(id)) {
            cacheById.put(id, storeOrder);
        }
        String name = storeOrder.getOrderName();
        if (id != null && cacheByName.containsKey(name)) {
            cacheByName.put(name, storeOrder);
        }

        repository.save(storeOrder);
        logger.info("Order with id {} has been created!", storeOrder.getOrderId());
        return storeOrder;
    }


    private void calculateAndLogDbRequestTime(long timeBefore) {
        long timeDB = System.currentTimeMillis();
        logger.info("\u001B[34m" + "DataBase request execution time is {} ms" + "\u001B[0m", timeDB-timeBefore);
    }

    private void calculateAndLogCacheRequestTime(long timeBefore) {
        long timeCache = System.currentTimeMillis();
        logger.info("\u001B[33m" + "Cache request execution time is {} ms" + "\u001B[0m", timeCache-timeBefore);
    }

    private void mergeCaches(Map<String, StoreOrder> cache, StoreOrder order, String key) {
        if (!cache.containsKey(key)) {
            cache.put(key, order);
        }
    }


    public Map<String, StoreOrder> getCacheById() {
        return cacheById;
    }

    public Map<String, StoreOrder> getCacheByName() {
        return cacheByName;
    }


}
