package com.practica.prueba.rental.application;

import com.practica.prueba.rental.domain.model.Tenant;
import com.practica.prueba.rental.infrastructure.persistence.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetTenantByIdUseCase {
    private final TenantRepository tenantRepository;

    @Transactional(readOnly = true)
    public Tenant execute(Long id) {
        return tenantRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Inquilino no encontrado"));
    }
}
