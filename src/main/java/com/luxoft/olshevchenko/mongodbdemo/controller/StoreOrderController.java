package com.luxoft.olshevchenko.mongodbdemo.controller;

import com.luxoft.olshevchenko.mongodbdemo.entity.StoreOrder;
import com.luxoft.olshevchenko.mongodbdemo.service.StoreOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Oleksandr Shevchenko
 */
@RestController
@RequestMapping("api/v1/store-orders")
@RequiredArgsConstructor
public class StoreOrderController {

    private final StoreOrderService storeOrderService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<StoreOrder> fetchStoreOrders(@RequestParam(value="id", required = false) String id,
                                             @RequestParam(value="name", required = false) String name) {
        List<StoreOrder> orders = null;

        if (id != null) {
            StoreOrder order = storeOrderService.getById(id);
            if (order != null) {
                orders = List.of(order);
            }
        } else if (name != null) {
            StoreOrder order = storeOrderService.getByName(name);
            if (order != null) {
                orders = List.of(order);
            }
        } else {
            orders = storeOrderService.findAll();
        }
        return orders;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public StoreOrder saveStoreOrder(@RequestBody @Valid StoreOrder order) {
        storeOrderService.save(order);
        return order;
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public StoreOrder updateStoreOrder(@RequestBody @Valid StoreOrder order, @RequestParam(value="id") String id) {
        order.setOrderId(id);
        storeOrderService.save(order);
        return order;
    }

    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public String deleteStoreOrder(@RequestParam(value="id") String id) {
        storeOrderService.delete(id);
        return id;
    }


}
