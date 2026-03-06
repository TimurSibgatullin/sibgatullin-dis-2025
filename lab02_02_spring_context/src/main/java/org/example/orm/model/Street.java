package org.example.orm.model;

import org.example.orm.annotation.Column;
import org.example.orm.annotation.Entity;
import org.example.orm.annotation.Id;
import org.example.orm.annotation.ManyToOne;

@Entity
public class Street {
    @Id
    Long id;
    @Column
    String name;
    @ManyToOne
    City city;

    public Street() {
    }

    public Street(String name, City city) {
        this.name = name;
        this.city = city;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
}
