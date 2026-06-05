package com.practica.prueba.rental.application;

import com.practica.prueba.property.application.MakePropertyAvailableUseCase;
import com.practica.prueba.rental.domain.model.Rental;
import com.practica.prueba.rental.infrastructure.persistence.RentalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FinishRentalUseCase {
    private final RentalRepository rentalRepository;
    private final MakePropertyAvailableUseCase makePropertyAvailableUseCase;

    @Transactional
    public void execute(Long rentalId) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new IllegalArgumentException("Arriendo no encontrado"));
        
        rental.finish();
        rentalRepository.save(rental);
        
        makePropertyAvailableUseCase.execute(rental.getPropertyId());
    }
}
