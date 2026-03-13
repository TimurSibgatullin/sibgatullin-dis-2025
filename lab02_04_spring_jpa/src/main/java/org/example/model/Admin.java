package org.example.model;
import jakarta.persistence.Entity;

//@Getter@Setter
@Entity
public class Admin extends Person {

    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

