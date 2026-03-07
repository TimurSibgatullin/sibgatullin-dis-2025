package org.example.spring_context.component;

import org.example.spring_context.model.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.NoSuchElementException;


@Component
public class MarketService {
    private Market market = new Market();

    public MarketService() {
        Product laptop = new Product(
                "Laptop",
                "B001",
                Category.LAPTOP,
                new BigDecimal("1200.00")
        );
        market.getProducts().put(laptop, 100);
    }

    public void doOrder(Order order) {
        Integer count = market.getProducts().get(order.getProduct());
        if (count == null || count < order.getCount()) {
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
