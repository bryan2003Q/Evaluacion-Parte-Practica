package com.practica.prueba.controller;

import com.practica.prueba.service.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Controller
@RequestMapping("/properties")
@RequiredArgsConstructor
public class PropertyController {

    private final PropertyService propertyService;

    @GetMapping
    public String listProperties(Model model) {
        model.addAttribute("properties", propertyService.getAllProperties());
        return "properties/list";
    }

    @GetMapping("/new")
    public String newPropertyForm() {
        return "properties/form";
    }

    @PostMapping
    public String createProperty(@RequestParam String name, @RequestParam String street, @RequestParam BigDecimal price) {
        propertyService.createProperty(name, street, price);
        return "redirect:/properties";
    }

    @GetMapping("/{id}/edit")
    public String editPropertyForm(@PathVariable Long id, Model model) {
        model.addAttribute("property", propertyService.getPropertyById(id));
        return "properties/form";
    }

    @PostMapping("/{id}")
    public String updateProperty(@PathVariable Long id, @RequestParam String name, @RequestParam String street, @RequestParam BigDecimal price) {
        propertyService.updateProperty(id, name, street, price);
        return "redirect:/properties";
    }
}
