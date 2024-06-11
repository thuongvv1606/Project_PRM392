package com.example.restaurantproject.repository;

import android.content.Context;

import com.example.restaurantproject.bean.Reservation;
import com.example.restaurantproject.dao.ReservationDao;
import com.example.restaurantproject.dao.RestaurantDatabase;

import java.util.List;

public class ReservationRepository {
    private ReservationDao reservationDao;

    public ReservationRepository(Context context) {
        reservationDao = RestaurantDatabase.getInstance(context).reservationDao();
    }

    public void createReservation(Reservation reservation) {
        reservationDao.insert(reservation);
    }

    public void updateReservation(Reservation reservation) {
        reservationDao.update(reservation);
    }

    public Reservation getReservation(int reservationId) {
        return reservationDao.select(reservationId);
    }

    public List<Reservation> getAllReservations() {
        return reservationDao.selectAll();
    }

    public void deleteReservation(int reservationId) {
        reservationDao.delete(reservationId);
    }

    public void deleteAllReservations() {
        reservationDao.deleteAll();
    }
}

