package com.quick_park_assist.repository;

import com.quick_park_assist.entity.BookingSpot;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingSpotRepository extends JpaRepository<BookingSpot, Long> {
    Optional<BookingSpot> findByBookingIdAndUserID(Long bookingId, Long userID);
    Optional<BookingSpot> findByBookingIdAndMobileNumber(Long bookingId, String mobileNumber);
    List<BookingSpot> findByUserIDAndMobileNumber(Long userID,String mobileNumber);
}