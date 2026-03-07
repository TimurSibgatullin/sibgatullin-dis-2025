package org.example.orm.model;

import org.example.orm.annotation.Column;
import org.example.orm.annotation.Entity;
import org.example.orm.annotation.Id;

@Entity
public class Country {
    @Id
    Integer id;
    @Column
    String name;

    public Country() {
    }

    public Country(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
