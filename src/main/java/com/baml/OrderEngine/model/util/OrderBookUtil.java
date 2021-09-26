package com.baml.OrderEngine.model.util;

import com.baml.OrderEngine.exception.OrderCreationException;
import com.baml.OrderEngine.model.orderbook.OrderBook;
import com.baml.OrderEngine.model.side.Side;
import com.baml.OrderEngine.rest.dto.OrderDto;
import com.baml.OrderEngine.rest.mapper.OrderDtoMapper;
import com.baml.OrderEngine.service.OrderBookService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class OrderBookUtil {

    public static final String UNKNOWN_ORDER_SIDE = "Unknown order side";

    public static void initializeOrderBook(OrderBookService service, OrderBook orderBook, OrderDtoMapper mapper) throws OrderCreationException {
        orderBook.storeOrder(mapper.mapOrderDto(service.getCurrentCounterId(), new OrderDto(BigDecimal.valueOf(20.30), Side.SELL, 100, LocalDateTime.of(LocalDate.now(), LocalTime.of(9, 1)))));
        orderBook.storeOrder(mapper.mapOrderDto(service.getCurrentCounterId(), new OrderDto(BigDecimal.valueOf(20.25), Side.SELL, 100, LocalDateTime.of(LocalDate.now(), LocalTime.of(9, 3)))));
        orderBook.storeOrder(mapper.mapOrderDto(service.getCurrentCounterId(), new OrderDto(BigDecimal.valueOf(20.30), Side.SELL, 200, LocalDateTime.of(LocalDate.now(), LocalTime.of(9, 5)))));
        orderBook.storeOrder(mapper.mapOrderDto(service.getCurrentCounterId(), new OrderDto(BigDecimal.valueOf(20.15), Side.BUY, 100, LocalDateTime.of(LocalDate.now(), LocalTime.of(9, 6)))));
        orderBook.storeOrder(mapper.mapOrderDto(service.getCurrentCounterId(), new OrderDto(BigDecimal.valueOf(20.20), Side.BUY, 200, LocalDateTime.of(LocalDate.now(), LocalTime.of(9, 8)))));
        orderBook.storeOrder(mapper.mapOrderDto(service.getCurrentCounterId(), new OrderDto(BigDecimal.valueOf(20.15), Side.BUY, 200, LocalDateTime.of(LocalDate.now(), LocalTime.of(9, 9)))));
    }
}
