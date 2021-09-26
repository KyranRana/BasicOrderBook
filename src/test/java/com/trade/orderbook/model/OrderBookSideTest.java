package com.trade.orderbook.model;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class OrderBookSideTest {

    @Parameters(name="{0}")
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { new OrderBookBuySide() },
                { new OrderBookSellSide() } });
    }

    private final OrderBookSide orderBookSide;

    public OrderBookSideTest(OrderBookSide orderBookSide) {
        this.orderBookSide = orderBookSide;
    }

    @After
    public void tearDown() {
        orderBookSide.clear();
    }

    @Test
    public void hasOrderId_returnsTrueIfContainsOrderId() {
        Order order = new Order(1, "Gold", orderBookSide.getSide(), 250, 1);
        orderBookSide.addOrder(order);

        assertThat(orderBookSide.hasOrder(order.getOrderId())).isTrue();
    }

    @Test
    public void hasOrderId_returnsFalseIfDoesNotContainsOrderId() {
        assertThat(orderBookSide.hasOrder(1)).isFalse();
    }

    @Test
    public void addOrder_addsOrderToCorrectPriceLevel() {
        Order expectedOrder = new Order(1, "Gold", orderBookSide.getSide(), 250, 1);

        orderBookSide.addOrder(expectedOrder);

        OrderNodes orderNodes = orderBookSide.getOrdersAtPriceLevel(250L);
        assertThat(orderNodes.get(0)).isEqualTo(expectedOrder);
    }

    @Test(expected = NullPointerException.class)
    public void addOrder_throwsIfNullOrder() {
        orderBookSide.addOrder(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addOrder_throwsIfOrderSideIncompatible() {
        Side side;
        if (orderBookSide.getSide().equals(Side.BUY)) {
            side = Side.SELL;
        } else {
            side = Side.BUY;
        }
        orderBookSide.addOrder(new Order(1, "Gold", side, 250, 1));
    }

    @Test
    public void removeOrder_removesOrderFromPriceLevel() {
        orderBookSide.addOrder(new Order(1, "Gold", orderBookSide.getSide(), 250, 1));

        assertThat(orderBookSide.removeOrder(1)).isTrue();
        assertThat(orderBookSide.getOrdersAtPriceLevel(250L)).isNull();
    }

    @Test
    public void removeOrder_returnsFalseIfOrderDoesNotExist() {
        assertThat(orderBookSide.removeOrder(1)).isFalse();
    }
}
