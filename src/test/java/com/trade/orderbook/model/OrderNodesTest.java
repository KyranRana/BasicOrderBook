package com.trade.orderbook.model;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderNodesTest {

    @Test
    public void add_addsOrder() {
        OrderNodes orderNodes = new OrderNodes();
        orderNodes.add(new Order(1, "Gold", Side.BUY, 250, 1));
        assertThat(orderNodes.size()).isEqualTo(1);
    }

    @Test(expected = NullPointerException.class)
    public void add_throwsIfNullOrder() {
        OrderNodes orderNodes = new OrderNodes();
        orderNodes.add(null);
    }

    @Test
    public void get_getOrderNodes() {
        Order firstOrder = new Order(1, "Gold", Side.BUY, 250, 1);
        Order secondOrder = new Order(2, "Gold", Side.BUY, 250, 1);

        OrderNodes orderNodes = new OrderNodes();
        orderNodes.add(firstOrder);
        orderNodes.add(secondOrder);

        assertThat(orderNodes.get(0)).isEqualTo(firstOrder);
        assertThat(orderNodes.get(1)).isEqualTo(secondOrder);
    }

    @Test
    public void get_returnsNullIfNonExistent() {
        OrderNodes orderNodes = new OrderNodes();
        assertThat(orderNodes.get(0)).isNull();
    }

    @Test
    public void remove_removesFirstOrder() {
        Order firstOrder = new Order(1, "Gold", Side.BUY, 250, 1);
        Order lastOrder = new Order(2, "Gold", Side.BUY, 250, 1);

        OrderNodes orderNodes = new OrderNodes();
        orderNodes.add(firstOrder);
        orderNodes.add(lastOrder);

        assertThat(orderNodes.remove(firstOrder)).isTrue();
        assertThat(orderNodes.size()).isEqualTo(1);
        assertThat(orderNodes.get(0)).isEqualTo(lastOrder);
    }

    @Test
    public void remove_removesLastOrder() {
        Order firstOrder = new Order(1, "Gold", Side.BUY, 250, 1);
        Order lastOrder = new Order(2, "Gold", Side.BUY, 250, 1);

        OrderNodes orderNodes = new OrderNodes();
        orderNodes.add(firstOrder);
        orderNodes.add(lastOrder);

        assertThat(orderNodes.remove(lastOrder)).isTrue();
        assertThat(orderNodes.size()).isEqualTo(1);
        assertThat(orderNodes.get(0)).isEqualTo(firstOrder);
    }

    @Test
    public void remove_removesOrderInMiddle() {
        Order firstOrder = new Order(1, "Gold", Side.BUY, 250, 1);
        Order secondOrder = new Order(2, "Gold", Side.BUY, 250, 1);
        Order thirdOrder = new Order(3, "Gold", Side.BUY, 250, 1);

        OrderNodes orderNodes = new OrderNodes();
        orderNodes.add(firstOrder);
        orderNodes.add(secondOrder);
        orderNodes.add(thirdOrder);

        assertThat(orderNodes.remove(secondOrder)).isTrue();
        assertThat(orderNodes.size()).isEqualTo(2);
        assertThat(orderNodes.get(0)).isEqualTo(firstOrder);
        assertThat(orderNodes.get(1)).isEqualTo(thirdOrder);
    }

    @Test
    public void remove_removesOnlyOrder() {
        Order onlyOrder = new Order(1, "Gold", Side.BUY, 250, 1);

        OrderNodes orderNodes = new OrderNodes();
        orderNodes.add(onlyOrder);

        assertThat(orderNodes.remove(onlyOrder)).isTrue();
        assertThat(orderNodes.size()).isEqualTo(0);
    }

    @Test(expected = NullPointerException.class)
    public void remove_throwsIfNullOrder() {
        OrderNodes orderNodes = new OrderNodes();
        orderNodes.remove(null);
    }
}
