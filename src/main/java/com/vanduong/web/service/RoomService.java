package com.vanduong.web.service;

import com.vanduong.web.model.Room;
import com.vanduong.web.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomService implements IRoomService{

    private final RoomRepository roomRepository;
    @Override
    public Room addNewRoom(MultipartFile file, String roomType, float roomPrice) throws IOException, SQLException {
        Room room = new Room();
        room.setRoomType(roomType);
        room.setRoomPrice(roomPrice);
        if(!file.isEmpty()){
           byte[] photeBytes = file.getBytes();
            Blob photeBlob = new SerialBlob(photeBytes);
            room.setPhoto(photeBlob);
        }
        return roomRepository.save(room);
    }

    @Override
    public List<String> getRoomTypes() {
        return roomRepository.findDistinctRoomType();
    }

    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @Override
    public byte[] getRoomPhotoByRoomId(long id) throws SQLException {
        Optional<Room> theRoom = roomRepository.findById(id);
        if(theRoom.isEmpty())
        {
            throw new RuntimeException("Room not found");
        }
        Blob photoBlob = theRoom.get().getPhoto();
        if(photoBlob != null)
        {
            return photoBlob.getBytes(1, (int) photoBlob.length());
        }
        return null;
    }

    @Override
    public void deleteRoom(long id) {
        Optional<Room> theRoom = roomRepository.findById(id);
        if(theRoom.isEmpty())
        {
            throw new RuntimeException("Room not found");
        }
        try {
            roomRepository.deleteById(id);
        }  catch (Exception e)
        {
            throw new RuntimeException("Error deleting room");
        }
    }

    @Override
    public Room updateRoom(long id, byte[] photoBytes, String roomType, float roomPrice) {
        Optional<Room> theRoom = roomRepository.findById(id);
        if(theRoom.isEmpty())
        {
            throw new RuntimeException("Room not found");
        }

        Room room = theRoom.get();

        if(roomType != null && !roomType.isEmpty())
        {
            room.setRoomType(roomType);
        }


        if(roomPrice > 0){
            room.setRoomPrice(roomPrice);
        }


        if(photoBytes != null && photoBytes.length>0)
        {
            Blob photoBlob = null;
            try {
                photoBlob = new SerialBlob(photoBytes);
            } catch (SQLException throwables) {
                throw new RuntimeException("Error updating photo");
            }
            room.setPhoto(photoBlob);
        }
        return roomRepository.save(room);
    }

    @Override
    public Room getRoomById(long id) {
        Optional<Room> theRoom = roomRepository.findById(id);
        if(theRoom.isEmpty())
        {
            throw new RuntimeException("Room not found");
        }
        return theRoom.get();
    }

    @Override
    public Room save(Room room) {
        return roomRepository.save(room);
    }
}
