package com.trade.orderbook.model;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderBookBuySideTest {

    @Test
    public void addOrder_updatesBestPrice() {
        OrderBookBuySide orderBookBuySide = new OrderBookBuySide();
        orderBookBuySide.addOrder(new Order(1, "Gold", Side.BUY, 250, 1));
        orderBookBuySide.addOrder(new Order(2, "Gold", Side.BUY, 265, 1));
        assertThat(orderBookBuySide.getBestPrice()).hasValue(265L);
    }

    @Test
    public void removeOrder_updatesBestPrice() {
        OrderBookBuySide orderBookBuySide = new OrderBookBuySide();
        orderBookBuySide.addOrder(new Order(1, "Gold", Side.BUY, 250, 1));
        orderBookBuySide.addOrder(new Order(2, "Gold", Side.BUY, 265, 1));
        orderBookBuySide.addOrder(new Order(3, "Gold", Side.BUY, 275, 1));
        orderBookBuySide.addOrder(new Order(4, "Gold", Side.BUY, 275, 1));
        orderBookBuySide.removeOrder(3);
        assertThat(orderBookBuySide.getBestPrice()).hasValue(275L);
        orderBookBuySide.removeOrder(4);
        assertThat(orderBookBuySide.getBestPrice()).hasValue(265L);
    }
}
