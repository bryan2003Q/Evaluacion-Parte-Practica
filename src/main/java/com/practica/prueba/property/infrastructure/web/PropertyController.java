package com.practica.prueba.property.infrastructure.web;

import com.practica.prueba.property.application.CreatePropertyUseCase;
import com.practica.prueba.property.application.GetAllPropertiesUseCase;
import com.practica.prueba.property.application.GetPropertyByIdUseCase;
import com.practica.prueba.property.application.UpdatePropertyUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Controller
@RequestMapping("/properties")
@RequiredArgsConstructor
public class PropertyController {

    private final GetAllPropertiesUseCase getAllPropertiesUseCase;
    private final GetPropertyByIdUseCase getPropertyByIdUseCase;
    private final CreatePropertyUseCase createPropertyUseCase;
    private final UpdatePropertyUseCase updatePropertyUseCase;

    @GetMapping
    public String listProperties(Model model) {
        model.addAttribute("properties", getAllPropertiesUseCase.execute());
        return "properties/list";
    }

    @GetMapping("/new")
    public String newPropertyForm(Model model) {
        return "properties/form";
    }

    @PostMapping
    public String createProperty(@RequestParam String name, @RequestParam String street, @RequestParam BigDecimal price) {
        createPropertyUseCase.execute(name, street, price);
        return "redirect:/properties";
    }

    @GetMapping("/{id}/edit")
    public String editPropertyForm(@PathVariable Long id, Model model) {
        model.addAttribute("property", getPropertyByIdUseCase.execute(id));
        return "properties/form";
    }

    @PostMapping("/{id}")
    public String updateProperty(@PathVariable Long id, @RequestParam String name, @RequestParam String street, @RequestParam BigDecimal price) {
        updatePropertyUseCase.execute(id, name, street, price);
        return "redirect:/properties";
    }
}
