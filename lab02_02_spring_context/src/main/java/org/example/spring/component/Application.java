package org.example.spring.component;

import org.example.model.Fruit;
import org.example.model.FruitType;
import org.example.model.Store;
import org.example.spring.model.Category;
import org.example.spring.model.Order;
import org.example.spring.model.Product;
import org.springframework.stereotype.Component;


import java.math.BigDecimal;

@Component("App")
public class Application {
    private final MarketService marketService;
    private final StoreService storeService;

    public Application(MarketService marketService, StoreService storeService) {
        this.marketService = marketService;
        this.storeService = storeService;
    }

    public void run() {
        try {
            marketService.doOrder(new Order(
                    new Product("Personal Computer", "0001", Category.PC, BigDecimal.TWO),
                    10, "Client 1"));

            storeService.add("I");
            storeService.add("II");

            Store storeI = storeService.findByName("I");
            storeService.addFruit(storeI, new Fruit("Apples", FruitType.APPLE), 1000);
            storeService.addFruit(storeI, new Fruit("Babanas", FruitType.BANANA), 2000);

            Store storeII = storeService.findByName("II");
            storeService.addFruit(storeI, new Fruit("Oranges", FruitType.ORANGE), 500);
            storeService.addFruit(storeI, new Fruit("Babanas", FruitType.BANANA), 2345);

            storeService.getAll().forEach(System.out::println);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
