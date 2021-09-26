package com.trade.orderbook;

import static org.assertj.core.api.Assertions.assertThat;

import com.trade.orderbook.model.Order;
import com.trade.orderbook.model.OrderNodes;
import com.trade.orderbook.model.Side;
import org.junit.Test;

public class SingleInstrumentOrderBookIntegrationTest {

  @Test
  public void addOrder_createsOrderAndUpdatesBestBidPrice() {
    SingleInstrumentOrderBook orderBook = new SingleInstrumentOrderBook();
    orderBook.addOrder(new Order(1, "Gold", Side.BUY, 250, 1));
    orderBook.addOrder(new Order(2, "Gold", Side.BUY, 275, 1));
    assertThat(orderBook.getBestPrice(Side.BUY)).hasValue(275L);
  }

  @Test
  public void addOrder_createsOrderAndUpdatesBestAskPrice() {
    SingleInstrumentOrderBook orderBook = new SingleInstrumentOrderBook();
    orderBook.addOrder(new Order(1, "Gold", Side.SELL, 250, 1));
    orderBook.addOrder(new Order(2, "Gold", Side.SELL, 275, 1));
    assertThat(orderBook.getBestPrice(Side.SELL)).hasValue(250L);
  }

  @Test
  public void getOrdersAtLevel_returnsBuyOrdersAtPriceLevel() {
    Order firstOrder = new Order(1, "Gold", Side.BUY, 255, 1);
    Order secondOrder = new Order(2, "Gold", Side.BUY, 255, 1);

    SingleInstrumentOrderBook orderBook = new SingleInstrumentOrderBook();
    orderBook.addOrder(firstOrder);
    orderBook.addOrder(secondOrder);

    OrderNodes orderNodes = orderBook.getOrdersAtLevel(Side.BUY, 255);
    assertThat(orderNodes.get(0)).isEqualTo(firstOrder);
    assertThat(orderNodes.get(1)).isEqualTo(secondOrder);
  }

  @Test
  public void getOrdersAtLevel_returnsSellOrdersAtPriceLevel() {
    Order firstOrder = new Order(1, "Gold", Side.SELL, 265, 1);
    Order secondOrder = new Order(2, "Gold", Side.SELL, 265, 1);

    SingleInstrumentOrderBook orderBook = new SingleInstrumentOrderBook();
    orderBook.addOrder(firstOrder);
    orderBook.addOrder(secondOrder);

    OrderNodes orderNodes = orderBook.getOrdersAtLevel(Side.SELL, 265);
    assertThat(orderNodes.get(0)).isEqualTo(firstOrder);
    assertThat(orderNodes.get(1)).isEqualTo(secondOrder);
  }

  @Test
  public void removeOrder_removesOrderAndUpdatesBestBidPrice() {
    SingleInstrumentOrderBook orderBook = new SingleInstrumentOrderBook();
    orderBook.addOrder(new Order(1, "Gold", Side.BUY, 250, 1));
    orderBook.addOrder(new Order(2, "Gold", Side.BUY, 250, 1));
    orderBook.addOrder(new Order(3, "Gold", Side.BUY, 255, 1));
    orderBook.addOrder(new Order(4, "Gold", Side.BUY, 275, 1));
    orderBook.addOrder(new Order(5, "Gold", Side.BUY, 275, 1));

    assertThat(orderBook.removeOrder(4)).isTrue();
    assertThat(orderBook.getBestPrice(Side.BUY)).hasValue(275L);

    assertThat(orderBook.removeOrder(5)).isTrue();
    assertThat(orderBook.getBestPrice(Side.BUY)).hasValue(255L);
  }

  @Test
  public void removeOrder_removesOrderAndUpdatesBestAskPrice() {
    SingleInstrumentOrderBook orderBook = new SingleInstrumentOrderBook();
    orderBook.addOrder(new Order(1, "Gold", Side.SELL, 250, 1));
    orderBook.addOrder(new Order(2, "Gold", Side.SELL, 250, 1));
    orderBook.addOrder(new Order(3, "Gold", Side.SELL, 255, 1));
    orderBook.addOrder(new Order(4, "Gold", Side.SELL, 275, 1));
    orderBook.addOrder(new Order(5, "Gold", Side.SELL, 275, 1));

    assertThat(orderBook.removeOrder(1)).isTrue();
    assertThat(orderBook.getBestPrice(Side.SELL)).hasValue(250L);

    assertThat(orderBook.removeOrder(2)).isTrue();
    assertThat(orderBook.getBestPrice(Side.SELL)).hasValue(255L);
  }

  @Test
  public void removeOrder_returnsFalseIfOrderNotFound() {
    SingleInstrumentOrderBook orderBook = new SingleInstrumentOrderBook();
    assertThat(orderBook.removeOrder(1)).isFalse();
  }
}
