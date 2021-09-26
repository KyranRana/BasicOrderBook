package com.trade.orderbook.model;

import java.util.Optional;

public class OrderBookBuySide extends OrderBookSide {

    @Override
    public Optional<Long> getBestPrice() {
        return Optional.ofNullable(orderedPrices.last());
    }

    @Override
    public Side getSide() {
        return Side.BUY;
    }
}
