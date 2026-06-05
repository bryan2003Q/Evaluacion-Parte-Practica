package com.practica.prueba.controller;

import com.practica.prueba.model.PropertyStatus;
import com.practica.prueba.service.PropertyService;
import com.practica.prueba.service.RentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final PropertyService propertyService;
    private final RentalService rentalService;

    @GetMapping("/")
    public String dashboard(Model model) {
        long totalProperties = propertyService.getAllProperties().size();
        long availableProperties = propertyService.getAllProperties().stream()
                .filter(p -> p.getStatus() == PropertyStatus.AVAILABLE)
                .count();
        long occupiedProperties = totalProperties - availableProperties;
        long activeRentals = rentalService.getActiveRentals().size();

        model.addAttribute("totalProperties", totalProperties);
        model.addAttribute("availableProperties", availableProperties);
        model.addAttribute("occupiedProperties", occupiedProperties);
        model.addAttribute("activeRentals", activeRentals);

        return "dashboard";
    }
}
