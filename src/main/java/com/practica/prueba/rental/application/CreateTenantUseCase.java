package com.practica.prueba.rental.application;

import com.practica.prueba.rental.domain.model.Tenant;
import com.practica.prueba.rental.infrastructure.persistence.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateTenantUseCase {
    private final TenantRepository tenantRepository;

    @Transactional
    public Tenant execute(String fullName, String email, String phone) {
        Tenant tenant = new Tenant(fullName, email, phone);
        return tenantRepository.save(tenant);
    }
}
