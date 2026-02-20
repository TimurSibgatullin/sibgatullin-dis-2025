package org.example.component;

import org.example.annotition.Component;
import org.example.model.Fruit;
import org.example.model.FruitType;
import org.example.model.Store;


@Component
public class Application {

//    @Inject
    private StoreService service;

    public Application(StoreService service) {
        this.service = service;
    }

    public void run() {
        service.add("I");
        service.add("II");

        Store storeI = service.findByName("I");
        service.addFruit(storeI, new Fruit("Apples", FruitType.APPLE), 1000);
        service.addFruit(storeI, new Fruit("Babanas", FruitType.BANANA), 2000);

        Store storeII = service.findByName("II");
        service.addFruit(storeI, new Fruit("Oranges", FruitType.ORANGE), 500);
        service.addFruit(storeI, new Fruit("Babanas", FruitType.BANANA), 2345);

        service.getAll().forEach(System.out::println);
    }
}
