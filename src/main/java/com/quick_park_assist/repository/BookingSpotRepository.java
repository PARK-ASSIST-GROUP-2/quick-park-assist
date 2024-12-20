package com.quick_park_assist.repository;

import com.quick_park_assist.entity.BookingSpot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingSpotRepository extends JpaRepository<BookingSpot, Long> {
}