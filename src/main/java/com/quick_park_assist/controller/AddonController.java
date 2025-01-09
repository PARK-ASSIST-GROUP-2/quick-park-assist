package com.quick_park_assist.controller;

import com.quick_park_assist.entity.AddonService;
import com.quick_park_assist.entity.ServiceEntity;
import com.quick_park_assist.entity.User;
import com.quick_park_assist.repository.ServiceRepository;
import com.quick_park_assist.entity.ServiceEntity;
import com.quick_park_assist.repository.ServiceRepository;
import com.quick_park_assist.serviceImpl.AddonServiceHandler;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.quick_park_assist.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/addon")
public class AddonController {

    @Autowired
    private AddonServiceHandler addonServiceHandler;
    private final ServiceRepository serviceRepository;
    @Autowired
    private UserRepository userRepository;

    public AddonController(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }


    @GetMapping("/all")
    public String viewAllAddons(Model model,HttpSession session)
    {
    	Long loggedInUser= (Long)session.getAttribute("userId");
    	if(loggedInUser==null)
    	{
    		return "login";
    	}
        List<ServiceEntity> services = serviceRepository.findAll();
        model.addAttribute("addons", services);
        model.addAttribute("addon", new AddonService());
        return "addon-services";
    }

    @GetMapping("/new")
    public String createAddonForm(HttpSession session,Model model) {
        Long loggedInUser= (Long)session.getAttribute("userId");
        if(loggedInUser==null)
        {
            return "login";
        }
        List<ServiceEntity> services = serviceRepository.findAll();
        // Fetch all services
        model.addAttribute("services", services);
        model.addAttribute("addon", new AddonService()); // Add a new AddonService object
        return "create-addon"; // Return the view name
    }


    @PostMapping("/save")
    public String saveAddon(@ModelAttribute("addon") AddonService addonService, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login"; // Redirect to login if the user is not logged in
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Set the user to the bookingSpot
        addonService.setUser(user);
        addonServiceHandler.saveAddon(addonService);
        return "redirect:/addon/all";
    }

    @GetMapping("/edit/{id}")
    public String editAddonForm(@PathVariable Long id, Model model, HttpSession session) {
        Long loggedInUser= (Long)session.getAttribute("userId");
        if(loggedInUser==null)
        {
            return "login";
        }
        List<AddonService> userServices = addonServiceHandler.getAddonByUserId(loggedInUser);
        model.addAttribute("addon", userServices);
        return "create-addon";
    }


    @PostMapping("/update/{id}")
    public String updateAddon(@PathVariable Long id, @ModelAttribute AddonService addonService) {
        addonServiceHandler.updateAddon(id, addonService);
        return "redirect:/addon/all";
    }

    @PostMapping("/delete/{id}")
    public String deleteAddon(@PathVariable Long id) {
        addonServiceHandler.deleteAddonById(id);
        return "redirect:/addon/delete";
    }

    @GetMapping("/modify-duration")
    public String modifyAddonDurationForm(Model model,HttpSession session) {
        Long loggedInUser= (Long)session.getAttribute("userId");
        if(loggedInUser==null)
        {
            return "login";
        }
        List<AddonService> addons = addonServiceHandler.getAllAddons();
        model.addAttribute("addons", addons);
        return "modify-addon-duration";
    }

    @PostMapping("/modify")
    public String modifyAddonDuration(@RequestParam Long addonId, @RequestParam String newDuration) {
        addonServiceHandler.updateAddonDuration(addonId, newDuration);
        return "redirect:/addon/modify-duration";
    }

    @GetMapping("/view")
    public String viewAddonServices(Model model) {
        List<AddonService> addons = addonServiceHandler.getAllAddons();
        model.addAttribute("addons", addons);
        return "view-addon-services";
    }

    @GetMapping("/delete")
    public String deleteAddonPage(Model model) {
        List<AddonService> addons = addonServiceHandler.getAllAddons();
        model.addAttribute("addons", addons);
        return "delete-service";
    }
}
