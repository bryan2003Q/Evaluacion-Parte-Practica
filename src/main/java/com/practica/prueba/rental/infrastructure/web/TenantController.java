package com.practica.prueba.rental.infrastructure.web;

import com.practica.prueba.rental.application.CreateTenantUseCase;
import com.practica.prueba.rental.application.GetAllTenantsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/tenants")
@RequiredArgsConstructor
public class TenantController {

    private final GetAllTenantsUseCase getAllTenantsUseCase;
    private final CreateTenantUseCase createTenantUseCase;

    @GetMapping
    public String listTenants(Model model) {
        model.addAttribute("tenants", getAllTenantsUseCase.execute());
        return "tenants/list";
    }

    @GetMapping("/new")
    public String newTenantForm() {
        return "tenants/form";
    }

    @PostMapping
    public String createTenant(@RequestParam String fullName, @RequestParam String email, @RequestParam String phone) {
        createTenantUseCase.execute(fullName, email, phone);
        return "redirect:/tenants";
    }
}
