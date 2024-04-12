package com.vanduong.web.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.internal.util.collections.ReadOnlyMap;

import java.time.LocalDate;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class BookedRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long bookingId;

    @Column(name = "check_in_date")
    private LocalDate checkInDate;

    @Column(name = "check_out_date")
    private LocalDate checkOutDate;

    @Column(name = "guest_full_name")
    private String guestFullName;

    @Column(name = "guest_email")
    private String guestEmail;

    @Column(name = "guest_phone")
    private String guestPhone;

    @Column(name = "guest_num_of_adults")
    private int guestNumOfAdults;

    @Column(name = "guest_num_of_children")
    private int guestNumOfChildren;

    @Column (name = "total_guests")
    private int totalGuests;

    @Column(name = "booking_confirmation_code")
    private String bookingConfirmationCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;


    public void calculateTotalGuests() {
        this.totalGuests = this.guestNumOfAdults + this.guestNumOfChildren;
    }

    public void setGuestNumOfChildren(int guestNumOfChildren) {
        this.guestNumOfChildren = guestNumOfChildren;
        calculateTotalGuests();
    }

    public void setGuestNumOfAdults(int guestNumOfAdults) {
        this.guestNumOfAdults = guestNumOfAdults;
        calculateTotalGuests();
    }


    public void setBookingConfirmationCode(String bookingConfirmationCode) {
        this.bookingConfirmationCode = bookingConfirmationCode;
    }


}
