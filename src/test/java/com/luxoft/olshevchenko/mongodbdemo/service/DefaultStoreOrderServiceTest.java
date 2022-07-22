package com.luxoft.olshevchenko.mongodbdemo.service;

import com.luxoft.olshevchenko.mongodbdemo.entity.StoreOrder;
import com.luxoft.olshevchenko.mongodbdemo.repository.StoreOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Oleksandr Shevchenko
 */
@ExtendWith(MockitoExtension.class)
class DefaultStoreOrderServiceTest {

    @Mock
    private StoreOrderRepository storeOrderRepository;

    private DefaultStoreOrderService storeOrderService;

    @BeforeEach
    public void before() {
        storeOrderService = new DefaultStoreOrderService(storeOrderRepository);
    }

    @Test
    void testFindAll() {
        StoreOrder firstOrder = new StoreOrder("FirstOrder", 1.1, LocalDateTime.of(2022, Month.FEBRUARY,24,4, 0));
        firstOrder.setOrderId("62d4584f69a5cb7a36a706f1");

        StoreOrder secondOrder = new StoreOrder("SecondOrder", 2.2, LocalDateTime.of(2022, Month.FEBRUARY,24,5, 0));
        secondOrder.setOrderId("62d4584f69a5cb7a36a706f2");

        StoreOrder thirdOrder = new StoreOrder("ThirdOrder", 3.3, LocalDateTime.of(2022, Month.FEBRUARY,24,6, 0));
        thirdOrder.setOrderId("62d4584f69a5cb7a36a706f3");

        List<StoreOrder> storeOrderList = List.of(firstOrder, secondOrder, thirdOrder);

        Mockito.when(storeOrderRepository.findAll()).thenReturn(storeOrderList);

        List<StoreOrder> actualOrders = storeOrderService.findAll();
        assertNotNull(actualOrders);
        assertEquals(3, actualOrders.size());
        assertEquals("FirstOrder", actualOrders.get(0).getOrderName());
        assertEquals("SecondOrder", actualOrders.get(1).getOrderName());
        assertEquals("ThirdOrder", actualOrders.get(2).getOrderName());
        assertEquals(1.1, actualOrders.get(0).getOrderPrice());
        assertEquals(2.2, actualOrders.get(1).getOrderPrice());
        assertEquals(3.3, actualOrders.get(2).getOrderPrice());
        assertEquals(firstOrder, actualOrders.get(0));
        assertEquals(secondOrder, actualOrders.get(1));
        assertEquals(thirdOrder, actualOrders.get(2));
    }

    @Test
    void testGetById() {
        StoreOrder firstOrder = new StoreOrder("FirstOrder", 1.1, LocalDateTime.of(2022, Month.FEBRUARY,24,4, 0));
        firstOrder.setOrderId("62d4584f69a5cb7a36a706f1");

        Mockito.when(storeOrderRepository.findById("62d4584f69a5cb7a36a706f1")).thenReturn(Optional.of(firstOrder));
        StoreOrder actualOrder = storeOrderService.getById("62d4584f69a5cb7a36a706f1");
        StoreOrder actualOrderCache = storeOrderService.getById("62d4584f69a5cb7a36a706f1");
        assertEquals(actualOrderCache, storeOrderService.getCacheById().get("62d4584f69a5cb7a36a706f1"));
        assertEquals(actualOrderCache, storeOrderService.getCacheByName().get("FirstOrder"));
        assertEquals("FirstOrder", actualOrder.getOrderName());
        assertEquals(1.1, actualOrder.getOrderPrice());
        assertEquals(LocalDateTime.of(2022, Month.FEBRUARY,24,4, 0), actualOrder.getOrderCreationDate());
    }

    @Test
    void testGetByName() {
        StoreOrder firstOrder = new StoreOrder("FirstOrder", 1.1, LocalDateTime.of(2022, Month.FEBRUARY,24,4, 0));
        firstOrder.setOrderId("62d4584f69a5cb7a36a706f1");

        Mockito.when(storeOrderRepository.findStoreOrderByOrderName("FirstOrder")).thenReturn(Optional.of(firstOrder));
        StoreOrder actualOrder = storeOrderService.getByName("FirstOrder");
        StoreOrder actualOrderCache = storeOrderService.getByName("FirstOrder");
        assertEquals(actualOrderCache, storeOrderService.getCacheById().get("62d4584f69a5cb7a36a706f1"));
        assertEquals(actualOrderCache, storeOrderService.getCacheByName().get("FirstOrder"));
        assertEquals("FirstOrder", actualOrder.getOrderName());
        assertEquals(1.1, actualOrder.getOrderPrice());
        assertEquals(LocalDateTime.of(2022, Month.FEBRUARY,24,4, 0), actualOrder.getOrderCreationDate());
    }
}