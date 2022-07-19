package com.luxoft.olshevchenko.mongodbdemo.service;

import com.luxoft.olshevchenko.mongodbdemo.entity.StoreOrder;
import com.luxoft.olshevchenko.mongodbdemo.repository.StoreOrderRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class DefaultStoreOrderService implements StoreOrderService {

    private final StoreOrderRepository repository;
    Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public List<StoreOrder> findAll() {
        return repository.findAll();
    }

    @Override
    public StoreOrder getById(String id) {
        Optional<StoreOrder> optionalOrder = repository.findById(id);
        StoreOrder order = null;
        if (optionalOrder.isPresent()) {
            order = optionalOrder.get();
        } else {
            logger.info("Order with id {} not found", id);
        }
        return order;
    }

    @Override
    public StoreOrder getByName(String name) {
        Optional<StoreOrder> optionalOrder = repository.findStoreOrderByOrderName(name);
        StoreOrder order = null;
        if (optionalOrder.isPresent()) {
            order = optionalOrder.get();
        } else {
            logger.info("Order with name {} not found", name);
        }
        return order;
    }

    @Override
    public void delete(String id) {
        Optional<StoreOrder> optionalOrder = repository.findById(id);
        if (optionalOrder.isPresent()) {
            repository.deleteById(id);
            logger.info("Order with id {} has been deleted!", id);
        } else {
            logger.info("Order with id {} does not exists!", id);
        }
    }

    @Override
    public void save(StoreOrder storeOrder) {
        storeOrder.setOrderCreationDate(LocalDateTime.now());
        repository.save(storeOrder);
        logger.info("Order with id {} has been created!", storeOrder.getOrderId());
    }


}
