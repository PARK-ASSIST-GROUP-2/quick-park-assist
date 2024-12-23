package com.quick_park_assist.serviceImpl;

import com.quick_park_assist.entity.BookingSpot;
import com.quick_park_assist.repository.BookingSpotRepository;
import com.quick_park_assist.service.IModifySpotService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;
import java.util.Optional;

@Service
public class ModifySpotServiceImpl implements IModifySpotService {

    @Autowired
    private BookingSpotRepository bookingSpotRepository;

    @Override
    @Transactional
    public boolean updateSpotDetails(@PathVariable Long bookingId, @PathVariable Long userID, @RequestBody Date startTime,@RequestBody Double duration) {
        // Find the booking by Spot ID
        Optional<BookingSpot> bookingOptional = bookingSpotRepository.findByBookingIdAndUserID(bookingId,userID);

        if (bookingOptional.isPresent()) {
            BookingSpot bookingSpot = bookingOptional.get();

            // Updating the details
            bookingSpot.setStartTime(startTime);
            bookingSpot.setDuration(duration);

            // Save the updated booking
            bookingSpotRepository.save(bookingSpot);
            return true;
        }
        return false; // Spot ID not found
    }

}
