package org.example.spring.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Product {
    private String name;
    private String article;
    private Category category;
    private BigDecimal price;


    public Product(String name, String article, Category category, BigDecimal price) {
        this.name = name;
        this.article = article;
        this.category = category;
        this.price = price;
    }

    public Product() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(article, product.article);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(article);
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", article='" + article + '\'' +
                ", category=" + category +
                ", price=" + price +
                '}';
    }
}
