package ru.itis.dis403.lab2_6.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.sql.Date;

@Entity
@Getter
public class BookingPersonView {

    @Id
    private long id;

    @Temporal(TemporalType.DATE)
    private Date arrivalDate;

    @Temporal(TemporalType.DATE)
    private Date stayingDate;

    private String name;
    private String gender;
    private String room;

    @Column(name = "hotel_id")
    private long holedId;

    @Temporal(TemporalType.DATE)
    private Date birthdate;


}
