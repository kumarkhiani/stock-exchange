package com.baml.OrderEngine.model.order;

import java.util.Comparator;

public class BuyComparator implements Comparator<Order> {

    @Override
    public int compare(Order buyOrder1, Order buyOrder2) {
        if (buyOrder1.getPrice().compareTo(buyOrder2.getPrice()) == 0) {
            if (buyOrder1.getTime().isEqual(buyOrder1.getTime())) {
                return buyOrder1.getId() < buyOrder2.getId() ? -1 : 1;
            }
            return buyOrder1.getTime().compareTo(buyOrder2.getTime());
        }
        return buyOrder1.getPrice().compareTo(buyOrder2.getPrice()) * -1;
    }

}
