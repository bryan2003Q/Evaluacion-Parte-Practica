package com.practica.prueba.service;

import com.practica.prueba.model.Tenant;
import com.practica.prueba.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TenantService {

    private final TenantRepository tenantRepository;

    @Transactional(readOnly = true)
    public List<Tenant> getAllTenants() {
        return tenantRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Tenant getTenantById(Long id) {
        return tenantRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Inquilino no encontrado"));
    }

    @Transactional
    public Tenant createTenant(String fullName, String email, String phone) {
        Tenant tenant = new Tenant(fullName, email, phone);
        return tenantRepository.save(tenant);
    }
}
