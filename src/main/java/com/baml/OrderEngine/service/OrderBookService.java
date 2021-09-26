package com.baml.OrderEngine.service;

import com.baml.OrderEngine.exception.OrderCreationException;
import com.baml.OrderEngine.model.order.Order;
import com.baml.OrderEngine.model.orderbook.OrderBook;
import com.baml.OrderEngine.model.trade.Trade;
import com.baml.OrderEngine.model.util.OrderBookUtil;
import com.baml.OrderEngine.rest.mapper.OrderDtoMapper;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderBookService {

    private final OrderBook orderBook;
    private final OrderDtoMapper mapper;

    public OrderBookService(OrderBook orderBook, OrderDtoMapper mapper) {
        this.orderBook = orderBook;
        this.mapper = mapper;
    }

    public List<Trade> tradeOrder(Order order) {
        return orderBook.tradeOrder(order);
    }

    public List<Order> getSortedOrdersForDisplay() {
        return orderBook.getSortedOrdersForDisplay();
    }

    public List<Trade> getTradeHistory() {
        return orderBook.getTradeHistory();
    }

    public int getCurrentCounterId() {
        return orderBook.getCurrentCounterId();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() throws OrderCreationException {
        OrderBookUtil.initializeOrderBook(this, orderBook, mapper);
    }
}
