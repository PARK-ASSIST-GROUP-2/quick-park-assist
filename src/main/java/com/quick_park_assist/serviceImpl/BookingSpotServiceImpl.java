package com.quick_park_assist.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quick_park_assist.entity.BookingSpot;
import com.quick_park_assist.repository.BookingSpotRepository;
import com.quick_park_assist.service.IBookingSpotService;

@Service
public class BookingSpotServiceImpl implements IBookingSpotService {

    @Autowired
    private BookingSpotRepository bookingSpotRepository;

	@Override
	public void saveBookingSpot(BookingSpot bookingSpot) {
		bookingSpotRepository.save(bookingSpot);
	}
}