package ru.itis.dis403.lab2_6.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import ru.itis.dis403.lab2_6.dto.AuthRequest;
import ru.itis.dis403.lab2_6.dto.AuthResponse;
import ru.itis.dis403.lab2_6.dto.BookingDto;
import ru.itis.dis403.lab2_6.dto.BookingsResponse;
import ru.itis.dis403.lab2_6.model.Booking;
import ru.itis.dis403.lab2_6.repository.BookingRepository;
import ru.itis.dis403.lab2_6.service.BookingService;
import ru.itis.dis403.lab2_6.service.JWTService;
import ru.itis.dis403.lab2_6.service.UserDetailImpl;

import java.util.List;

@RestController
@RequestMapping("/api/booking")
public class BookingController {

    private final BookingRepository bookingRepository;
    private final BookingService bookingService;

    public BookingController(BookingRepository bookingRepository, BookingService bookingService) {
        this.bookingRepository = bookingRepository;
        this.bookingService = bookingService;
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<BookingDto> getBookingById(@PathVariable Long id) {

        UserDetailImpl userDetails =
                (UserDetailImpl) SecurityContextHolder.getContext()
                        .getAuthentication().getPrincipal();

        System.out.println(userDetails.getUser());
        BookingDto booking = bookingService.getBookingById(id, userDetails.getUser());
        System.out.println(booking);
        return ResponseEntity.ok(booking);
    }

    @GetMapping("/all")
    public ResponseEntity<BookingsResponse> getBookings() {

        UserDetailImpl userDetails =
                (UserDetailImpl) SecurityContextHolder.getContext()
                        .getAuthentication().getPrincipal();

        System.out.println(userDetails.getUser());
        List<Booking> bookings = bookingRepository.findByHotel(userDetails.getUser().getHotel());

        bookings.forEach(b-> System.out.println(b.getId()));

        return ResponseEntity.ok(new BookingsResponse(bookings));
    }
    // ru.itis.dis403.lab2_6.controller.BookingController

// Добавим DTO для обновления (можете переиспользовать BookingDto, если он подходит)
// или создать отдельный BookingUpdateDto
// src/main/java/ru/itis/dis403/lab2_6/dto/BookingUpdateDto.java (если нужно)
/*
package ru.itis.dis403.lab2_6.dto;
import java.util.Date;
public class BookingUpdateDto {
    private Date arrivaldate;
    private Date stayingdate;
    private Date departuredate;
    private String room; // Добавим поле room для редактирования
    // getters and setters...
    public Date getArrivaldate() { return arrivaldate; }
    public void setArrivaldate(Date arrivaldate) { this.arrivaldate = arrivaldate; }
    public Date getStayingdate() { return stayingdate; }
    public void setStayingdate(Date stayingdate) { this.stayingdate = stayingdate; }
    public Date getDeparturedate() { return departuredate; }
    public void setDeparturedate(Date departuredate) { this.departuredate = departuredate; }
    public String getRoom() { return room; }
    public void setRoom(String room) { this.room = room; }
}
*/

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateBooking(@PathVariable Long id, @RequestBody BookingDto updatedBookingData) { // Используем BookingDto или BookingUpdateDto

        UserDetailImpl userDetails = (UserDetailImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long currentHotelId = userDetails.getUser().getHotel().getId();

        Booking existingBooking = bookingRepository.findByIdAndHotelId(id, currentHotelId);

        if (existingBooking == null) {
            // Бронь не найдена или не принадлежит пользователю
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Booking not found or does not belong to your hotel.");
        }

        existingBooking.setArrivaldate(updatedBookingData.getArrivaldate());
        existingBooking.setStayingdate(updatedBookingData.getStayingdate());
        existingBooking.setDeparturedate(updatedBookingData.getDeparturedate());
        existingBooking.setRoom(updatedBookingData.getRoom());

        // Сохраним обновлённую бронь
        bookingRepository.save(existingBooking);

        return ResponseEntity.ok("Booking updated successfully.");
    }

}
