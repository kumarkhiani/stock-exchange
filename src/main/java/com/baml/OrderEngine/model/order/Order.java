package com.baml.OrderEngine.model.order;

import com.baml.OrderEngine.exception.OrderCreationException;
import com.baml.OrderEngine.model.side.Side;
import com.baml.OrderEngine.rest.dto.OrderDto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

public class Order {
    private final int id;
    private final BigDecimal price;
    private final LocalDateTime time;
    private final Side side;
    private int quantity;

    public Order(int id, OrderDto orderDto) throws OrderCreationException {
        this(id, orderDto.getPrice(), orderDto.getQuantity(), orderDto.getSide(), orderDto.getTime() == null ? LocalDateTime.now() : orderDto.getTime());
    }

    public Order(int id, BigDecimal price, int quantity, Side side, LocalDateTime time) throws OrderCreationException {
        if (price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new OrderCreationException("Price cannot be less than or equal to 0");
        }
        if (quantity <= 0) {
            throw new OrderCreationException("Quantity cannot be less than or equal to 0");
        }
        if (side == null) {
            throw new OrderCreationException("Side cannot be null");
        }
        this.id = id;
        this.price = price;
        this.quantity = quantity;
        this.side = side;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public BigDecimal getPrice() {
        return price.setScale(2, RoundingMode.HALF_UP);
    }

    public LocalDateTime getTime() {
        return time;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Side getSide() {
        return side;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", price=" + price +
                ", time=" + time +
                ", quantity=" + quantity +
                ", side=" + side +
                '}';
    }
}
