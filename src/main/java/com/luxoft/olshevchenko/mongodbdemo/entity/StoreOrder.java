package com.luxoft.olshevchenko.mongodbdemo.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document
public class StoreOrder {

    @Id
    private String orderId;

    @Indexed(unique = true)
    private String orderName;

    private double orderPrice;

    private LocalDateTime orderCreationDate;

    public StoreOrder(String orderName, double orderPrice, LocalDateTime orderCreationDate) {
        this.orderName = orderName;
        this.orderPrice = orderPrice;
        this.orderCreationDate = orderCreationDate;
    }
}
