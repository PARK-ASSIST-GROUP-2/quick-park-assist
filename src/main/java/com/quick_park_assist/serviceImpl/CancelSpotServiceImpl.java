package com.quick_park_assist.serviceImpl;

import com.quick_park_assist.entity.BookingSpot;
import com.quick_park_assist.enums.BookingSpotStatus;
import com.quick_park_assist.repository.BookingSpotRepository;
import com.quick_park_assist.service.ICancelSpotService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CancelSpotServiceImpl implements ICancelSpotService {

    @Autowired
    BookingSpotRepository bookingSpotRepository;

    @Override
    @Transactional
    public boolean cancelBooking(Long bookingId, String mobileNumber) {
        Optional<BookingSpot> bookingOptional = bookingSpotRepository.findByBookingIdAndMobileNumber(bookingId,mobileNumber);
        if (bookingOptional.isPresent()) {
            BookingSpot bookingSpot = bookingOptional.get();
            bookingSpot.setBookingSpotStatus(BookingSpotStatus.CANCELLED);
            bookingSpotRepository.save(bookingSpot);
            return true;
        }
        return false;
    }
}
