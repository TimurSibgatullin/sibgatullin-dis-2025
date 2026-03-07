package org.example.spring_context.component;


import org.example.spring_context.model.Fruit;
import org.example.spring_context.model.FruitType;
import org.example.spring_context.model.Store;
import org.example.spring_context.model.Category;
import org.example.spring_context.model.Order;
import org.example.spring_context.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.math.BigDecimal;

@Component("App")
public class Application {
    @Autowired
    private MarketService marketService;

    @Autowired
    private StoreService storeService;

    public Application() {
    }

    public void run() {
        try {
            marketService.doOrder(new Order(
                    new Product(
                    "Laptop",
                    "B001",
                    Category.LAPTOP,
                    new BigDecimal("1200.00")),
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
