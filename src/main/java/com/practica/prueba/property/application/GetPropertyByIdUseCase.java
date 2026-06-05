package com.practica.prueba.property.application;

import com.practica.prueba.property.domain.model.Property;
import com.practica.prueba.property.infrastructure.persistence.PropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetPropertyByIdUseCase {
    private final PropertyRepository propertyRepository;

    @Transactional(readOnly = true)
    public Property execute(Long id) {
        return propertyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Propiedad no encontrada"));
    }
}
