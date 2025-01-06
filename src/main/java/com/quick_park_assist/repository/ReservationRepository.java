package com.quick_park_assist.repository;



import com.quick_park_assist.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    boolean existsByChargingStationAndReservationTime(String chargingStation, LocalDateTime reservationTime);
    // Find reservation by vehicle number
    Reservation findByVehicleNumber(String vehicleNumber);
}
