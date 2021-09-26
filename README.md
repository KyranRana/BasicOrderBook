# Order Book Manager

## Focus:
- Singlethreaded implementation
- Fixed price order matching
- Buying and selling a single instrument

---

##SingleInstrumentOrderBook

### Add Order:
1. Validation
    - Check order is not null
    - Check order does not exist
    - Check order matches instrument of order book
2. Order matching
    - Check order with same price level on the opposite side.
        - If no, continue to step 3
        - if yes, match on earliest orders at price level until order partially / fully satisfied
            - For each order fully satisfied
                - Remove order from order book
                - Evaluate best bid and ask price
            - If order is partially satisfied, continue to step 3
3. Store order against price level

### Remove order:
1. Validation
    - Check order exists
    - Check order matches instrument of order book
2. Remove order
3. Evaluate best bid and ask price

### Get best price
- if side is BUY, return highest bid price
- if side is SELL, return lowest ask price

### Get orders at level:
- given price, return orders for price level ordered by how they arrive
