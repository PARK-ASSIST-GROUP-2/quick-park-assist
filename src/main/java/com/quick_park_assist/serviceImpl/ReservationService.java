package com.quick_park_assist.serviceImpl;


import com.quick_park_assist.entity.Reservation;
import com.quick_park_assist.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    // Get all reservations
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    // Get reservation by ID
    public Reservation getReservationById(Long id) {
        Optional<Reservation> reservation = reservationRepository.findById(id);
        return reservation.orElse(null);
    }

    // Add a new reservation
    public void addReservation(Reservation reservation) {
        reservationRepository.save(reservation);
    }

    public Reservation findReservationByVehicleNumber(String vehicleNumber) {
        return reservationRepository.findByVehicleNumber(vehicleNumber);
    }

   

    // Update an existing reservation
    public void updateReservation(Reservation reservation) {
        reservationRepository.save(reservation);
    }

    // Delete a reservation
    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }
 // Delete reservation by vehicle number
    public void deleteReservationByVehicleNumber(String vehicleNumber) {
        Reservation reservation = reservationRepository.findByVehicleNumber(vehicleNumber);
        if (reservation != null) {
            reservationRepository.delete(reservation);  // Delete the reservation from the database
        }
    }
}