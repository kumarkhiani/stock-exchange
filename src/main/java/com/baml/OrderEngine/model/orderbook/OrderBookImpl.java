package com.baml.OrderEngine.model.orderbook;

import com.baml.OrderEngine.model.order.BuyComparator;
import com.baml.OrderEngine.model.order.Order;
import com.baml.OrderEngine.model.order.SellComparator;
import com.baml.OrderEngine.model.trade.Trade;
import com.baml.OrderEngine.model.util.OrderBookUtil;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Arrays.sort;

@Component
public class OrderBookImpl implements OrderBook {
    private final PriorityBlockingQueue<Order> buyOrders = new PriorityBlockingQueue<>(10, new BuyComparator());
    private final PriorityBlockingQueue<Order> sellOrders = new PriorityBlockingQueue<>(10, new SellComparator());
    private final List<Trade> tradesHistory = new ArrayList<>();
    private final AtomicInteger idCounter = new AtomicInteger(0);

    public synchronized int getCurrentCounterId() {
        return idCounter.incrementAndGet();
    }

    public void storeOrder(Order order) {
        switch (order.getSide()) {
            case BUY:
                buyOrders.add(order);
                break;
            case SELL:
                sellOrders.add(order);
                break;
            default:
                throw new IllegalArgumentException(OrderBookUtil.UNKNOWN_ORDER_SIDE);
        }
    }

    public List<Trade> tradeOrder(Order incomingOrder) {
        PriorityBlockingQueue<Order> existingOrders = determineOrderQueue(incomingOrder);
        List<Trade> trades = tradeOrder(incomingOrder, existingOrders);
        if (!trades.isEmpty()) {
            tradesHistory.addAll(trades);
        }
        return trades;
    }

    private synchronized List<Trade> tradeOrder(Order incomingOrder, PriorityBlockingQueue<Order> existingOrders) {
        List<Trade> trades = new ArrayList<>();
        while (incomingOrder.getQuantity() > 0) {
            Order existingOrder = existingOrders.peek();
            if (existingOrder == null || !isValidTradePrice(incomingOrder, existingOrder)) {
                storeOrder(incomingOrder);
                return trades;
            }
            int existingQuantity = existingOrder.getQuantity();
            int incomingQuantity = incomingOrder.getQuantity();
            if (incomingQuantity >= existingQuantity) {
                trades.add(handleFullOrder(existingOrders));
                int newIncomingQuantity = incomingQuantity - existingQuantity;
                incomingOrder.setQuantity(newIncomingQuantity);
            } else {
                trades.add(handlePartialOrder(existingOrders, incomingQuantity));
                incomingOrder.setQuantity(0);
            }
        }
        return trades;
    }

    private Trade handleFullOrder(PriorityBlockingQueue<Order> existingOrders) {
        Order order = existingOrders.remove();
        return new Trade(order.getId(), order.getPrice(), order.getQuantity());
    }

    private Trade handlePartialOrder(PriorityBlockingQueue<Order> existingOrders, int remainingIncomingQuantity) {
        Order order = existingOrders.remove();
        Trade trade = new Trade(order.getId(), order.getPrice(), remainingIncomingQuantity);
        order.setQuantity(order.getQuantity() - remainingIncomingQuantity);
        existingOrders.add(order);
        return trade;
    }

    private PriorityBlockingQueue<Order> determineOrderQueue(Order incomingOrder) {
        switch (incomingOrder.getSide()) {
            case BUY:
                return this.sellOrders;
            case SELL:
                return this.buyOrders;
            default:
                throw new IllegalArgumentException(OrderBookUtil.UNKNOWN_ORDER_SIDE);
        }
    }

    private boolean isValidTradePrice(Order incomingOrder, Order existingOrder) {
        switch (incomingOrder.getSide()) {
            case BUY:
                return incomingOrder.getPrice().compareTo(existingOrder.getPrice()) >= 0;
            case SELL:
                return incomingOrder.getPrice().compareTo(existingOrder.getPrice()) <= 0;
            default:
                throw new IllegalArgumentException(OrderBookUtil.UNKNOWN_ORDER_SIDE);
        }
    }

    PriorityBlockingQueue<Order> getBuyOrders() {
        return buyOrders;
    }

    PriorityBlockingQueue<Order> getSellOrders() {
        return sellOrders;
    }

    public ArrayList<Order> getSortedOrdersForDisplay() {
        Order[] sortedSellOrders = sellOrders.toArray(new Order[0]);
        Order[] sortedBuyOrders = buyOrders.toArray(new Order[0]);
        sort(sortedSellOrders, new SellComparator().reversed());
        sort(sortedBuyOrders, new BuyComparator());
        ArrayList<Order> sortedList = new ArrayList<>();
        sortedList.addAll(Arrays.asList(sortedSellOrders));
        sortedList.addAll(Arrays.asList(sortedBuyOrders));
        return sortedList;
    }

    @Override
    public List<Trade> getTradeHistory() {
        return tradesHistory;
    }

}
