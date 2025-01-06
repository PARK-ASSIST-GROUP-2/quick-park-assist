package com.quick_park_assist.controller;

import com.quick_park_assist.entity.ParkingSpot;
import com.quick_park_assist.entity.User;
import com.quick_park_assist.enums.BookingSpotStatus;
import com.quick_park_assist.repository.BookingSpotRepository;
import com.quick_park_assist.repository.ParkingSpotRepository;
import com.quick_park_assist.repository.UserRepository;
import com.quick_park_assist.service.IParkingSpotService;
import com.quick_park_assist.service.IUpdateParkingSpotService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.quick_park_assist.entity.BookingSpot;
import com.quick_park_assist.service.IBookingSpotService;

import java.util.*;

@Controller
@RequestMapping("/bookingSpot/")
public class BookingSpotController {

	@Autowired
    private IBookingSpotService IbookingSpotService;
    @Autowired
    private IParkingSpotService parkingSpotService;
    @Autowired
    private BookingSpotRepository bookingSpotRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ParkingSpotRepository parkingSpotRepository;

    @GetMapping("/")
    public String showBookingSpotForm(
            @RequestParam(value = "query", required = false) String searchQuery, // Accept user input
            HttpSession session ,
            Model model) { // Retrieve the user from the session
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login"; // Redirect to login if user is not logged in
        }
        Optional<User> currentUser = userRepository.findByEmail(loggedInUser.getEmail());
        if (currentUser.isPresent()){
            User user = currentUser.get();
            model.addAttribute("loggedInUser", user);
        }
        else{
            model.addAttribute("loggedInUser", loggedInUser); // Fallback if not found in DB
        }
        //System.out.println("Working inside booking form controller");
        model.addAttribute("bookingSpot", new BookingSpot());
        // Fetch available parking spots and add to model
        return "myBookingSpot";
    }
    @GetMapping("/searching")
    @ResponseBody
    public List<Map<String, String>> searchParkingSpots(
            @RequestParam("query") String query,
            @RequestParam(value = "query", required = false) String searchQuery, // Accept user input
            HttpSession session ,
            Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        Optional<User> currentUser = userRepository.findByEmail(loggedInUser.getEmail());
        if (currentUser.isPresent()){
            User user = currentUser.get();
            model.addAttribute("loggedInUser", user);
        }
        else{
            model.addAttribute("loggedInUser", loggedInUser); // Fallback if not found in DB
        }
        // Fetch spots based on query (location-based search)
        List<ParkingSpot> parkingSpots = parkingSpotService.getAllAvailableSpots(query);

        // Format the response as JSON with spot ID and location
        List<Map<String, String>> formattedSpots = new ArrayList<>();
        for (ParkingSpot spot : parkingSpots) {
            Map<String, String> spotData = new HashMap<>();
            spotData.put("id", String.valueOf(spot.getId())); // Spot ID
            spotData.put("location", spot.getLocation());    // Location
            formattedSpots.add(spotData);
        }

        return formattedSpots; // Return formatted JSON response

    }

    @GetMapping("/redirect-to-booking")
    public String redirectToBookingPage() {
        return "redirect:/bookingSpot/";
    }

	@PostMapping("/book-spot/")
    public String submitBookingSpotForm(
            @RequestParam("spotId") Long spotId,
            @RequestParam("spotLocation") String spotLocation,
            @ModelAttribute("bookingSpot") BookingSpot bookingSpot,
            Model model,
            HttpSession session ) {
        // Get the logged-in userId from the session
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login"; // Redirect to login if the user is not logged in
        }

        // Fetch the User entity from the database
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Set the user to the bookingSpot
        bookingSpot.setUser(user);
        // Fetch the ParkingSpot entity by spotId
        ParkingSpot parkingSpot = parkingSpotRepository.findById(spotId)
                .orElseThrow(() -> new RuntimeException("Parking spot not found"));

        // Set the spotLocation and spotId in the BookingSpot entity
        bookingSpot.setSpotId(parkingSpot);

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