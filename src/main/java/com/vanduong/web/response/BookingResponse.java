package com.vanduong.web.response;

import com.vanduong.web.model.Room;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponse {
    private long bookingId;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

    private String guestFullName;

    private String guestEmail;

    private String guestPhone;

    private int guestNumOfAdults;

    private int guestNumOfChildren;

    private int totalGuests;

    private String bookingConfirmationCode;

    private RoomResponse roomResponse;


    public BookingResponse(long bookingId, LocalDate checkInDate, LocalDate checkOutDate, String bookingConfirmationCode) {
        this.bookingId = bookingId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.bookingConfirmationCode = bookingConfirmationCode;
    }

    public BookingResponse(long id, LocalDate checkInDate, LocalDate checkOutDate, String bookingConfirmationCode, RoomResponse room) {
        this.bookingId = id;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.bookingConfirmationCode = bookingConfirmationCode;
        this.roomResponse = room;
    }
}
