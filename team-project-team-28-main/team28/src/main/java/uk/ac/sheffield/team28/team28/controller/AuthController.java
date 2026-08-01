package uk.ac.sheffield.team28.team28.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import uk.ac.sheffield.team28.team28.dto.MemberRegistrationDto;
import uk.ac.sheffield.team28.team28.service.MemberService;


@Controller
@RequestMapping("/auth")
public class AuthController {
    private final MemberService memberService;

    public AuthController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/logout")
    public String logout() {
        return "home";
    }

    @GetMapping("/login")
    public String showLoginForm(@RequestParam(value = "success", required = false) String successMessage,
                                @RequestParam(value = "error", required = false) String errorMessage,
                                Model model) {
        model.addAttribute("successMessage", successMessage);
        model.addAttribute("errorMessage", errorMessage);
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("member", new MemberRegistrationDto());
        return "register";
    }

    @PostMapping("/register")
    public String performRegister(@Valid @ModelAttribute("member") MemberRegistrationDto registrationDto,
                                  Model model) {

        try {
            memberService.registerMember(registrationDto);
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "register"; 
        }

        model.addAttribute("successMessage", "Registration completed successfully, please login.");
        return "/login";
    }
}