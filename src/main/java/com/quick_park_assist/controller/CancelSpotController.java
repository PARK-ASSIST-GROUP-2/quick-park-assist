package com.quick_park_assist.controller;

import com.quick_park_assist.entity.BookingSpot;
import com.quick_park_assist.service.ICancelSpotService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/cancelSpot")
public class CancelSpotController {
    @Autowired
    ICancelSpotService ICancelspotservice;
    @GetMapping("/")
    public  String showCancelForm(Model model){
        model.addAttribute("cancelSpot", new BookingSpot());
        return "CancelBooking";
    }
    @GetMapping("/cancel-booking")
    public String handleGetRequest() {
        return "redirect:/cancelSpot/";
    }
    @PostMapping("/cancel-booking")
    @Transactional
    public String cancelSpot(@RequestParam(value = "bookingId",required = true) Long bookingId,
                             @RequestParam(value = "mobileNumber",required = true) String mobileNumber,
                             Model model
                             ){
        boolean isCancelled = ICancelspotservice.cancelBooking(bookingId,mobileNumber);
        if (isCancelled) {
            model.addAttribute("message", "Booking Cancellation successful!");
        } else {
            model.addAttribute("message", "Cancellation failed.");
        }
        return "success";
    }

}
