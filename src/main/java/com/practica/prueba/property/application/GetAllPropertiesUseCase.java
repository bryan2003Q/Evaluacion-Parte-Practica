package com.practica.prueba.property.application;

import com.practica.prueba.property.domain.model.Property;
import com.practica.prueba.property.infrastructure.persistence.PropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllPropertiesUseCase {
    private final PropertyRepository propertyRepository;

    @Transactional(readOnly = true)
    public List<Property> execute() {
        return propertyRepository.findAll();
    }
}
