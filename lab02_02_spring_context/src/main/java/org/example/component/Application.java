package org.example.component;

import org.example.component.MarketService;
import org.example.model.Category;
import org.example.model.Order;
import org.example.model.Product;
import org.springframework.stereotype.Component;


import java.math.BigDecimal;

@Component("App")
public class Application {
    private final MarketService service;

    public Application(MarketService service) {
        this.service = service;
    }

    public void run() {
        try {
            service.doOrder(new Order(
                    new Product("Personal Computer", "0001", Category.PC, BigDecimal.TWO),
                    10, "Client 1"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
