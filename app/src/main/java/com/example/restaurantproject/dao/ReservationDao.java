package com.example.restaurantproject.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.restaurantproject.bean.Reservation;

import java.util.List;

@Dao
public interface ReservationDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Reservation reservation);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Reservation reservation);

    @Query("SELECT * FROM Reservation WHERE reservation_id = :reservationId")
    Reservation select(int reservationId);

    @Query("SELECT * FROM Reservation")
    List<Reservation> selectAll();

    @Query("DELETE FROM Reservation")
    void deleteAll();

    @Query("DELETE FROM Reservation WHERE reservation_id = :reservationId")
    void delete(int reservationId);
}
