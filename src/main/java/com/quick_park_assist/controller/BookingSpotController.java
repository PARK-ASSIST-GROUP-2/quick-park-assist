package com.quick_park_assist.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.quick_park_assist.entity.BookingSpot;
import com.quick_park_assist.service.IBookingSpotService;

@Controller
@RequestMapping("/bookingSpot/")
public class BookingSpotController {

	@Autowired
    private IBookingSpotService IbookingSpotService;

	@GetMapping("/")
    public String showBookingSpotForm(Model model) {
        System.out.println("Working inside booking form controller");
        model.addAttribute("bookingSpot", new BookingSpot());
        return "myBookingSpot";
    }

	@PostMapping("/book-spot/")
    public String submitBookingSpotForm(
            @ModelAttribute("bookingSpot") BookingSpot bookingSpot,
            Model model) {

        // Save the booking details
        IbookingSpotService.saveBookingSpot(bookingSpot);

        // Generate a success message based on the action
        String successMessage = "Your Booking is Successful!";

        // Add the success message to the model
        model.addAttribute("message", successMessage);

        // Return the success page view
        return "success";
    }
}