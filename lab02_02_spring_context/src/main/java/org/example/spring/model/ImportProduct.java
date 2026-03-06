package org.example.spring.model;

public class ImportProduct {
    private Product product;
    private Integer count;
    private String client;
    private String supplier;

    public ImportProduct(Product product, Integer count, String client, String supplier) {
        this.product = product;
        this.count = count;
        this.client = client;
        this.supplier = supplier;
    }

    public ImportProduct() {
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

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }
}
