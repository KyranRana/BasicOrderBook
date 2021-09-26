package com.trade.orderbook;

import com.trade.orderbook.model.Order;
import com.trade.orderbook.model.OrderBookBuySide;
import com.trade.orderbook.model.OrderBookSellSide;
import com.trade.orderbook.model.OrderBookSide;
import com.trade.orderbook.model.OrderNodes;
import com.trade.orderbook.model.Side;

import java.util.Optional;

public class SingleInstrumentOrderBook implements OrderBook {

    private final OrderBookSide buyOrders = new OrderBookBuySide();
    private final OrderBookSide sellOrders = new OrderBookSellSide();

    /**
     * Time complexity:
     * O(log N) - given new price level is introduced
     * O(1)     - given existing price level
     */
    @Override
    public void addOrder(Order order) {
        performOrderMatching(order);

        OrderBookSide bookSide = order.getSide().equals(Side.BUY) ?
                buyOrders : sellOrders;

        if (order.getQuantity() == 0) {
            bookSide.removeOrder(order.getOrderId());
            return;
        }
        bookSide.addOrder(order);
    }

    /**
     * Time complexity:
     * O(log N) - given price level is removed
     * O(1)     - given price level is not removed
     */
    @Override
    public boolean removeOrder(long orderId) {
        if (buyOrders.hasOrder(orderId)) {
            return buyOrders.removeOrder(orderId);
        } else {
            return sellOrders.removeOrder(orderId);
        }
    }

    /**
     * Time complexity: O(1)
     */
    @Override
    public Optional<Long> getBestPrice(Side side) {
        return side.equals(Side.BUY) ?
                buyOrders.getBestPrice() :
                sellOrders.getBestPrice();
    }

    /**
     * Time complexity: O(1)
     */
    @Override
    public OrderNodes getOrdersAtLevel(Side side, long priceLevel) {
        return side.equals(Side.BUY) ?
                buyOrders.getOrdersAtPriceLevel(priceLevel) :
                sellOrders.getOrdersAtPriceLevel(priceLevel);
    }

    private void performOrderMatching(Order order) {
        OrderBookSide otherSide = order.getSide().equals(Side.BUY) ?
                sellOrders : buyOrders;

        OrderNodes matchingOrders = otherSide.getOrdersAtPriceLevel(order.getPrice());
        if (matchingOrders != null) {
            while (order.getQuantity() > 0) {
                Order matchingOrder = matchingOrders.firstOrder();
                if (matchingOrder == null) {
                    break;
                }
                order.matchWith(matchingOrder);
                if (matchingOrder.getQuantity() == 0) {
                    otherSide.removeOrder(matchingOrder.getOrderId());
                }
            }
        }
    }
}
