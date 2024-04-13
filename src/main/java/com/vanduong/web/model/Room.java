package com.vanduong.web.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
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
    @Lob
    @JsonIgnore
    private Blob photo;


    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "room")
    @JsonManagedReference
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
        String bookingCode= RandomStringUtils.randomAlphanumeric(10).toUpperCase();
        booking.setBookingConfirmationCode(bookingCode);
    }
}
