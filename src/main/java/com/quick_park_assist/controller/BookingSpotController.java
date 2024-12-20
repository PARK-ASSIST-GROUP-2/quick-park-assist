package com.quick_park_assist.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.quick_park_assist.entity.BookingSpot;
import com.quick_park_assist.service.IBookingSpotService;

@Controller
@RequestMapping("/bookingSpot")
public class BookingSpotController {
	
	@Autowired
    private IBookingSpotService IbookingSpotService;
	
	@GetMapping("/")
    public String showBookingSpotForm(Model model) {
        model.addAttribute("bookingSpot", new BookingSpot());
        return "BookingSpot";
    }
	
	@PostMapping("/book-spot")
    public String submitBookingSpotForm(@ModelAttribute("bookingSpot")BookingSpot bookingSpot) {
		IbookingSpotService.saveBookingSpot(bookingSpot);
        return "success";
    }
}