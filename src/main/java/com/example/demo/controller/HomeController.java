package com.example.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String home(
            Authentication authentication,
            Model model,
            @RequestParam(value = "loginSuccess", required = false) String loginSuccess
    ) {
        String username = authentication != null ? authentication.getName() : "";
        model.addAttribute("username", username);

        if (loginSuccess != null) {
            model.addAttribute("mensaje", "Inicio de sesion exitoso");
            model.addAttribute("tipoMensaje", "success");
        }
        return "home";
    }
}
