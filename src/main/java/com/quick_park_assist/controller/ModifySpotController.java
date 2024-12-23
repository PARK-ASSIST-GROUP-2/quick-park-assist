package com.quick_park_assist.controller;

import com.quick_park_assist.entity.BookingSpot;
import com.quick_park_assist.service.IModifySpotService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("/modifySpot")
public class ModifySpotController {
    @Autowired
    private IModifySpotService IModifyspotservice;


    @GetMapping("/")
    public String showModifySpotForm(Model model){
        System.out.println("Working inside modify form controller");
        model.addAttribute("modifySpot", new BookingSpot() );
        return "ModifyBooking";
    }
    @GetMapping("/update-spot")
    public String handleGetRequest() {
        return "redirect:/modifySpot/";
    }

    @PostMapping("/update-spot")
    @Transactional
    public String updateSpotDetails(
            @RequestParam(value = "bookingId", required=true)  Long bookingId,
            @RequestParam(value = "userID", required=true)  Long userID,
            @RequestParam(value = "startTime",required = true) String startTimeStr,
            @RequestParam(value ="duration",required = true) Double duration,
            Model model) {
        // Call the service to update the booking details
        try {
            SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

            Date startTime = dateTimeFormatter.parse(startTimeStr);

            boolean isUpdated = IModifyspotservice.updateSpotDetails(bookingId,userID, startTime, duration);

            if (isUpdated) {
                model.addAttribute("message", "Booking updated successfully!");
            } else {
                model.addAttribute("message", "Update failed. UserID not found.");
            }
        }
        catch (java.text.ParseException e){
            model.addAttribute("message", "Invalid date format. Please use the correct format.");
            e.printStackTrace();
        }
        catch (ParseException e) {
            model.addAttribute("message", "Invalid date format. Please use the correct format.");
            e.printStackTrace();
        }
        return "success";
    }
}
