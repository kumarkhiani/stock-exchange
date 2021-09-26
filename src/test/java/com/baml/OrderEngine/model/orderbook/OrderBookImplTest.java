package com.baml.OrderEngine.model.orderbook;

import com.baml.OrderEngine.exception.OrderCreationException;
import com.baml.OrderEngine.model.order.BuyOrder;
import com.baml.OrderEngine.model.order.Order;
import com.baml.OrderEngine.model.order.SellOrder;
import com.baml.OrderEngine.model.trade.Trade;
import com.baml.OrderEngine.model.util.OrderBookUtil;
import com.baml.OrderEngine.rest.mapper.OrderDtoMapper;
import com.baml.OrderEngine.service.OrderBookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class OrderBookImplTest {
    public OrderBookImpl orderBook;

    @BeforeEach
    public void before() throws OrderCreationException {
        orderBook = new OrderBookImpl();
        OrderDtoMapper mapper = new OrderDtoMapper();
        OrderBookService service = new OrderBookService(orderBook, mapper);
        initializeOrderBook(orderBook, service, mapper);
    }

    @Test
    public void testBasicUseCaseBuy() throws OrderCreationException {
        List<Trade> trades = orderBook.tradeOrder(new BuyOrder(orderBook.getCurrentCounterId(), BigDecimal.valueOf(20.35), 250, LocalDateTime.of(LocalDate.now(), LocalTime.of(9, 10))));

        assertThat(trades.size(), is(equalTo(3)));
        assertThat(trades.get(0), is(samePropertyValuesAs(new Trade(2, BigDecimal.valueOf(20.25), 100))));
        assertThat(trades.get(1), is(samePropertyValuesAs(new Trade(1, BigDecimal.valueOf(20.30), 100))));
        assertThat(trades.get(2), is(samePropertyValuesAs(new Trade(3, BigDecimal.valueOf(20.30), 50))));

        PriorityBlockingQueue<Order> buyOrders = orderBook.getBuyOrders();
        PriorityBlockingQueue<Order> sellOrders = orderBook.getSellOrders();
        assertThat(sellOrders.size(), is(equalTo(1)));
        assertThat(sellOrders.peek(), is(samePropertyValuesAs(new SellOrder(3, BigDecimal.valueOf(20.30), 150, LocalDateTime.of(LocalDate.now(), LocalTime.of(9, 5))))));
        assertThat(buyOrders.size(), is(equalTo(3)));
        assertThat(buyOrders.peek(), is(samePropertyValuesAs(new BuyOrder(5, BigDecimal.valueOf(20.20), 200, LocalDateTime.of(LocalDate.now(), LocalTime.of(9, 8))))));
    }

    @Test
    public void testBasicUseCaseSell() throws OrderCreationException {
        List<Trade> trades = orderBook.tradeOrder(new SellOrder(7, BigDecimal.valueOf(20.10), 250, LocalDateTime.of(LocalDate.now(), LocalTime.of(9, 10))));

        assertThat(trades.size(), is(equalTo(2)));
        assertThat(trades.get(0), is(samePropertyValuesAs(new Trade(5, BigDecimal.valueOf(20.20), 200))));
        assertThat(trades.get(1), is(samePropertyValuesAs(new Trade(4, BigDecimal.valueOf(20.15), 50))));

        PriorityBlockingQueue<Order> buyOrders = orderBook.getBuyOrders();
        PriorityBlockingQueue<Order> sellOrders = orderBook.getSellOrders();
        assertThat(buyOrders.size(), is(equalTo(2)));
        assertThat(buyOrders.peek(), is(samePropertyValuesAs(new BuyOrder(4, BigDecimal.valueOf(20.15), 50, LocalDateTime.of(LocalDate.now(), LocalTime.of(9, 6))))));
        assertThat(sellOrders.size(), is(equalTo(3)));
        assertThat(sellOrders.peek(), is(samePropertyValuesAs(new SellOrder(2, BigDecimal.valueOf(20.25), 100, LocalDateTime.of(LocalDate.now(), LocalTime.of(9, 3))))));
    }

    @Test
    public void ifCannotTradeIncomingOrderThenItShouldBeStored() throws OrderCreationException {
        List<Trade> trades = orderBook.tradeOrder(new BuyOrder(7, BigDecimal.valueOf(20.20), 250, LocalDateTime.of(LocalDate.now(), LocalTime.of(9, 10))));

        //Cannot Trade
        assertThat(trades.size(), is(equalTo(0)));

        PriorityBlockingQueue<Order> buyOrders = orderBook.getBuyOrders();
        PriorityBlockingQueue<Order> sellOrders = orderBook.getSellOrders();
        //Sell order unchanged
        assertThat(sellOrders.size(), is(equalTo(3)));
        assertThat(sellOrders.peek(), is(samePropertyValuesAs(new SellOrder(2, BigDecimal.valueOf(20.25), 100, LocalDateTime.of(LocalDate.now(), LocalTime.of(9, 3))))));
        //Added to buy Order
        assertThat(buyOrders.size(), is(equalTo(4)));
        assertThat(buyOrders.poll(), is(samePropertyValuesAs(new BuyOrder(5, BigDecimal.valueOf(20.20), 200, LocalDateTime.of(LocalDate.now(), LocalTime.of(9, 8))))));
        assertThat(buyOrders.poll(), is(samePropertyValuesAs(new BuyOrder(7, BigDecimal.valueOf(20.20), 250, LocalDateTime.of(LocalDate.now(), LocalTime.of(9, 10))))));
    }

    private void initializeOrderBook(OrderBookImpl orderBook, OrderBookService service, OrderDtoMapper mapper) throws OrderCreationException {
        OrderBookUtil.initializeOrderBook(service, orderBook, mapper);
    }

    @Test
    public void testOrderBook() throws OrderCreationException {
        for (int i = 0; i < 100; i++) {
            if (i % 3 == 0) {
                orderBook.storeOrder(new BuyOrder(i, BigDecimal.valueOf(Math.random() * 10 % 5 + 1), 100, LocalDateTime.now()));
            } else {
                orderBook.storeOrder(new SellOrder(i, BigDecimal.valueOf(Math.random() * 10 % 5 + 1), 100, LocalDateTime.now()));
            }
        }
        printOrderBook(orderBook);
    }

    private void printOrderBook(OrderBookImpl orderBook) {
        printOrders(orderBook.getSellOrders());
        printOrders(orderBook.getBuyOrders());
    }

    private <T> void printOrders(PriorityBlockingQueue<T> orders) {
        for (T order : orders) {
            System.out.println(orders.remove());
        }
    }
}