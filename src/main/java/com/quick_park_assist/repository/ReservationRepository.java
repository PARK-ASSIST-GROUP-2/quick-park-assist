package com.quick_park_assist.repository;



import com.quick_park_assist.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    boolean existsByChargingStationAndReservationTime(String chargingStation, LocalDateTime reservationTime);
    // Find reservation by vehicle number
    Reservation findByVehicleNumberAndId(String vehicleNumber,Long Id);
    List<Reservation> findByUserId(Long userId);

}
