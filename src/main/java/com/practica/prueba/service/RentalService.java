package com.practica.prueba.service;

import com.practica.prueba.model.Property;
import com.practica.prueba.model.PropertyStatus;
import com.practica.prueba.model.Rental;
import com.practica.prueba.repository.RentalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RentalService {

    private final RentalRepository rentalRepository;
    private final PropertyService propertyService;
    private final TenantService tenantService;

    @Transactional(readOnly = true)
    public List<Rental> getAllRentals() {
        return rentalRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Rental> getActiveRentals() {
        return rentalRepository.findByActiveTrue();
    }

    @Transactional
    public Rental createRental(Long propertyId, Long tenantId) {
        // Validar que el inquilino existe
        tenantService.getTenantById(tenantId);

        // Validar que la propiedad está disponible
        Property property = propertyService.getPropertyById(propertyId);
        if (property.getStatus() == PropertyStatus.OCCUPIED) {
            throw new IllegalStateException("La propiedad no está disponible para arrendar.");
        }

        // Cambiar estado de la propiedad a ocupada
        propertyService.setOccupied(propertyId);

        // Crear el arriendo
        Rental rental = new Rental(propertyId, tenantId);
        return rentalRepository.save(rental);
    }

    @Transactional
    public void finishRental(Long rentalId) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new IllegalArgumentException("Arriendo no encontrado"));

        if (!rental.isActive()) {
            throw new IllegalStateException("El arriendo ya se encuentra finalizado.");
        }

        rental.setActive(false);
        rental.setEndDate(LocalDate.now());
        rentalRepository.save(rental);

        // Liberar la propiedad
        propertyService.setAvailable(rental.getPropertyId());
    }
}
