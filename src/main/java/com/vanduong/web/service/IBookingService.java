package com.vanduong.web.service;

import com.vanduong.web.model.BookedRoom;
import com.vanduong.web.response.BookingResponse;

import java.awt.print.Book;
import java.util.List;

public interface IBookingService {
    List<BookedRoom> getAllBooking();

    BookedRoom getBookingByConfirmationCode(String confirmationCode);

    String saveBooking(long id, BookedRoom bookingResponse);

    void cancelBooking(long bookingId);

    void checkRoomStatus(long bookingId);
}
