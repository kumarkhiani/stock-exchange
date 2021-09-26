package com.baml.OrderEngine.model.trade;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Trade {
    private final int id;
    private final BigDecimal price;
    private final int quantity;

    public Trade(int id, BigDecimal price, int quantity) {
        this.id = id;
        this.price = price;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public BigDecimal getPrice() {
        return price.setScale(2, RoundingMode.HALF_UP);
    }

    public int getQuantity() {
        return quantity;
    }
}