package com.quick_park_assist.serviceImpl;

import com.quick_park_assist.entity.BookingSpot;
import com.quick_park_assist.enums.BookingSpotStatus;
import com.quick_park_assist.repository.BookingSpotRepository;
import com.quick_park_assist.service.IModifySpotService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ModifySpotServiceImpl implements IModifySpotService {

    @Autowired
    private BookingSpotRepository bookingSpotRepository;
    @Override
    public List<BookingSpot> getConfirmedBookings(Long UserID) {

        return bookingSpotRepository.findByUserIDAndBookingSpotStatus(UserID, BookingSpotStatus.CONFIRMED);
    }
    @Override
    @Transactional
    public boolean updateSpotDetails(@PathVariable Long bookingId, @RequestBody Date startTime,@RequestBody Double duration) {
        // Find the booking by Spot ID
        Optional<BookingSpot> bookingOptional = bookingSpotRepository.findById(bookingId);
        if (bookingOptional.isPresent()) {
            BookingSpot bookingSpot = bookingOptional.get();
            bookingSpot.setStartTime(startTime); // Assumes `startTime` is passed as an ISO string
            bookingSpot.setDuration(duration);
            bookingSpotRepository.save(bookingSpot);
            return true;
        }
        return false;
    }

}
