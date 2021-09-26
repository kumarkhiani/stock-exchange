package com.baml.OrderEngine.rest.dto;

import com.baml.OrderEngine.model.side.Side;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderDto {
    private BigDecimal price;
    private Side side;
    private int quantity;
    private LocalDateTime time;

    public OrderDto(BigDecimal price, Side side, int quantity, LocalDateTime time) {
        this.price = price;
        this.side = side;
        this.quantity = quantity;
        this.time = time;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Side getSide() {
        return side;
    }

    public void setSide(Side side) {
        this.side = side;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
