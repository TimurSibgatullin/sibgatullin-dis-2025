package ru.itis.dis403.lab2_6.dto;

import ru.itis.dis403.lab2_6.model.Booking;
import ru.itis.dis403.lab2_6.model.BookingPersonView;

import java.util.List;

public class BookingViewResponse {
    private List<BookingPersonViewDto> bookings;

    public BookingViewResponse(List<BookingPersonViewDto> bookings) {
        this.bookings = bookings;
    }

    public List<BookingPersonViewDto> getBookings() {
        return bookings;
    }

    public void setBookings(List<BookingPersonViewDto> bookings) {
        this.bookings = bookings;
    }
}