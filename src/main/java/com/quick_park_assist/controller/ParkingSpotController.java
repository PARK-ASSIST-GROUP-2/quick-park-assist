package com.quick_park_assist.controller;

import com.quick_park_assist.entity.ParkingSpot;
import com.quick_park_assist.entity.User;
import com.quick_park_assist.repository.ParkingSpotRepository;
import com.quick_park_assist.repository.UserRepository;
import com.quick_park_assist.service.IParkingSpotPriceService;
import com.quick_park_assist.service.IUpdateParkingSpotService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/smart-spots/")
public class ParkingSpotController {

    @Autowired
    private ParkingSpotRepository parkingSpotRepository;
    @Autowired
    private IParkingSpotPriceService parkingSpotService;
    @Autowired
    private IUpdateParkingSpotService updateParkingSpotService;
    @Autowired
    private UserRepository userRepository;

    public ParkingSpotController(ParkingSpotRepository parkingSpotRepository) {
        this.parkingSpotRepository = parkingSpotRepository;
    }

    @GetMapping("/add-spot")
    public String showAddSpotForm(HttpSession session, Model model) {
        // Retrieve the logged-in user from the session
        Long loggedInUser = (Long) session.getAttribute("userId");

        if (loggedInUser == null) {
            return "redirect:/login"; // Redirect to login if user is not in session
        }

        model.addAttribute("parkingSpot", new ParkingSpot());
        return "AddParkingSpot"; // Show the add spot form page
    }

    @PostMapping("/add-spot/")
    public String addSpot(HttpSession session, @ModelAttribute("parkingSpot") ParkingSpot parkingSpot, Model model) {
        System.out.println("spot is adding");
        // Get the logged-in userId from the session
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login"; // Redirect to login if the user is not logged in
        }

        // Fetch the User entity from the database
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Set the user to the bookingSpot
        parkingSpot.setUser(user);

        parkingSpotRepository.save(parkingSpot);// Save the parking spot to the database
        model.addAttribute("message", "Your new Parking Spot is now Added!");
        return "success"; // Redirect after saving
    }

    @GetMapping("/searching")
    @ResponseBody
    public List<ParkingSpot> searchLocations(@RequestParam(value = "query", required = false) String query) {
        return query != null && !query.isEmpty()
                ? parkingSpotRepository.findByLocationContainingIgnoreCase(query)
                : new ArrayList<>();
    }


    @GetMapping("/search")
    public String showSearchParkingSpotsForm(
            @RequestParam(value = "location", required = false) String location,
            @RequestParam(value = "availability", required = false) String availability,
            Model model,
            HttpSession session) {
        // Retrieve the logged-in user from the session
        Long loggedInUser = (Long) session.getAttribute("userId");

        if (loggedInUser == null) {
            return "redirect:/login"; // Redirect to login if user is not in session
        }

        if (location == null || location.trim().isEmpty()) {
            model.addAttribute("error", "Please enter a location to search.");
            return "SearchParkingSpot";
        }
        System.out.println("Searching for location: " + location + ", availability: " + availability);
        List<ParkingSpot> parkingSpots;
        try {
            if ("all".equalsIgnoreCase(availability)) {
                parkingSpots = parkingSpotRepository.findByLocationContainingIgnoreCase(location);
            } else {
                parkingSpots = parkingSpotRepository.findByLocationContainingIgnoreCaseAndAvailabilityIgnoreCase(
                        location, "Available".equalsIgnoreCase(availability) ? "Available" : "Unavailable");
            }
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "An error occurred while searching for parking spots.");
            return "SearchParkingSpot";
        }

        if (parkingSpots.isEmpty()) {
            model.addAttribute("message", "No parking spots found for the given location and availability.");
        } else {
            model.addAttribute("parkingSpots", parkingSpots);
        }

        model.addAttribute("location", location);
        model.addAttribute("availability", availability);

        return "SearchParkingSpot";
    }

    @GetMapping("/update-spot")
    public String getParkingSpots(Model model, HttpSession session) {
        // Retrieve the logged-in user from the session
        Long loggedInUser = (Long) session.getAttribute("userId");

        if (loggedInUser == null) {
            return "redirect:/login"; // Redirect to login if user is not in session
        }

        // Fetch parking spots for the logged-in user
        List<ParkingSpot> parkingSpots = updateParkingSpotService.getParkingSpotsForLoggedInUser(loggedInUser);

        // Add parking spots to the model
        model.addAttribute("parkingSpots", parkingSpots);
        return "EditParkingSpot"; // Name of the Thymeleaf template
    }

    @PostMapping("/updateSpot")
    public String updateParkingSpot(
            @RequestParam(value = "id", required = true) Long spotId,
            @RequestParam(value = "availability", required = true) String availability,
            @RequestParam(value = "pricePerHour", required = true) Double pricePerHour,
            @RequestParam(value = "spotType", required = true) String spotType,
            @RequestParam(value = "additionalInstructions", required = true) String additionalInstructions,
            Model model) {
        try {
            boolean isUpdated = updateParkingSpotService.updateParkingSpot(spotId, availability, pricePerHour, spotType, additionalInstructions);
            if (isUpdated) {
                model.addAttribute("message", "Parking spot updated successfully!");
            } else {
                model.addAttribute("message", "Failed to update the parking spot, Try Again.");
            }
        } catch (RuntimeException e) {
            return "redirect:/smart-spots/update-spot";
        } catch (Exception e) {
            return "redirect:/smart-spots/update-spot";
        }
        return "success";
    }

    // Show the remove spot page
    @GetMapping("/remove")
    public String showRemoveParkingSpot( Model model, HttpSession session) {
        Long loggedInUser = (Long) session.getAttribute("userId");

        if (loggedInUser == null) {
            return "redirect:/login"; // Redirect to log-in if user is not in session
        }

        // Fetch updated parking spots for the logged-in user
        List<ParkingSpot> parkingSpots = updateParkingSpotService.getParkingSpotsForLoggedInUser(loggedInUser);
        if(parkingSpots.isEmpty()){
            model.addAttribute("message", "Parking spot not found.");
        }
        else{
            model.addAttribute("parkingSpots", parkingSpots);
        }
        return "RemoveParkingSpot"; // Show the updated remove spot page
    }

    @PostMapping("/remove")
    public String removeParkingSpot(@RequestParam("id") Long id, Model model, HttpSession session) {
        Long loggedInUser = (Long) session.getAttribute("userId");

        if (loggedInUser == null) {
            return "redirect:/login"; // Redirect to log-in if user is not in session
        }

        Optional<ParkingSpot> parkingSpot = parkingSpotRepository.findById(id);
        if (parkingSpot.isPresent()) {
            parkingSpotRepository.delete(parkingSpot.get()); // Remove the parking spot from the database
            model.addAttribute("message", "Parking spot removed successfully.");
        } else {
            model.addAttribute("message", "Parking spot not found.");
        }

        // Fetch updated parking spots for the logged-in user
        List<ParkingSpot> parkingSpots = updateParkingSpotService.getParkingSpotsForLoggedInUser(loggedInUser);
        model.addAttribute("parkingSpots", parkingSpots);

        return "RemoveParkingSpot"; // Show the updated remove spot page
    }
}