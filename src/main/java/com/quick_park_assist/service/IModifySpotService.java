package com.quick_park_assist.service;

import com.quick_park_assist.entity.BookingSpot;

import java.util.Date;

public interface IModifySpotService {
    boolean updateSpotDetails(Long spotID, Date startTime, Double duration);
}
