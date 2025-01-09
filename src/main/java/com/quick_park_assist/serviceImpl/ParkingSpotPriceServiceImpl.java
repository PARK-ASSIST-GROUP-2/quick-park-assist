package com.quick_park_assist.serviceImpl;

import com.quick_park_assist.entity.ParkingSpot;
import com.quick_park_assist.repository.ParkingSpotRepository;
import com.quick_park_assist.service.IParkingSpotPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ParkingSpotPriceServiceImpl implements IParkingSpotPriceService {
    @Autowired
    private ParkingSpotRepository parkingSpotRepository;

    @Override
    public ParkingSpot updatePrice(Long id, String location, double pricePerHour, String spotType, String availability, String additionalInstructions, String accessibleSpot) {
        ParkingSpot spot = parkingSpotRepository.findById(id).orElseThrow(() -> new RuntimeException("Parking spot not found"));
        System.out.println("spot before update:"+spot);
        spot.setPricePerHour(pricePerHour);
        spot.setLocation(location);
        spot.setSpotType(spotType);
        spot.setAvailability(availability);
        spot.setAdditionalInstructions(additionalInstructions);
        System.out.println("spot after update:"+spot.toString());
        return parkingSpotRepository.save(spot);
    }
}
