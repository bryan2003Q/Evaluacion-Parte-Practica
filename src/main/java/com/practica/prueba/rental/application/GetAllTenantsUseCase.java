package com.practica.prueba.rental.application;

import com.practica.prueba.rental.domain.model.Tenant;
import com.practica.prueba.rental.infrastructure.persistence.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllTenantsUseCase {
    private final TenantRepository tenantRepository;

    @Transactional(readOnly = true)
    public List<Tenant> execute() {
        return tenantRepository.findAll();
    }
}
