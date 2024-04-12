package com.vanduong.web.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String roomType;
    private float roomPrice;
    private boolean isBooked =false;
    @Lob
    private Blob photo;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "room")
    private List<BookedRoom> bookings;

    public Room(long id) {
        this.bookings = new ArrayList<>();
    }


    public void addBooking(BookedRoom booking) {
        if(bookings==null)
        {
            bookings = new ArrayList<>();
        }
        bookings.add(booking);
        booking.setRoom(this);
        isBooked=true;
        String bookingCode= RandomStringUtils.randomAlphanumeric(10).toUpperCase();
        booking.setBookingConfirmationCode(bookingCode);
    }
}
