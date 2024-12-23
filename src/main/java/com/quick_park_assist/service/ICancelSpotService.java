package com.quick_park_assist.service;

public interface ICancelSpotService {
    boolean cancelBooking(Long bookingId,String mobileNumber);
}
