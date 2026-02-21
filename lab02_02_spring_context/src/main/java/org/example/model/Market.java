package org.example.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Market {
    private Map<Product, Integer> products = new HashMap<>();
    private List<Order> orders = new ArrayList<>();
    private List<ImportProduct> importProducts = new ArrayList<>();

    public Map<Product, Integer> getProducts() {
        return products;
    }

    public void setProducts(Map<Product, Integer> products) {
        this.products = products;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public List<ImportProduct> getImportProducts() {
        return importProducts;
    }

    public void setImportProducts(List<ImportProduct> importProducts) {
        this.importProducts = importProducts;
    }
}
