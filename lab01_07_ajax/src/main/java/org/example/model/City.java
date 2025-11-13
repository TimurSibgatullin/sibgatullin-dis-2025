package org.example.model;

public class City {
    private long id;
    private String name;
    private String comment;

    public City(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public City(long id, String name, String comment) {
        this.id = id;
        this.name = name;
        this.comment = comment;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
