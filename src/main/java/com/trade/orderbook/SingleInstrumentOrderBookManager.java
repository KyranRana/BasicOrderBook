package com.trade.orderbook;

import com.trade.orderbook.model.Order;
import com.trade.orderbook.model.OrderNodes;
import com.trade.orderbook.model.Side;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

public class SingleInstrumentOrderBookManager implements OrderBookManager {

    private final Map<String, OrderBook> orderBooks = new HashMap<>();
    private final Map<Long, String> orderIdToInstrument = new HashMap<>();

    @Override
    public void addOrder(Order order) {
        requireNonNull(order);

        OrderBook orderBook = orderBooks
                .computeIfAbsent(order.getInstrument(), k -> new SingleInstrumentOrderBook());

        orderBook.addOrder(order);

        orderIdToInstrument.put(order.getOrderId(), order.getInstrument());
    }

    @Override
    public boolean deleteOrder(long orderId) {
        String instrument = orderIdToInstrument.get(orderId);
        if (!orderBooks.containsKey(instrument)) {
            return false;
        }
        OrderBook orderBook = orderBooks.get(instrument);
        return orderBook.removeOrder(orderId);
    }

    @Override
    public Optional<Long> getBestPrice(String instrument, Side side) {
        if (orderBooks.containsKey(instrument)) {
            OrderBook orderBook = orderBooks.get(instrument);
            return orderBook.getBestPrice(side);
        }
        return Optional.empty();
    }

    @Override
    public OrderNodes getOrdersAtLevel(String instrument, Side side, long price) {
        OrderBook orderBook = orderBooks.get(instrument);
        return orderBook.getOrdersAtLevel(side, price);
    }
}