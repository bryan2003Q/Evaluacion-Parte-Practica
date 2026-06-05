package com.practica.prueba.controller;

import com.practica.prueba.service.TenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/tenants")
@RequiredArgsConstructor
public class TenantController {

    private final TenantService tenantService;

    @GetMapping
    public String listTenants(Model model) {
        model.addAttribute("tenants", tenantService.getAllTenants());
        return "tenants/list";
    }

    @GetMapping("/new")
    public String newTenantForm() {
        return "tenants/form";
    }

    @PostMapping
    public String createTenant(@RequestParam String fullName, @RequestParam String email, @RequestParam String phone) {
        tenantService.createTenant(fullName, email, phone);
        return "redirect:/tenants";
    }
}
