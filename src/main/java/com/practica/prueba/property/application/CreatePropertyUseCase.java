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
public class CreatePropertyUseCase {
    private final PropertyRepository propertyRepository;

    @Transactional
    public Property execute(String name, String street, BigDecimal price) {
        Property property = new Property(name, new Address(street), price);
        return propertyRepository.save(property);
    }
}
