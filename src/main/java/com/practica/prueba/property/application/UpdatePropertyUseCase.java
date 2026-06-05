package com.practica.prueba.property.application;

import com.practica.prueba.property.domain.model.Address;
import com.practica.prueba.property.domain.model.Property;
import com.practica.prueba.property.infrastructure.persistence.PropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class UpdatePropertyUseCase {
    private final GetPropertyByIdUseCase getPropertyByIdUseCase;
    private final PropertyRepository propertyRepository;

    @Transactional
    public void execute(Long id, String name, String street, BigDecimal price) {
        Property property = getPropertyByIdUseCase.execute(id);
        property.update(name, new Address(street), price);
        propertyRepository.save(property);
    }
}
