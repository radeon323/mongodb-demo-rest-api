package com.luxoft.olshevchenko.mongodbdemo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luxoft.olshevchenko.mongodbdemo.entity.StoreOrder;
import com.luxoft.olshevchenko.mongodbdemo.service.StoreOrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Oleksandr Shevchenko
 */
@WebMvcTest(StoreOrderController.class)
class StoreOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StoreOrderService storeOrderService;

    @Test
    void testFetchStoreOrders() throws Exception {
        StoreOrder firstOrder = new StoreOrder("FirstOrder", 1.1, LocalDateTime.of(2022, Month.FEBRUARY,24,4, 0));
        firstOrder.setOrderId("62d4584f69a5cb7a36a706f1");

        StoreOrder secondOrder = new StoreOrder("SecondOrder", 2.2, LocalDateTime.of(2022, Month.FEBRUARY,24,5, 0));
        secondOrder.setOrderId("62d4584f69a5cb7a36a706f2");

        StoreOrder thirdOrder = new StoreOrder("ThirdOrder", 3.3, LocalDateTime.of(2022, Month.FEBRUARY,24,6, 0));
        thirdOrder.setOrderId("62d4584f69a5cb7a36a706f3");

        List<StoreOrder> storeOrderList = List.of(firstOrder, secondOrder, thirdOrder);

        when(storeOrderService.findAll()).thenReturn(storeOrderList);
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/api/v1/store-orders/")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].orderName").value("FirstOrder"))
                .andExpect(jsonPath("$[0].orderPrice").value(1.1))
                .andExpect(jsonPath("$[1].orderName").value("SecondOrder"))
                .andExpect(jsonPath("$[1].orderPrice").value(2.2))
                .andExpect(jsonPath("$[2].orderName").value("ThirdOrder"))
                .andExpect(jsonPath("$[2].orderPrice").value(3.3));
        verify(storeOrderService, times(1)).findAll();
    }

    @Test
    void testFetchStoreOrdersGetOrderById() throws Exception {
        StoreOrder firstOrder = new StoreOrder("FirstOrder", 1.1, LocalDateTime.of(2022, Month.FEBRUARY,24,4, 0));
        firstOrder.setOrderId("62d4584f69a5cb7a36a706f1");

        when(storeOrderService.getById("62d4584f69a5cb7a36a706f1")).thenReturn(firstOrder);
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/api/v1/store-orders?id={id}","62d4584f69a5cb7a36a706f1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].orderName").value("FirstOrder"))
                .andExpect(jsonPath("$[0].orderPrice").value(1.1));
        verify(storeOrderService, times(1)).getById("62d4584f69a5cb7a36a706f1");
    }

    @Test
    void testFetchStoreOrdersGetOrderByName() throws Exception {
        StoreOrder firstOrder = new StoreOrder("FirstOrder", 1.1, LocalDateTime.of(2022, Month.FEBRUARY,24,4, 0));
        firstOrder.setOrderId("62d4584f69a5cb7a36a706f1");

        when(storeOrderService.getByName("FirstOrder")).thenReturn(firstOrder);
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/api/v1/store-orders?name={name}","FirstOrder")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].orderName").value("FirstOrder"))
                .andExpect(jsonPath("$[0].orderPrice").value(1.1));
        verify(storeOrderService, times(1)).getByName("FirstOrder");
    }

    @Test
    void testSaveStoreOrder() throws Exception {
        StoreOrder firstOrder = new StoreOrder("FirstOrder", 1.1, LocalDateTime.of(2022, Month.FEBRUARY,24,4, 0));
        firstOrder.setOrderId("62d4584f69a5cb7a36a706f1");

        mockMvc.perform( MockMvcRequestBuilders
                        .post("/api/v1/store-orders/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firstOrder)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderName").value("FirstOrder"))
                .andExpect(jsonPath("$.orderPrice").value(1.1));
        verify(storeOrderService).save(any(StoreOrder.class));
    }

    @Test
    void testUpdateStoreOrder() throws Exception {
        StoreOrder firstOrder = new StoreOrder("FirstOrder", 1.1, LocalDateTime.of(2022, Month.FEBRUARY,24,4, 0));
        firstOrder.setOrderId("62d4584f69a5cb7a36a706f1");

        mockMvc.perform( MockMvcRequestBuilders
                        .put("/api/v1/store-orders?id={id}","62d4584f69a5cb7a36a706f1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firstOrder)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderName").value("FirstOrder"))
                .andExpect(jsonPath("$.orderPrice").value(1.1));
        verify(storeOrderService).save(any(StoreOrder.class));
    }

    @Test
    void testDeleteStoreOrder() throws Exception {
        StoreOrder firstOrder = new StoreOrder("FirstOrder", 1.1, LocalDateTime.of(2022, Month.FEBRUARY,24,4, 0));
        firstOrder.setOrderId("62d4584f69a5cb7a36a706f1");

        mockMvc.perform( MockMvcRequestBuilders
                        .delete("/api/v1/store-orders?id={id}","62d4584f69a5cb7a36a706f1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firstOrder)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("62d4584f69a5cb7a36a706f1"));
        verify(storeOrderService, times(1)).delete("62d4584f69a5cb7a36a706f1");
    }
}