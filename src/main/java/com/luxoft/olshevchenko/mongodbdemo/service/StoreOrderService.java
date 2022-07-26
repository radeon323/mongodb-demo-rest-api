package com.luxoft.olshevchenko.mongodbdemo.service;

import com.luxoft.olshevchenko.mongodbdemo.entity.StoreOrder;

import java.util.List;

/**
 * @author Oleksandr Shevchenko
 */
public interface StoreOrderService {
    List<StoreOrder> findAll();
    StoreOrder getById(String id);
    StoreOrder getByName(String name);
    String delete(String id);
    StoreOrder save(StoreOrder storeOrder);
}
