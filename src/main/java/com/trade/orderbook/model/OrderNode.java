package com.trade.orderbook.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderNode {
    private Order order;
    private OrderNode prev;
    private OrderNode next;

    public OrderNode(Order order) {
        this.order = order;
    }
}
