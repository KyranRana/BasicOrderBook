package com.trade.orderbook.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class OrderBookSellSideTest {

  @Test
  public void addOrder_updatesBestPrice() {
    OrderBookSellSide orderBookSellSide = new OrderBookSellSide();
    orderBookSellSide.addOrder(new Order(1, "Gold", Side.SELL, 250, 1));
    orderBookSellSide.addOrder(new Order(2, "Gold", Side.SELL, 265, 1));
    assertThat(orderBookSellSide.getBestPrice()).hasValue(250L);
  }

  @Test
  public void removeOrder_updatesBestPrice() {
    OrderBookSellSide orderBookSellSide = new OrderBookSellSide();
    orderBookSellSide.addOrder(new Order(1, "Gold", Side.SELL, 250, 1));
    orderBookSellSide.addOrder(new Order(2, "Gold", Side.SELL, 250, 1));
    orderBookSellSide.addOrder(new Order(3, "Gold", Side.SELL, 275, 1));
    orderBookSellSide.addOrder(new Order(4, "Gold", Side.SELL, 275, 1));
    orderBookSellSide.removeOrder(1);
    assertThat(orderBookSellSide.getBestPrice()).hasValue(250L);
    orderBookSellSide.removeOrder(2);
    assertThat(orderBookSellSide.getBestPrice()).hasValue(275L);
  }
}
