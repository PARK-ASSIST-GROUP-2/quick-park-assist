package com.quick_park_assist.repository;

import com.quick_park_assist.entity.BookingSpot;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;

import com.quick_park_assist.entity.User;
import com.quick_park_assist.enums.BookingSpotStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingSpotRepository extends JpaRepository<BookingSpot, Long> {
    @Query("SELECT b FROM BookingSpot b WHERE b.user.id = :userId")
    List<BookingSpot> findBookingsByUserId(@Param("userId") Long userId);
    // used in cancel-booking
    @Query("SELECT b FROM BookingSpot b WHERE b.user.id = :userId AND b.bookingSpotStatus = :bookingSpotStatus")
    List<BookingSpot> findByUserIDAndBookingSpotStatus(@Param("userId") Long userId, BookingSpotStatus bookingSpotStatus);
    @Query("SELECT b FROM BookingSpot b WHERE b.user.id = :userId AND b.spotLocation = :spotLocation")
    List<BookingSpot> getBookingsBySpotLocationAndUserId(@Param("userId") Long userId, @Param("spotLocation") String spotLocation);

    Optional<BookingSpot> findByBookingId(Long bookingId);
}