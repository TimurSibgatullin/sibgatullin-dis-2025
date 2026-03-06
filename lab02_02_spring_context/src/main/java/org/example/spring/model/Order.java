package org.example.spring.model;

public class Order {
    private Product product;
    private Integer count;
    private String client;

    public Order(Product product, Integer count, String client) {
        this.product = product;
        this.count = count;
        this.client = client;
    }

    public Order() {
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }
}
