package com.luxoft.olshevchenko.mongodbdemo.repository;

import com.luxoft.olshevchenko.mongodbdemo.entity.StoreOrder;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface StoreOrderRepository extends MongoRepository<StoreOrder, String> {
    Optional<StoreOrder> findStoreOrderByOrderName(String orderName);
}
