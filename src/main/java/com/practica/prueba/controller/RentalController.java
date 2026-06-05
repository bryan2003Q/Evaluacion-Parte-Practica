package com.practica.prueba.controller;

import com.practica.prueba.service.PropertyService;
import com.practica.prueba.service.RentalService;
import com.practica.prueba.service.TenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/rentals")
@RequiredArgsConstructor
public class RentalController {

    private final RentalService rentalService;
    private final PropertyService propertyService;
    private final TenantService tenantService;

    @GetMapping
    public String listRentals(Model model) {
        model.addAttribute("rentals", rentalService.getAllRentals());
        model.addAttribute("properties", propertyService.getAllProperties());
        model.addAttribute("tenants", tenantService.getAllTenants());
        return "rentals/list";
    }

    @GetMapping("/new")
    public String newRentalForm(Model model) {
        model.addAttribute("properties", propertyService.getAllProperties());
        model.addAttribute("tenants", tenantService.getAllTenants());
        return "rentals/form";
    }

    @PostMapping
    public String createRental(@RequestParam Long propertyId, @RequestParam Long tenantId, Model model) {
        try {
            rentalService.createRental(propertyId, tenantId);
        } catch (IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("properties", propertyService.getAllProperties());
            model.addAttribute("tenants", tenantService.getAllTenants());
            return "rentals/form";
        }
        return "redirect:/rentals";
    }

    @PostMapping("/{id}/finish")
    public String finishRental(@PathVariable Long id) {
        rentalService.finishRental(id);
        return "redirect:/rentals";
    }
}
