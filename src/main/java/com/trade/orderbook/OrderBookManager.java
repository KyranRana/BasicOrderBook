package com.trade.orderbook;

import com.trade.orderbook.model.Order;
import com.trade.orderbook.model.OrderNodes;
import com.trade.orderbook.model.Side;

import java.util.Optional;

/** All functions in this class should throw if given null parameters */
public interface OrderBookManager {

  /**
   * Add new order
   *
   * <p>Orders for the same instrument, on the same side, with the same price should be kept in the
   * order as they arrive
   *
   * Time complexity:
   * - O(log N) - given new price level is introduced
   * - O(1)     - given existing price level
   *
   * @param order new order to add <br>
   * @see Order
   */
  void addOrder(Order order);

  /**
   * Delete an existing order. Returns false if no such order exists
   *
   * Time complexity:
   * - O(log N) - given price level is removed
   * - O(1)     - given price level is not removed
   *
   * @param orderId unique identifier of existing order
   * @return True if the order was successfully deleted, false otherwise
   */
  boolean deleteOrder(long orderId);

  /**
   * Get the best price for the instrument and side.
   *
   * <p>For buy orders - the highest price For sell orders - the lowest price
   *
   * Time complexity: O(1)
   *
   * @param instrument identifier of an instrument
   * @param side either buy or sell
   * @return the best price, or Optional.empty() if there're no orders for the instrument on this
   *     side
   */
  Optional<Long> getBestPrice(String instrument, Side side);

  /**
   * Get all orders for the instrument on given side with given price
   *
   * <p>Result should contain orders in the same order as they arrive
   *
   * Time complexity: O(1)
   *
   * @param instrument identifier of an instrument
   * @param side either buy or sell
   * @param price requested price level
   * @return all orders, or empty list if there are no orders for the instrument on this side with
   *     this price
   */
  OrderNodes getOrdersAtLevel(String instrument, Side side, long price);
}
