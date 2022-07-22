package com.luxoft.olshevchenko.mongodbdemo.repository;

import com.luxoft.olshevchenko.mongodbdemo.entity.StoreOrder;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Oleksandr Shevchenko
 */
@Repository
public interface StoreOrderRepository extends MongoRepository<StoreOrder, String> {
    Optional<StoreOrder> findStoreOrderByOrderName(String orderName);
}
