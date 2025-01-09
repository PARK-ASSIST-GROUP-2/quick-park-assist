package com.quick_park_assist.service;

import com.quick_park_assist.entity.BookingSpot;
import com.quick_park_assist.entity.User;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
@Service
public interface IModifySpotService {
    boolean updateSpotDetails(Long BookingId, Date startTime, Double duration);
    List<BookingSpot> getConfirmedBookings(Long UserID);
}
