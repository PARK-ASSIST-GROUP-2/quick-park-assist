package com.quick_park_assist.controller;
import com.quick_park_assist.service.IUserService;
import jakarta.servlet.http.HttpSession;
import com.quick_park_assist.util.PasswordMatchValidator;
import com.quick_park_assist.dto.UserRegistrationDTO;
import com.quick_park_assist.entity.User;
import com.quick_park_assist.dto.UserProfileDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {

    // Add this line for logger
    private static final Logger log = LoggerFactory.getLogger(UserController.class);


    @Autowired
    private IUserService userService;

    @Autowired
    private PasswordMatchValidator passwordMatchValidator;

    @GetMapping("/")
    public String home() {
        return "home";
    }


    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new UserRegistrationDTO());
        }
        return "registration";
    }


    @PostMapping("/user-register")
    public String registerUser(@Valid @ModelAttribute("user") UserRegistrationDTO userDTO,
                               BindingResult result,
                               HttpSession session,
                               Model model) {
        try {
            log.info("Received registration request for email: {}", userDTO.getEmail());

            // Custom password match validation
            passwordMatchValidator.validate(userDTO, result);

            // Check for existing email and phone
            if (userService.isEmailTaken(userDTO.getEmail())) {
                result.rejectValue("email", "email.exists", "Email already registered");
            }
            if (userService.isPhoneNumberTaken(userDTO.getPhoneNumber())) {
                result.rejectValue("phoneNumber", "phone.exists", "Phone number already registered");
            }

            if (result.hasErrors()) {
                log.warn("Validation errors occurred: {}", result.getAllErrors());
                return "registration";
            }

            User savedUser = userService.registerUser(userDTO);
            log.info("Successfully registered user with email: {}", savedUser.getEmail());
          //  session.setAttribute("userId", user.getId());
            model.addAttribute("userFullName", session.getAttribute("userFullName"));
            return "dashboard";

        } catch (Exception e) {
            log.error("Error in registration: ", e);
            model.addAttribute("errorMessage", "An error occurred: " + e.getMessage());
            return "registration";
        }
    }

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        log.info("Showing login page");
        // Add any necessary attributes to the model
        return "login";
    }

    @PostMapping("/user-login")
    public String loginUser(@RequestParam String email,
                            @RequestParam String password,
                            HttpSession session,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        try {
            log.info("Login attempt for email: {}", email);

            User user = userService.authenticateUser(email, password);

            if (user != null) {
                // Store user information in session
                session.setAttribute("userId", user.getId());
                session.setAttribute("userEmail", user.getEmail());
                session.setAttribute("userFullName", user.getFullName());
                session.setAttribute("loggedInUser", user);


                log.info("User successfully logged in: {}", email);
                model.addAttribute("userFullName", session.getAttribute("userFullName"));
                return "dashboard";  // Redirect to dashboard after successful login
            } else {
                log.warn("Failed login attempt for email: {}", email);
                redirectAttributes.addFlashAttribute("error", "Invalid email or password");
                return "redirect:/login?error";
            }
        } catch (Exception e) {
            log.error("Error during login: ", e);
            redirectAttributes.addFlashAttribute("error", "An error occurred during login");
            return "redirect:/login?error";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("successMessage", "You have been successfully logged out");
        return "redirect:/login?logout";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        // Check if user is logged in
        if (session.getAttribute("userId") == null) {
            return "redirect:/login";
        }

        // Add user information to model
        model.addAttribute("userFullName", session.getAttribute("userFullName"));
        return "dashboard";  // You'll need to create this view
    }

    @GetMapping("/profileAction")
    public String viewProfile(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        User user = userService.getUserById(userId);
        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);
        return "DeleteProfile";
    }

    @GetMapping("/profile/edit")
    public String showEditProfileForm(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        User user = userService.getUserById(userId);
        if (user == null) {
            return "redirect:/login";
        }

        UserProfileDTO profileDTO = new UserProfileDTO();
        profileDTO.setFullName(user.getFullName());
        profileDTO.setEmail(user.getEmail());
        profileDTO.setPhoneNumber(user.getPhoneNumber());
        profileDTO.setAddress(user.getAddress());

        model.addAttribute("userProfile", profileDTO);
        return "EditProfile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@Valid @ModelAttribute("userProfile") UserProfileDTO profileDTO,
                                BindingResult result,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        if (result.hasErrors()) {
            return "EditProfile";
        }

        try {
            userService.updateProfile(userId, profileDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Profile updated successfully!");
            return "redirect:/dashboard";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating profile: " + e.getMessage());
            return "redirect:/profile/edit";
        }
    }

    @PostMapping("/profile/deactivate")
    public String deactivateAccount(HttpSession session, RedirectAttributes redirectAttributes) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        try {
            userService.deactivateAccount(userId);
            session.invalidate();
            redirectAttributes.addFlashAttribute("successMessage", "Account deactivated successfully");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deactivating account: " + e.getMessage());
            return "redirect:/profileAction";
        }
    }

    @PostMapping("/profile/delete")
    public String deleteAccount(HttpSession session, RedirectAttributes redirectAttributes) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        try {
            userService.deleteAccount(userId);
            session.invalidate();
            redirectAttributes.addFlashAttribute("successMessage", "Account deleted successfully");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting account: " + e.getMessage());
            return "redirect:/profileAction";
        }
    }


}