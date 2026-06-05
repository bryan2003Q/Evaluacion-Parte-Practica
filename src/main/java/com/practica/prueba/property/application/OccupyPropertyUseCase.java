package com.practica.prueba.property.application;

import com.practica.prueba.property.domain.model.Property;
import com.practica.prueba.property.infrastructure.persistence.PropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OccupyPropertyUseCase {
    private final GetPropertyByIdUseCase getPropertyByIdUseCase;
    private final PropertyRepository propertyRepository;

    @Transactional
    public void execute(Long id) {
        Property property = getPropertyByIdUseCase.execute(id);
        property.occupy();
        propertyRepository.save(property);
    }
}
