package com.practica.prueba.rental.application;

import com.practica.prueba.property.application.GetPropertyByIdUseCase;
import com.practica.prueba.property.application.OccupyPropertyUseCase;
import com.practica.prueba.property.domain.model.Property;
import com.practica.prueba.property.domain.model.PropertyStatus;
import com.practica.prueba.rental.domain.model.Rental;
import com.practica.prueba.rental.infrastructure.persistence.RentalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateRentalUseCase {
    private final RentalRepository rentalRepository;
    private final GetTenantByIdUseCase getTenantByIdUseCase;
    private final GetPropertyByIdUseCase getPropertyByIdUseCase;
    private final OccupyPropertyUseCase occupyPropertyUseCase;

    @Transactional
    public Rental execute(Long propertyId, Long tenantId) {
        getTenantByIdUseCase.execute(tenantId);
        
        Property property = getPropertyByIdUseCase.execute(propertyId);
        if (property.getStatus() == PropertyStatus.OCCUPIED) {
            throw new IllegalStateException("La propiedad no está disponible para arrendar.");
        }
        
        occupyPropertyUseCase.execute(propertyId);
        
        Rental rental = new Rental(propertyId, tenantId);
        return rentalRepository.save(rental);
    }
}
