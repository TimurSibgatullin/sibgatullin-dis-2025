package org.example.spring_context.component;


import org.example.spring_context.model.Base;
import org.example.spring_context.model.Fruit;
import org.example.spring_context.model.FruitType;
import org.example.spring_context.model.Store;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StoreService {
    private Base base = new Base();

    public StoreService() {
    }

    public void add(String name) {
        base.getStores().add(new Store(name));
    }

    public void addFruit(Store store, Fruit fruit, Integer count) {
        store.getFruits().put(fruit, count);
    }

    public List<Store> getAll() {
        return base.getStores();
    }

    public Store findByName(String name) {
        return base.getStores().stream()
                .filter(store -> store.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public Long getCountFruitsByStore(String name, FruitType type) {
        return base.getStores().stream()
                .filter(store -> store.getName().equals(name))
                .findFirst()
                .orElseThrow()
                .getFruits()
                .entrySet()
                .stream().filter(e -> e.getKey().getType() == type)
                .count()
                ;
    }
}
