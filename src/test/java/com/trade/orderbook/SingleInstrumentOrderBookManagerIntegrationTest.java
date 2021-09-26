package com.trade.orderbook;

import com.trade.orderbook.model.Order;
import com.trade.orderbook.model.OrderNodes;
import com.trade.orderbook.model.Side;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SingleInstrumentOrderBookManagerIntegrationTest {

    @Test
    public void givenBuyAndSellOrders_whenOrdersMatchExactly_thenOrdersAreSatisfiedAndRemoved() {
        SingleInstrumentOrderBookManager orderBookManager = new SingleInstrumentOrderBookManager();
        orderBookManager.addOrder(new Order(1, "Gold", Side.BUY, 250, 2));
        orderBookManager.addOrder(new Order(2, "Gold", Side.BUY, 250, 2));
        orderBookManager.addOrder(new Order(3, "Gold", Side.BUY, 245, 2));
        orderBookManager.addOrder(new Order(4, "Gold", Side.SELL, 250, 2));
        orderBookManager.addOrder(new Order(5, "Gold", Side.SELL, 250, 2));

        assertThat(orderBookManager.getBestPrice("Gold", Side.BUY)).hasValue(245L);
        Assertions.assertThat(orderBookManager.getOrdersAtLevel("Gold", Side.BUY, 250)).isNull();
        Assertions.assertThat(orderBookManager.getOrdersAtLevel("Gold", Side.SELL, 250)).isNull();
    }

    @Test
    public void givenBuyAndSellOrders_whenOrdersMatchPartially_thenOrdersArePartiallySatisfied() {
        Order unsatisfiedOrder = new Order(2, "Gold", Side.BUY, 250, 3);

        SingleInstrumentOrderBookManager orderBookManager = new SingleInstrumentOrderBookManager();
        orderBookManager.addOrder(new Order(1, "Gold", Side.BUY, 250, 2));
        orderBookManager.addOrder(unsatisfiedOrder);
        orderBookManager.addOrder(new Order(3, "Gold", Side.BUY, 245, 2));
        orderBookManager.addOrder(new Order(4, "Gold", Side.SELL, 250, 2));
        orderBookManager.addOrder(new Order(5, "Gold", Side.SELL, 250, 2));

        OrderNodes priceLevelOrders = orderBookManager.getOrdersAtLevel("Gold", Side.BUY, 250);

        assertThat(orderBookManager.getBestPrice("Gold", Side.BUY)).hasValue(250L);
        assertThat(priceLevelOrders.get(0)).isEqualTo(unsatisfiedOrder);
        assertThat(unsatisfiedOrder.getQuantity()).isEqualTo(1);
    }

    @Test
    public void addOrder_updatesBestBidPriceForMultipleInstruments() {
        SingleInstrumentOrderBookManager orderBookManager = new SingleInstrumentOrderBookManager();
        orderBookManager.addOrder(new Order(1, "Gold", Side.BUY, 250, 1));
        orderBookManager.addOrder(new Order(2, "Gold", Side.BUY, 265, 1));
        assertThat(orderBookManager.getBestPrice("Gold", Side.BUY)).hasValue(265L);

        orderBookManager.addOrder(new Order(3, "Silver", Side.BUY, 240, 1));
        orderBookManager.addOrder(new Order(4, "Silver", Side.BUY, 255, 1));
        assertThat(orderBookManager.getBestPrice("Silver", Side.BUY)).hasValue(255L);
    }

    @Test
    public void addOrder_updatesBestAskPriceForMultipleInstruments() {
        SingleInstrumentOrderBookManager orderBookManager = new SingleInstrumentOrderBookManager();
        orderBookManager.addOrder(new Order(1, "Gold", Side.SELL, 250, 1));
        orderBookManager.addOrder(new Order(2, "Gold", Side.SELL, 265, 1));
        assertThat(orderBookManager.getBestPrice("Gold", Side.SELL)).hasValue(250L);

        orderBookManager.addOrder(new Order(3, "Silver", Side.SELL, 240, 1));
        orderBookManager.addOrder(new Order(4, "Silver", Side.SELL, 255, 1));
        assertThat(orderBookManager.getBestPrice("Silver", Side.SELL)).hasValue(240L);
    }

    @Test
    public void addOrder_updatesOrdersAtPriceLevel() {
        Order firstOrder = new Order(1, "Gold", Side.BUY, 255, 1);
        Order secondOrder = new Order(2, "Gold", Side.BUY, 255, 1);

        SingleInstrumentOrderBookManager orderBookManager = new SingleInstrumentOrderBookManager();
        orderBookManager.addOrder(firstOrder);
        orderBookManager.addOrder(secondOrder);

        OrderNodes orderNodes = orderBookManager.getOrdersAtLevel("Gold", Side.BUY, 255);
        assertThat(orderNodes.get(0)).isEqualTo(firstOrder);
        assertThat(orderNodes.get(1)).isEqualTo(secondOrder);
    }

    @Test(expected = NullPointerException.class)
    public void addOrder_throwsIfNullOrder() {
        SingleInstrumentOrderBookManager orderBookManager = new SingleInstrumentOrderBookManager();
        orderBookManager.addOrder(null);
    }

    @Test
    public void deleteOrder_updatesBestBidPriceForMultipleInstruments() {
        SingleInstrumentOrderBookManager orderBookManager = new SingleInstrumentOrderBookManager();
        orderBookManager.addOrder(new Order(1, "Gold", Side.BUY, 250, 1));
        orderBookManager.addOrder(new Order(2, "Gold", Side.BUY, 265, 1));
        assertThat(orderBookManager.deleteOrder(2)).isTrue();
        assertThat(orderBookManager.getBestPrice("Gold", Side.BUY)).hasValue(250L);

        orderBookManager.addOrder(new Order(3, "Silver", Side.BUY, 240, 1));
        orderBookManager.addOrder(new Order(4, "Silver", Side.BUY, 255, 1));
        orderBookManager.deleteOrder(4);
        assertThat(orderBookManager.getBestPrice("Silver", Side.BUY)).hasValue(240L);
    }

    @Test
    public void deleteOrder_updatesBestAskPriceForMultipleInstruments() {
        SingleInstrumentOrderBookManager orderBookManager = new SingleInstrumentOrderBookManager();
        orderBookManager.addOrder(new Order(1, "Gold", Side.SELL, 250, 1));
        orderBookManager.addOrder(new Order(2, "Gold", Side.SELL, 265, 1));
        assertThat(orderBookManager.deleteOrder(1)).isTrue();
        assertThat(orderBookManager.getBestPrice("Gold", Side.SELL)).hasValue(265L);

        orderBookManager.addOrder(new Order(3, "Silver", Side.SELL, 240, 1));
        orderBookManager.addOrder(new Order(4, "Silver", Side.SELL, 255, 1));
        assertThat(orderBookManager.deleteOrder(3)).isTrue();
        assertThat(orderBookManager.getBestPrice("Silver", Side.SELL)).hasValue(255L);
    }
}
