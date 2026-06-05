package com.practica.prueba.rental.infrastructure.web;

import com.practica.prueba.property.application.GetAllPropertiesUseCase;
import com.practica.prueba.property.application.GetPropertyByIdUseCase;
import com.practica.prueba.rental.application.CreateRentalUseCase;
import com.practica.prueba.rental.application.FinishRentalUseCase;
import com.practica.prueba.rental.application.GetAllRentalsUseCase;
import com.practica.prueba.rental.application.GetAllTenantsUseCase;
import com.practica.prueba.rental.application.GetTenantByIdUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/rentals")
@RequiredArgsConstructor
public class RentalController {

    private final GetAllRentalsUseCase getAllRentalsUseCase;
    private final CreateRentalUseCase createRentalUseCase;
    private final FinishRentalUseCase finishRentalUseCase;
    
    private final GetAllPropertiesUseCase getAllPropertiesUseCase;
    private final GetPropertyByIdUseCase getPropertyByIdUseCase;
    
    private final GetAllTenantsUseCase getAllTenantsUseCase;
    private final GetTenantByIdUseCase getTenantByIdUseCase;

    @GetMapping
    public String listRentals(Model model) {
        model.addAttribute("rentals", getAllRentalsUseCase.execute());
        
        // Exposing use cases to thymeleaf so it can fetch property and tenant names by ID
        // Note: For a clean DDD approach we could map this to a DTO in the controller, 
        // but sticking to the simple approach as requested.
        model.addAttribute("propertyService", getPropertyByIdUseCase);
        model.addAttribute("tenantService", getTenantByIdUseCase);
        return "rentals/list";
    }

    @GetMapping("/new")
    public String newRentalForm(Model model) {
        model.addAttribute("properties", getAllPropertiesUseCase.execute());
        model.addAttribute("tenants", getAllTenantsUseCase.execute());
        return "rentals/form";
    }

    @PostMapping
    public String createRental(@RequestParam Long propertyId, @RequestParam Long tenantId, Model model) {
        try {
            createRentalUseCase.execute(propertyId, tenantId);
        } catch (IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("properties", getAllPropertiesUseCase.execute());
            model.addAttribute("tenants", getAllTenantsUseCase.execute());
            return "rentals/form";
        }
        return "redirect:/rentals";
    }

    @PostMapping("/{id}/finish")
    public String finishRental(@PathVariable Long id) {
        finishRentalUseCase.execute(id);
        return "redirect:/rentals";
    }
}
