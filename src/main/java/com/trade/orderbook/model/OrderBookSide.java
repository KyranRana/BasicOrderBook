package com.trade.orderbook.model;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.TreeSet;

public abstract class OrderBookSide {

  protected final Map<Long, Order> orders;
  protected final Map<Long, OrderNodes> priceLevels;
  protected final TreeSet<Long> orderedPrices;

  public OrderBookSide() {
    this.orders = new HashMap<>();
    this.priceLevels = new HashMap<>();
    this.orderedPrices = new TreeSet<>();
  }

  /**
   * Time complexity: O(1)
   */
  public boolean hasOrder(long orderId) {
    return orders.containsKey(orderId);
  }

  /**
   * Time complexity:
   *
   * <ul>
   *   <li>O(log N) - given new price level is introduced</li>
   *   <li>O(1) - given existing price level</li>
   * </ul>
   */
  public void addOrder(Order order) {
    validateOrder(order);
    addOrderToPriceLevel(order);
    orders.put(order.getOrderId(), order);
  }

  /**
   * Time complexity:
   *
   * <ul>
   *   <li>O(log N) - given price level is removed</li>
   *   <li>O(1) - given price level is not removed</li>
   * </ul>
   */
  public boolean removeOrder(long orderId) {
    if (!hasOrder(orderId)) {
      return false;
    }
    Order order = orders.get(orderId);
    removeOrderFromPriceLevel(order);
    orders.remove(orderId);
    return true;
  }

  /**
   * Time complexity: O(1)
   */
  public OrderNodes getOrdersAtPriceLevel(long priceLevel) {
    if (priceLevels.containsKey(priceLevel)) {
      return priceLevels.get(priceLevel);
    }
    return null;
  }

  /**
   * Time complexity: O(1)
   */
  public abstract Optional<Long> getBestPrice();

  public abstract Side getSide();

  private void addOrderToPriceLevel(Order order) {
    OrderNodes priceLevelOrders =
        priceLevels.computeIfAbsent(order.getPrice(), k -> new OrderNodes());

    if (priceLevelOrders.size() == 0) {
      orderedPrices.add(order.getPrice());
    }

    priceLevelOrders.add(order);
  }

  private void removeOrderFromPriceLevel(Order order) {
    OrderNodes priceLevelOrders =
        priceLevels.computeIfAbsent(order.getPrice(), k -> new OrderNodes());

    priceLevelOrders.remove(order);

    if (priceLevelOrders.size() == 0) {
      priceLevels.remove(order.getPrice());
      orderedPrices.remove(order.getPrice());
    }
  }

  private void validateOrder(Order order) {
    requireNonNull(order);
    checkArgument(
        order.getSide().equals(getSide()), String.format("Order must be a %s order!", getSide()));
  }

  public void clear() {
    orders.clear();
    priceLevels.clear();
    orderedPrices.clear();
  }
}
