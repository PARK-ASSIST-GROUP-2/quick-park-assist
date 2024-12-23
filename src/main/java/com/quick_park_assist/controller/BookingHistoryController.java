package com.quick_park_assist.controller;

import com.quick_park_assist.entity.BookingSpot;
import com.quick_park_assist.service.IBookingHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/bookingHistory")
public class BookingHistoryController {
    @Autowired
    IBookingHistoryService IBookinghistoryservice;

    @GetMapping("/")
    public  String showCancelForm(Model model){
        model.addAttribute("cancelSpot", new BookingSpot());
        return "BookingHistory";
    }
    @GetMapping("/booking-history")
    public String handleGetRequest() {
        return "redirect:/bookingHistory/";
    }
    @PostMapping("/booking-history")
    public String viewBookingHistory(@RequestParam Long userID, @RequestParam String mobileNumber, Model model) {
        List<BookingSpot> bookings = IBookinghistoryservice.getBookingsByUserIdAndMobile(userID, mobileNumber);
        model.addAttribute("bookings", bookings);
        return "bookingHistory"; // Name of the Thymeleaf template
    }

}
