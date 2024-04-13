package com.vanduong.web.service;

import com.vanduong.web.model.BookedRoom;
import com.vanduong.web.model.Room;
import com.vanduong.web.repository.BookingRepository;
import com.vanduong.web.response.BookingResponse;
import org.springframework.stereotype.Service;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService implements IBookingService{



    private final BookingRepository bookingRepository;
    private final IRoomService roomService;

    public BookingService(BookingRepository bookingRepository, IRoomService roomService) {
        this.bookingRepository = bookingRepository;
        this.roomService = roomService;
    }


    public List<BookedRoom> getAllBookingByRoomId(long id) {
        return bookingRepository.findByRoomId(id);
    }

    @Override
    public List<BookedRoom> getAllBooking() {
        return bookingRepository.findAll();
    }



    @Override
    public BookedRoom getBookingByConfirmationCode(String confirmationCode) {
        return bookingRepository.findAll().stream().filter(
                booking -> booking.getBookingConfirmationCode()
                        .equals(confirmationCode)).findFirst().orElseThrow(()
                -> new RuntimeException("Booking not found"));
    }



    @Override
    public String saveBooking(long id, BookedRoom bookingRequest) {
        if(bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate()))
        {
            throw new RuntimeException("Check out date must be after check in date");
        }
        if(bookingRequest.getRoom().getId() != id)
        {
            throw new RuntimeException("Room id not match");
        }
        Room room = roomService.getRoomById(id);
        List<BookedRoom> existingBooking = room.getBookings();
        boolean roomIsAvailable = roomIsAvailable(bookingRequest, existingBooking);
        if(roomIsAvailable)
        {
            room.addBooking(bookingRequest);
            bookingRepository.save(bookingRequest);
        }
        else {
            throw new RuntimeException("Room is not available");
        }
        
        return bookingRequest.getBookingConfirmationCode();
    }

    private boolean roomIsAvailable(BookedRoom bookingResponse, List<BookedRoom> existingBooking) {
        for(BookedRoom bookedRoom : existingBooking)
        {
            if(bookingResponse.getCheckInDate().isBefore(bookedRoom.getCheckOutDate()) && bookingResponse.getCheckOutDate().isAfter(bookedRoom.getCheckInDate()))
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public void cancelBooking(long bookingId) {
        Optional<BookedRoom> bookedRoom = bookingRepository.findById(bookingId);
        if (bookedRoom.isPresent()) {
            Room room = bookedRoom.get().getRoom();
            room.getBookings().remove(bookedRoom.get());
            bookingRepository.deleteById(bookingId);
            List<BookedRoom> bookings = room.getBookings();

        } else {
            throw new RuntimeException("Booking not found");
        }
    }


    public void checkRoomStatus(long roomId) {
        Room room = roomService.getRoomById(roomId);


        roomService.save(room);
    }
}