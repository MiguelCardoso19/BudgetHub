package com.paymentMicroservice.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @Value("${STRIPE_PUBLIC_KEY}")
    private String STRIPE_PUBLIC_KEY;

    @GetMapping("/")
    public String home(Model model, String email) {
        model.addAttribute("stripePublicKey", STRIPE_PUBLIC_KEY);
        model.addAttribute("email", "test@gmail.com");
        return "checkout";
    }
}