package com.baml.OrderEngine.rest.controller;

import com.baml.OrderEngine.exception.OrderCreationException;
import com.baml.OrderEngine.model.order.Order;
import com.baml.OrderEngine.model.trade.Trade;
import com.baml.OrderEngine.rest.dto.OrderDto;
import com.baml.OrderEngine.rest.mapper.OrderDtoMapper;
import com.baml.OrderEngine.service.OrderBookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController()
public class OrderBookRestController {

    OrderBookService service;
    OrderDtoMapper mapper;

    public OrderBookRestController(OrderBookService service, OrderDtoMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping("/api/getOrderBook")
    public List<Order> getOrderBook() {
        return service.getSortedOrdersForDisplay();
    }

    @GetMapping("/api/getTrades")
    public List<Trade> getTradeHistory() {
        return service.getTradeHistory();
    }

    @PostMapping(value = "/api/tradeOrder", consumes = "text/plain", produces = "application/json")
    public ResponseEntity tradeOrder(@RequestBody String instructions) {
        try {
            OrderDto orderDto = mapper.mapInstructions(instructions);
            Order order = mapper.mapOrderDto(service.getCurrentCounterId(), orderDto);
            return ResponseEntity.of(Optional.of(service.tradeOrder(order)));
        } catch (OrderCreationException e) {
            return ResponseEntity.of(Optional.of(e.getMessage()));
        }
    }
}
