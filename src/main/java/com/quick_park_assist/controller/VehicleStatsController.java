package com.quick_park_assist.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/stats")
public class VehicleStatsController {
    @GetMapping
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("availableSpots", 20);  // Replace with dynamic data from your database
        stats.put("activeBookings", 5);
        stats.put("totalHours", 75);
        stats.put("amountSpent", 150);
        return stats;
    }
}
