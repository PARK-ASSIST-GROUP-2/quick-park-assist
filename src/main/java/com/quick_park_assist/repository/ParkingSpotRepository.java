package com.quick_park_assist.repository;

import com.quick_park_assist.entity.ParkingSpot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
public interface ParkingSpotRepository extends JpaRepository<ParkingSpot, Long> {
    List<ParkingSpot> findByLocationContainingIgnoreCase(String location);
    List<ParkingSpot> findByUserId(Long userId);
    List<ParkingSpot> findByLocationContainingIgnoreCaseAndAvailabilityIgnoreCase(String location, String availability);
    List<ParkingSpot> findByAvailabilityIgnoreCaseAndSpotLocationContainingIgnoreCaseOrLocationContainingIgnoreCase(
            String availability, String spotLocation, String location);
}