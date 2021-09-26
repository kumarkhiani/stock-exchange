package com.baml.OrderEngine.model.order;

import java.util.Comparator;

public class SellComparator implements Comparator<Order> {

    @Override
    public int compare(Order sellOrder1, Order sellOrder2) {
        if (sellOrder1.getPrice().compareTo(sellOrder2.getPrice()) == 0) {
            if (sellOrder1.getTime().isEqual(sellOrder2.getTime())) {
                return sellOrder1.getId() < sellOrder2.getId() ? -1 : 1;
            }
            return sellOrder1.getTime().compareTo(sellOrder2.getTime());
        }
        return sellOrder1.getPrice().compareTo(sellOrder2.getPrice());
    }
}
