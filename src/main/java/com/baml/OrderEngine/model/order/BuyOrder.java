package com.baml.OrderEngine.model.order;

import com.baml.OrderEngine.exception.OrderCreationException;
import com.baml.OrderEngine.model.side.Side;
import com.baml.OrderEngine.rest.dto.OrderDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BuyOrder extends Order {
    public BuyOrder(int id, BigDecimal price, int quantity, LocalDateTime time) throws OrderCreationException {
        super(id, price, quantity, Side.BUY, time);
    }

    public BuyOrder(int id, OrderDto orderDto) throws OrderCreationException {
        super(id, orderDto);
    }
}
