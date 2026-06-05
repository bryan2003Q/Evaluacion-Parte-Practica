package com.practica.prueba.property.infrastructure.persistence;

import com.practica.prueba.property.domain.model.Property;
import com.practica.prueba.property.domain.model.PropertyStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
    List<Property> findByStatus(PropertyStatus status);
}
