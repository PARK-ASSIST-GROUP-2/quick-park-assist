package com.quick_park_assist.controller;

import com.quick_park_assist.entity.BookingSpot;
import com.quick_park_assist.service.ICancelSpotService;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/cancelSpot")
public class CancelSpotController {
    @Autowired
    ICancelSpotService cancelSpotService;

    @GetMapping("/")
    public String showCancelForm(HttpSession session, Model model) {
        Long loggedInUser = (Long) session.getAttribute("userId");
        if (loggedInUser == null) {
            return "redirect:/login"; // Redirect to login if user is not in session
        }
        List<BookingSpot> confirmedBookings = cancelSpotService.getConfirmedBookingsByUserID(loggedInUser);
        model.addAttribute("bookings", confirmedBookings);
        model.addAttribute("cancelSpot", new BookingSpot());
        return "CancelBooking";
    }
/*    @PostMapping("/fetchConfirmedBookings")
    public String fetchConfirmedBookings(@RequestParam("mobileNumber") String mobileNumber, Model model) {
        List<BookingSpot> confirmedBookings = cancelSpotService.getConfirmedBookingsByUserID();
        model.addAttribute("bookings", confirmedBookings);
        model.addAttribute("mobileNumber", mobileNumber);
        return "CancelBooking";
    }*/
    @PostMapping("/cancelSelectedBooking")
    @Transactional
    public String cancelSelectedBooking(@RequestParam("bookingId") Long bookingId, Model model) {
        boolean isCancelled = cancelSpotService.cancelBooking(bookingId);
        if (isCancelled) {
            model.addAttribute("message", "Booking successfully Cancelled!");
        } else {
            model.addAttribute("message", "Cancellation failed.");
        }
        return "success";
    }
}
