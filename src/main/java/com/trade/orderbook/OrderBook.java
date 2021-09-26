package com.trade.orderbook;

import com.trade.orderbook.model.Order;
import com.trade.orderbook.model.OrderNodes;
import com.trade.orderbook.model.Side;

import java.util.Optional;

public interface OrderBook {

    void addOrder(Order order);

    boolean removeOrder(long orderId);

    Optional<Long> getBestPrice(Side side);

    OrderNodes getOrdersAtLevel(Side side, long priceLevel);
}
