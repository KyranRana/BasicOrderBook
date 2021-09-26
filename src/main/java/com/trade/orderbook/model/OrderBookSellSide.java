package com.trade.orderbook.model;

import java.util.Optional;

public class OrderBookSellSide extends OrderBookSide {

  @Override
  public Optional<Long> getBestPrice() {
    return Optional.ofNullable(orderedPrices.first());
  }

  @Override
  public Side getSide() {
    return Side.SELL;
  }
}
