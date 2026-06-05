package com.practica.prueba.shared.infrastructure.web;

import com.practica.prueba.property.application.GetAllPropertiesUseCase;
import com.practica.prueba.property.domain.model.PropertyStatus;
import com.practica.prueba.rental.application.GetActiveRentalsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final GetAllPropertiesUseCase getAllPropertiesUseCase;
    private final GetActiveRentalsUseCase getActiveRentalsUseCase;

    @GetMapping("/")
    public String dashboard(Model model) {
        long totalProperties = getAllPropertiesUseCase.execute().size();
        long availableProperties = getAllPropertiesUseCase.execute().stream()
                .filter(p -> p.getStatus() == PropertyStatus.AVAILABLE)
                .count();
        long occupiedProperties = totalProperties - availableProperties;
        long activeRentals = getActiveRentalsUseCase.execute().size();

        model.addAttribute("totalProperties", totalProperties);
        model.addAttribute("availableProperties", availableProperties);
        model.addAttribute("occupiedProperties", occupiedProperties);
        model.addAttribute("activeRentals", activeRentals);

        return "dashboard";
    }
}
