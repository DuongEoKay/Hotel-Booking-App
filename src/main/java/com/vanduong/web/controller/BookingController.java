package com.vanduong.web.controller;

import com.vanduong.web.model.BookedRoom;
import com.vanduong.web.model.Room;
import com.vanduong.web.response.BookingResponse;
import com.vanduong.web.response.RoomResponse;
import com.vanduong.web.service.BookingService;
import com.vanduong.web.service.IBookingService;
import com.vanduong.web.service.IRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin("http://localhost:5173")
@RequiredArgsConstructor
@RestController
@RequestMapping("/bookings")
public class BookingController {
    private final IBookingService bookingService;
    private final IRoomService roomService;




    @GetMapping("/")
    public ResponseEntity<List<BookingResponse>> getAllBooking() {
        List< BookedRoom > bookedRooms = bookingService.getAllBooking();
        List<BookingResponse>bookingResponses = new ArrayList<>();
        for(BookedRoom bookedRoom : bookedRooms)
        {
            BookingResponse bookingResponse = getBookingResponse(bookedRoom);
            bookingResponses.add(bookingResponse);
        }
        return ResponseEntity.ok(bookingResponses);
    }

    private BookingResponse getBookingResponse(BookedRoom bookedRoom) {
        Room room = roomService.getRoomById(bookedRoom.getRoom().getId());
        RoomResponse roomResponse = new RoomResponse(room.getId(), room.getRoomType(), room.getRoomPrice());
        return new BookingResponse(bookedRoom.getBookingId(), bookedRoom.getCheckInDate(), bookedRoom.getCheckOutDate(), bookedRoom.getBookingConfirmationCode(), roomResponse );
    }


    @GetMapping("/confirmation/{confirmationCode}")
    public ResponseEntity<BookingResponse> getBookingByConfirmationCode(@PathVariable String confirmationCode) {
        try {
            BookedRoom bookedRoom = bookingService.getBookingByConfirmationCode(confirmationCode);
            BookingResponse bookingResponse = getBookingResponse(bookedRoom);
            return ResponseEntity.ok(bookingResponse);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping("/room/{id}/booking/")
    public ResponseEntity<?> saveBooking(@PathVariable("id") long id, @RequestBody BookedRoom bookingResponse) {
        try {
            String confirmationCode = bookingService.saveBooking(id, bookingResponse);
            bookingService.checkRoomStatus(id);
            return ResponseEntity.ok("Save booking successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



    @DeleteMapping("/cancel/{bookingId}")
    public ResponseEntity<?> cancelBooking(@PathVariable("bookingId") long bookingId) {
        try {
            bookingService.cancelBooking(bookingId);
            //bookingService.checkRoomStatus(bookingId);
            return ResponseEntity.ok("Cancel booking successfully");
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

}
