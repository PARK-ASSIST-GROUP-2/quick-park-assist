package com.quick_park_assist.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.quick_park_assist.serviceImpl.ReservationService;
import com.quick_park_assist.entity.Reservation;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/ev-charging")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @GetMapping("/list")
    public String listReservations(Model model) {
        List<Reservation> reservations = reservationService.getAllReservations();
        
        // Format the reservation time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        reservations.forEach(reservation -> 
            reservation.setFormattedReservationTime(reservation.getReservationTime().format(formatter))
        );

        model.addAttribute("reservations", reservations);
        return "list";
    }

    // Show form for adding a new reservation
    @GetMapping("/add")
    public String addReservationForm(Model model) {
        model.addAttribute("reservation", new Reservation());
        return "addReservation";  // Thymeleaf template 'addReservation.html'
    }

    // Process the form for adding a new reservation
    @PostMapping("/add")
    public String addReservation(Reservation reservation) {
        reservationService.addReservation(reservation);
        return "redirect:/list";  // After adding, redirect to the reservation list
    }

    @GetMapping("/edit")
    public String editForm(Model model) {
        model.addAttribute("vehicleNumber", new String()); // empty string to capture the vehicle number
        return "editForm"; // Template to input vehicle number
    }

    // Step 2: After submitting the vehicle number, fetch the reservation based on it
    @PostMapping("/edit")
    public String findReservationByVehicleNumber(@RequestParam("vehicleNumber") String vehicleNumber, Model model) {
        // Fetch the reservation from the service based on the vehicle number
        Reservation reservation = reservationService.findReservationByVehicleNumber(vehicleNumber);
        
        // If reservation is not found, return an error message
        if (reservation == null) {
            model.addAttribute("error", "Reservation not found for this vehicle number");
            return "editForm"; // Show the error in the vehicle number input form
        }

        // Format the reservation time as needed (for example: yyyy-MM-dd'T'HH:mm)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        String formattedDate = reservation.getReservationTime().format(formatter);
        
        // Add the formatted date and the reservation object to the model
        model.addAttribute("formattedReservationTime", formattedDate);
        model.addAttribute("reservation", reservation); // Add the reservation object to the model
        
        return "updateForm"; // Display the update form
    }

    // Step 3: Handle the form submission for updating the reservation
    @PostMapping("/update")
    public String updateReservation(@ModelAttribute Reservation reservation, @RequestParam("reservationTime") String reservationTime, Model model) {
        // Parse the reservationTime string into a LocalDateTime object
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime parsedDate = LocalDateTime.parse(reservationTime, formatter);

        // Update the reservation with the new reservationTime
        reservation.setReservationTime(parsedDate);
        
        // Save the updated reservation
        reservationService.updateReservation(reservation);
        
        // Redirect to the list of reservations after successful update
        return "redirect:/list";
    }
    
    // Show the delete reservation form
    @GetMapping("/delete-form")
    public String showDeleteForm() {
        return "delete-formhtml";  // Return the delete form page
    }

    // Handle the form submission to delete the reservation
    
    
    
    @PostMapping("/delete")
    public String deleteReservation(@RequestParam("vehicleNumber") String vehicleNumber) {
        // Call service to delete the reservation based on vehicle number
        reservationService.deleteReservationByVehicleNumber(vehicleNumber);
        return "redirect:/list";  // Redirect to the reservation list after deletion
    }


    

   
}