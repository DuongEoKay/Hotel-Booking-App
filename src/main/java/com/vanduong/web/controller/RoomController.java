package com.vanduong.web.controller;

import com.vanduong.web.model.BookedRoom;
import com.vanduong.web.model.Room;
import com.vanduong.web.response.BookingResponse;
import com.vanduong.web.response.RoomResponse;
import com.vanduong.web.service.BookingService;
import com.vanduong.web.service.RoomService;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.vanduong.web.service.IRoomService;


import javax.sql.rowset.serial.SerialBlob;
import javax.swing.*;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@CrossOrigin("http://localhost:5173")
@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class RoomController {

    private final IRoomService roomService;
    private final BookingService bookingService;

    @PostMapping("/add/new-room/")
    public ResponseEntity<RoomResponse> addNewRoom(@RequestParam("photo") MultipartFile phote,
                                                   @RequestParam("roomType") String roomType,
                                                   @RequestParam("roomPrice") float roomPrice) throws SQLException, IOException {
        Room savedRoom = roomService.addNewRoom(phote, roomType, roomPrice);
        RoomResponse roomResponse = new RoomResponse(savedRoom.getId(), savedRoom.getRoomType(), savedRoom.getRoomPrice());
        return ResponseEntity.ok(roomResponse);


    }


    @GetMapping("/{id}")
    public ResponseEntity<RoomResponse> getRoomById(@PathVariable("id") long id) {
        Room room = roomService.getRoomById(id);
        RoomResponse roomResponse = getRoomResponse(room);
        return ResponseEntity.ok(roomResponse);
    }



    @GetMapping("/types/")
    public List<String> getRoomTypes() {
        return roomService.getRoomTypes();
    }

    @GetMapping("/")
    public ResponseEntity<List<RoomResponse>> getAllRooms() throws SQLException {
        List<Room> rooms = roomService.getAllRooms();
        List<RoomResponse> roomResponses = new ArrayList<>();
        for (Room room : rooms) {
            byte[] photoBytes = roomService.getRoomPhotoByRoomId(room.getId());
            if (photoBytes != null && photoBytes.length > 0) {
                String base64Photo = Base64.getEncoder().encodeToString(photoBytes);
                RoomResponse roomResponse = getRoomResponse(room);
                roomResponse.setPhoto(base64Photo);
                roomResponses.add(roomResponse);


            }

        }
        return ResponseEntity.ok(roomResponses);

    }
        private RoomResponse getRoomResponse (Room room){
            List<BookedRoom> bookings = getAllBookingByRoomId(room.getId());
            List<BookingResponse>  bookingInfo = bookings
                    .stream().map(booking -> new BookingResponse(
                            booking.getBookingId(),
                            booking.getCheckInDate(),
                            booking.getCheckOutDate(),
                            booking.getBookingConfirmationCode())).toList();
            byte[] photoBytes = null;
            Blob photoBlob = room.getPhoto();
            if (photoBlob != null) {
                try {
                    photoBytes = photoBlob.getBytes(1, (int) photoBlob.length());
                } catch (SQLException throwables) {
                    throw new RuntimeException("Error retrieving photo");
                }
            }

            return new RoomResponse(room.getId(),
                    bookingInfo,
                    photoBytes,
                    room.getRoomType(),
                    room.getRoomPrice());
        }

    private List<BookedRoom> getAllBookingByRoomId(long id) {
        return bookingService.getAllBookingByRoomId(id);
    }



    @PutMapping("/update/{id}")
    public ResponseEntity<RoomResponse> updateRoom(@PathVariable("id") long id,
                                                   @RequestParam(required = false) MultipartFile photo,
                                                   @RequestParam(required = false) String roomType,
                                                   @RequestParam(required = false) float roomPrice) throws SQLException, IOException {
        byte[] photoBytes = photo != null && !photo.isEmpty() ? photo.getBytes() : roomService.getRoomPhotoByRoomId(id);
        Blob photoBlob = photoBytes != null && photoBytes.length>0 ? new SerialBlob(photoBytes) : null;
        Room room = roomService.updateRoom(id, photoBytes, roomType, roomPrice);
        room.setPhoto(photoBlob);
        RoomResponse roomResponse = getRoomResponse(room);
        return ResponseEntity.ok(roomResponse);
    }




    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteRoom(@PathVariable("id") long id) {
        roomService.deleteRoom(id);
        return ResponseEntity.ok("Room deleted");
    }
}
