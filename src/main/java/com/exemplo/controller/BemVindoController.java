package com.exemplo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class BemVindoController {
    
    @GetMapping("/")
    public String bemVindo(Model model) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
        model.addAttribute("currentTime", now.format(formatter));
        model.addAttribute("userName", "Desenvolvedor");
        model.addAttribute("message", "Bem-vindo à sua aplicação Spring Boot com Thymeleaf!");
        
        return "bem-vindo";
    }
}
