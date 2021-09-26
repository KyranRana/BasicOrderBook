package com.trade.orderbook.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@Setter
@ToString
public class Order {

  /** unique identifier for the order */
  @EqualsAndHashCode.Include
  private long orderId;

  /** identifier of an instrument */
  private String instrument;

  /** either buy or sell */
  private Side side;

  /** limit price for the order, always positive */
  private long price;

  /** required quantity, always positive */
  private long quantity;

  /**
   * All-values ctor
   *
   * @param orderId unique identifier for the order
   * @param instrument identifier of an instrument
   * @param side either buy or sell
   * @param price limit price for the order, always positive
   * @param quantity required quantity, always positive
   */
  public Order(int orderId, String instrument, Side side, long price, long quantity) {
    requireNonNull(instrument);
    checkArgument(price > 0, "price must be positive");
    checkArgument(quantity > 0, "quantity must be positive");
    this.orderId = orderId;
    this.instrument = instrument;
    this.side = side;
    this.price = price;
    this.quantity = quantity;
  }

  public void setInstrument(String instrument) {
    requireNonNull(instrument);
    this.instrument = instrument;
  }

  public void setPrice(long price) {
    checkArgument(price > 0, "price must be positive");
    this.price = price;
  }

  public void setQuantity(long quantity) {
    checkArgument(quantity >= 0, "quantity must be positive");
    this.quantity = quantity;
  }

  public void matchWith(Order order) {
    if (getQuantity() >= order.getQuantity()) {
      setQuantity(getQuantity() - order.getQuantity());
      order.setQuantity(0);
    } else {
      order.setQuantity(order.getQuantity() - getQuantity());
      setQuantity(0);
    }
  }
}
