package com.trade.orderbook.model;


import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;

public class OrderNodes {

    private final Map<Order, OrderNode> index;

    private OrderNode head = null;
    private OrderNode tail = null;

    private int size = 0;

    public OrderNodes() {
        this.index = new HashMap<>();
    }

    public void add(Order order) {
        requireNonNull(order);
        addNode(new OrderNode(order));
    }

    public Order get(int index) {
        OrderNode node = getNode(index);
        if (node != null) {
            return node.getOrder();
        }
        return null;
    }

    public boolean remove(Order order) {
        requireNonNull(order);
        if (!index.containsKey(order)) {
             return false;
        }
        removeNode(index.get(order));
        return true;
    }

    public int size() {
        return size;
    }

    public Order firstOrder() {
        if (head == null) {
            return null;
        }
        return head.getOrder();
    }

    private void addNode(OrderNode node) {
        if (head == null) {
            head = node;
            tail = node;
        } else {
            OrderNode lastTail = tail;
            tail = node;
            lastTail.setNext(tail);
            tail.setPrev(lastTail);
        }
        index.put(node.getOrder(), node);
        size++;
    }

    private OrderNode getNode(int index) {
        OrderNode ptr = head;
        if (ptr == null) {
            return null;
        }
        int currentIndex = 0;
        while (ptr.getNext() != null) {
            if (currentIndex == index) {
                return ptr;
            }
            ptr = ptr.getNext();
            currentIndex++;
        }
        if (currentIndex == index) {
            return ptr;
        }
        return null;
    }

    private void removeNode(OrderNode node) {
        requireNonNull(node);
        if (node.getPrev() == null && node.getNext() == null) {
            head = null;
            tail = null;
        } else if (node.getPrev() == null) {
            head = node.getNext();
            head.setPrev(null);
        } else if (node.getNext() == null) {
            tail = node.getPrev();
            tail.setNext(null);
        } else {
            OrderNode tmpNext = node.getNext();

            OrderNode prev = node.getPrev();
            prev.setNext(tmpNext);
            tmpNext.setPrev(prev);
        }
        index.remove(node.getOrder());
        size--;
    }
}
