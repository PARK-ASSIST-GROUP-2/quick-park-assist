package com.quick_park_assist.controller;

import com.quick_park_assist.dto.VehicleDTO;
import com.quick_park_assist.entity.Vehicle;
import com.quick_park_assist.service.IVehicleService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/vehicles")
public class VehicleController {

    @Autowired
    private IVehicleService vehicleService;
    @GetMapping("api/stats")
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("availableSpots", 20);  // Replace with dynamic data from your database
        stats.put("activeBookings", 5);
        stats.put("totalHours", 75);
        stats.put("amountSpent", 150);
        return stats;
    }

    @GetMapping("/editVehicle")
    public String listVehicles(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        List<Vehicle> vehicles = vehicleService.getVehiclesByUserId(userId);
        //model.addAttribute("vehicle",new Vehicle());
        model.addAttribute("vehicles", vehicles);
        return "EditVehicle";
    }

    @GetMapping("/add")
    public String showAddVehicleForm(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        model.addAttribute("vehicle", new VehicleDTO());
        return "AddVehicle";
    }

    @PostMapping("/new-vehicle")
    public String addVehicle(@Valid @ModelAttribute("vehicle") VehicleDTO vehicleDTO,
                             BindingResult result,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "AddVehicle";
        }

        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        try {
            vehicleService.addVehicle(userId, vehicleDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Vehicle added successfully!");
            return "redirect:/dashboard";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error adding vehicle: " + e.getMessage());
            return "AddVehicle";
        }
    }

    @GetMapping("/{id}")
    public String viewVehicle(@PathVariable("id") Long vehicleId,
                              HttpSession session,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        try {
            Vehicle vehicle = vehicleService.getVehicleByIdAndUserId(vehicleId, userId);
            model.addAttribute("vehicle", vehicle);
            return "ListVehicle";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/dashboard";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long vehicleId,
                               HttpSession session,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        try {
            Vehicle vehicle = vehicleService.getVehicleByIdAndUserId(vehicleId, userId);
            VehicleDTO vehicleDTO = new VehicleDTO();
            vehicleDTO.setVehicleNumber(vehicle.getVehicleNumber());
            vehicleDTO.setVehicleType(vehicle.getVehicleType());
            vehicleDTO.setManufacturer(vehicle.getManufacturer());
            vehicleDTO.setModel(vehicle.getModel());
            vehicleDTO.setColor(vehicle.getColor());

            model.addAttribute("vehicleId", vehicleId);
            model.addAttribute("vehicle", vehicleDTO);
            return "/";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/vehicles";
        }
    }

    @PostMapping("/update/{id}")
    public String updateVehicle(@PathVariable("id") Long vehicleId,
                                @Valid @ModelAttribute("vehicle") VehicleDTO vehicleDTO,
                                BindingResult result,
                                HttpSession session,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        if (result.hasErrors()) {
            model.addAttribute("vehicleId", vehicleId);
            return "vehicles/edit";
        }

        try {
            vehicleService.updateVehicle(vehicleId, userId, vehicleDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Vehicle updated successfully!");
            return "redirect:/dashboard";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating vehicle: " + e.getMessage());
            return "redirect:/editVehicle";
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteVehicle(@PathVariable("id") Long vehicleId,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        try {
            vehicleService.deleteVehicle(vehicleId, userId);
            redirectAttributes.addFlashAttribute("successMessage", "Vehicle deleted successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting vehicle: " + e.getMessage());
        }

        return "redirect:/dashboard";
    }
}