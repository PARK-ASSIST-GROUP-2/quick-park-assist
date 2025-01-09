package com.quick_park_assist.controller;

import com.quick_park_assist.entity.User;
import com.quick_park_assist.repository.UserRepository;
import com.quick_park_assist.service.IReservationService;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.quick_park_assist.serviceImpl.ReservationServiceImpl;
import com.quick_park_assist.entity.Reservation;
import org.springframework.expression.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
@Controller
@RequestMapping("/ev-charging")
public class ReservationController {

    @Autowired
    private IReservationService reservationService;
    @Autowired
    private UserRepository userRepository;
    @GetMapping("/list")
    public String listReservations(HttpSession session,Model model) {
        Long loggedInUser = (Long) session.getAttribute("userId");
        if(loggedInUser == null){
            return "redirect:/login";
        }
        List<Reservation> reservations = reservationService.getReservationsByUserId(loggedInUser);
        
        // Format the reservation time
        model.addAttribute("reservations", reservations);
        return "ViewReservations";
    }

    // Show form for adding a new reservation
    @GetMapping("/add")
    public String addReservationForm(HttpSession session, Model model) {
        Long loggedInUser = (Long) session.getAttribute("userId");
        if(loggedInUser == null){
            return "redirect:/login";
        }
        model.addAttribute("reservation", new Reservation());
        return "addReservation";  // Thymeleaf template 'addReservation.html'
    }

    // Process the form for adding a new reservation
    @PostMapping("/add")
    public String addReservation(HttpSession session, Reservation reservation) {
        // Get the logged-in userId from the session
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login"; // Redirect to login if the user is not logged in
        }

        // Fetch the User entity from the database
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Set the user to the bookingSpot
        reservation.setUser(user);

        reservationService.addReservation(reservation);
        return "redirect:/ev-charging/list";  // After adding, redirect to the reservation list
    }

    @GetMapping("/edit")
    public String editForm(HttpSession session, Model model) {
        Long loggedInUser = (Long) session.getAttribute("userId");
        if(loggedInUser == null){
            return "redirect:/login";
        }
        List<Reservation> reservations = reservationService.getReservationsByUserId(loggedInUser);

        // Format the reservation time

        model.addAttribute("reservations", reservations);
        model.addAttribute("vehicleNumber", new String()); // empty string to capture the vehicle number
        return "EditReservation"; // Template to input vehicle number
    }
    @PostMapping("/update-reservation")
    @Transactional
    public String updateSpotDetails(
            @RequestParam(value = "id", required = true) Long id,
            @RequestParam(value = "startTime", required = true) String startTimeStr,
            @RequestParam(value = "vehicleNumber", required = true) String vehicleNumber,
            Model model) {
        try {
            // Define a formatter (this format must match your input string)
            SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

            Date startTime = dateTimeFormatter.parse(startTimeStr);

            boolean isUpdated = reservationService.updateSpotDetails(id, startTime, vehicleNumber);

            if (isUpdated) {
                model.addAttribute("message", "Booking updated successfully!");
            } else {
                model.addAttribute("message", "Update failed. Booking ID not found.");
            }
        } catch (Exception e) {
            model.addAttribute("message", "Invalid date format. Please use the correct format.");
            e.printStackTrace();
        }
        return "redirect:/ev-charging/edit";
    }

    // Step 3: Handle the form submission for updating the reservation
    // Show the delete reservation form
    @GetMapping("/delete-form")
    public String showDeleteForm(HttpSession session, Model model) {
        Long loggedInUser = (Long) session.getAttribute("userId");
        if(loggedInUser == null){
            return "redirect:/login";
        }
        List<Reservation> reservations = reservationService.getReservationsByUserId(loggedInUser);

        model.addAttribute("reservations", reservations);
        return "CancelReservation";
    }

    // Handle the form submission to delete the reservation
    
    
    
    @PostMapping("/delete/{id}")
    public String deleteReservation(@RequestParam("id") Long id, Model model) {
        // Call service to delete the reservation based on vehicle number
       boolean isDeleted =  reservationService.deleteReservationById(id);
       if(isDeleted){
           model.addAttribute("message","Reservation Successfully Cancelled");
           return "success";
       }
        model.addAttribute("message","Reservation Couldn't be Cancelled");

        return "/ev-charging/delete-form";  // Redirect to the reservation list after deletion
    }


    

   
}