package org.example.spring.component;

import org.example.spring.model.ImportProduct;
import org.example.spring.model.Market;
import org.example.spring.model.Order;

import java.util.NoSuchElementException;


public class MarketService {
    private Market market;

    public MarketService(Market market) {
        this.market = market;
    }

    public void doOrder(Order order) {
        Integer count = market.getProducts().get(order.getProduct());
        if (count < order.getCount() || count == null) {
            throw new NoSuchElementException("not found products");
        }
        market.getOrders().add(order);
        market.getProducts().put(order.getProduct(), count - order.getCount());
    }

    public void doImport(ImportProduct importProduct) {
        Integer count = market.getProducts().get(importProduct.getProduct());
        if (count == null) {
            count = 0;
        }
        market.getProducts().put(importProduct.getProduct(), count + importProduct.getCount());
        market.getImportProducts().add(importProduct);
    }

    public void printProducts() {
        market.getProducts().entrySet().forEach(System.out::println);
    }
}
