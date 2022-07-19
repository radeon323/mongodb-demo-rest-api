package com.luxoft.olshevchenko.mongodbdemo.service;

import com.luxoft.olshevchenko.mongodbdemo.entity.StoreOrder;

import java.util.List;

public interface StoreOrderService {
    List<StoreOrder> findAll();
    StoreOrder getById(String id);
    StoreOrder getByName(String name);
    void delete(String id);
    void save(StoreOrder storeOrder);
}
