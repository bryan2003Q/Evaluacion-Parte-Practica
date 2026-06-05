package com.practica.prueba.service;

import com.practica.prueba.model.Property;
import com.practica.prueba.model.PropertyStatus;
import com.practica.prueba.repository.PropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PropertyService {

    private final PropertyRepository propertyRepository;

    @Transactional(readOnly = true)
    public List<Property> getAllProperties() {
        return propertyRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Property getPropertyById(Long id) {
        return propertyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Propiedad no encontrada"));
    }

    @Transactional
    public Property createProperty(String name, String street, BigDecimal monthlyPrice) {
        Property property = new Property(name, street, monthlyPrice);
        return propertyRepository.save(property);
    }

    @Transactional
    public void updateProperty(Long id, String name, String street, BigDecimal monthlyPrice) {
        Property property = getPropertyById(id);
        property.setName(name);
        property.setStreet(street);
        property.setMonthlyPrice(monthlyPrice);
        propertyRepository.save(property);
    }

    @Transactional
    public void setOccupied(Long id) {
        Property property = getPropertyById(id);
        property.setStatus(PropertyStatus.OCCUPIED);
        propertyRepository.save(property);
    }

    @Transactional
    public void setAvailable(Long id) {
        Property property = getPropertyById(id);
        property.setStatus(PropertyStatus.AVAILABLE);
        propertyRepository.save(property);
    }
}
