package com.vanduong.web.service;

import com.vanduong.web.model.Room;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface IRoomService {

    Room addNewRoom(MultipartFile phote, String roomType, float roomPrice) throws IOException, SQLException;

    List<String> getRoomTypes();

    List<Room> getAllRooms();

    byte[] getRoomPhotoByRoomId(long id) throws SQLException;

    void deleteRoom(long id);

    Room updateRoom(long id, byte[] photoBytes, String roomType, float roomPrice);

    Room getRoomById(long id);

    Room save(Room room);

}
