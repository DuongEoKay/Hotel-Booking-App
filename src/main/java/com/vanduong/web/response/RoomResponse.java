package com.vanduong.web.response;



import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;

import java.sql.Blob;
import java.util.List;

@Data
@NoArgsConstructor
public class RoomResponse {
    private long id;
    private String roomType;
    private float roomPrice;
    private boolean isBooked =false;
    private String photo;
    private List<BookingResponse> bookings;

    public RoomResponse(long id, String roomType, float roomPrice) {
        this.id = id;
        this.roomType = roomType;
        this.roomPrice = roomPrice;
    }

    public RoomResponse(long id, List<BookingResponse> bookings,
                        byte[] photoBytes,
                        boolean isBooked, String roomType, float roomPrice) {
        this.id = id;
        //this.bookings = bookings;
        this.photo = photoBytes !=null ? Base64.encodeBase64String(photoBytes) : null;
        this.isBooked = isBooked;
        this.roomType = roomType;
        this.roomPrice = roomPrice;
    }
}
