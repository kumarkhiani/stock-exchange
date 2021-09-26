package com.baml.OrderEngine.model.orderbook;

import com.baml.OrderEngine.model.order.Order;
import com.baml.OrderEngine.model.trade.Trade;

import java.util.List;

public interface OrderBook {

    void storeOrder(Order order);

    List<Trade> tradeOrder(Order order);

    List<Order> getSortedOrdersForDisplay();

    List<Trade> getTradeHistory();

    int getCurrentCounterId();
}
